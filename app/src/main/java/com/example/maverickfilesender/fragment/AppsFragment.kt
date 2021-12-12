package com.example.maverickfilesender.fragment

import android.content.Context
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
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.adapters.AppPackageRecyclerAdapter
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.model.AppPackage
import kotlinx.android.synthetic.main.fragment_apps.*
import kotlinx.android.synthetic.main.fragment_apps.view.*


class AppsFragment : Fragment() {

var mContext:Context?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_apps, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        if(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){


            val pm=requireContext().packageManager
progress_apps.visibility=View.VISIBLE

            val fetchAppsThread= Thread(object :Runnable{
                override fun run() {

                    val packages=pm.getInstalledApplications(PackageManager.GET_META_DATA)
                    val nonSystemPackages=ArrayList<AppPackage>()
                    for(i: ApplicationInfo in packages){

                        if(i.sourceDir.startsWith("/data/app/")){


                            nonSystemPackages.add(AppPackage(i,i.loadIcon(mContext!!.packageManager)))

                        }


                    }





                    (mContext as MainActivity).runOnUiThread{

                        val adapter= AppPackageRecyclerAdapter(mContext!!,nonSystemPackages)

                        view.rv_apps.layoutManager= GridLayoutManager(mContext,4)
                        view.rv_apps.adapter=adapter
                        view.rv_apps.setHasFixedSize(true)
                        view.rv_apps.setItemViewCacheSize(20)
view.progress_apps.visibility=View.GONE
                    }


                }


            }).start()



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