package com.example.maverickfilesender.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.adapters.AppPackageRecyclerAdapter
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.databinding.FragmentAppsBinding
import com.example.maverickfilesender.model.AppPackage

class AppsFragment : Fragment() {

    private var _binding: FragmentAppsBinding? = null
    private val binding get() = _binding!!

    private var mainActivity: MainActivity? = null
    var adapter: AppPackageRecyclerAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as? MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity?.appsFragment = this

        // Loading installed apps doesn't require storage permissions
        // It only requires QUERY_ALL_PACKAGES which is declared in manifest
        loadInstalledApps()
    }

    private fun loadInstalledApps() {
        val packageManager = requireContext().packageManager
        binding.progressApps.visibility = View.VISIBLE

        Thread {
            val packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            val nonSystemPackages = ArrayList<AppPackage>()

            packages.forEach { info ->
                if (info.sourceDir.startsWith("/data/app/")) {
                    nonSystemPackages.add(AppPackage(info, info.loadIcon(packageManager), false, null))
                }
            }

            mainActivity?.runOnUiThread {
                val fragmentBinding = _binding ?: return@runOnUiThread
                val activity = mainActivity ?: return@runOnUiThread
                adapter = AppPackageRecyclerAdapter(activity, nonSystemPackages)

                fragmentBinding.rvApps.layoutManager = GridLayoutManager(activity, 4)
                fragmentBinding.rvApps.adapter = adapter
                fragmentBinding.rvApps.setHasFixedSize(true)
                fragmentBinding.rvApps.setItemViewCacheSize(20)
                fragmentBinding.progressApps.visibility = View.GONE
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}