package com.example.maverickfilesender.fragment

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.maverickfilesender.R
import com.example.maverickfilesender.adapters.AppPackageRecyclerAdapter
import com.example.maverickfilesender.constants.Constants
import kotlinx.android.synthetic.main.fragment_apps.*


class AppsFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_apps, container, false)
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){


            val pm=requireContext().packageManager

            val packages=pm.getInstalledApplications(PackageManager.GET_META_DATA)
            val nonSystemPackages=ArrayList<ApplicationInfo>()
            for(i: ApplicationInfo in packages){

                if(i.sourceDir.startsWith("/data/app/")){

                    nonSystemPackages.add(i)

                }


            }

            rv_apps.layoutManager= GridLayoutManager(requireContext(),4)

            val adapter= AppPackageRecyclerAdapter(requireContext(),nonSystemPackages)

            rv_apps.adapter=adapter

        }

        else{

            if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),android.Manifest.permission.READ_EXTERNAL_STORAGE)||
                ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){

                var intent= Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                var uri= Uri.fromParts("package",requireContext().packageName,null)
                intent.setData(uri)
                startActivity(intent)



            }


            else{
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.RQ_READ_WRITE_PERMISSION)



            }



        }

    }




}