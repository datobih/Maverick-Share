package com.example.maverickfilesender.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maverickfilesender.R
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.adapters.HistoryFilesRecyclerAdapter
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.model.AppFile
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import kotlinx.android.synthetic.main.fragment_videos.*
import java.io.Console
import java.io.File
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class HistoryFragment : Fragment() {
var mContext:Context?=null
    var mSharedPreferences : SharedPreferences?=null
    override fun onAttach(context: Context) {
        super.onAttach(context)

    mContext=context
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSharedPreferences= mContext!!.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        ll_fileHistory_mode.setOnClickListener {

            if(!Constants.isMedia){
                tv_fileHistory_mode.text="Media Files"
           imb_fileHistory_mode.setImageResource(R.drawable.ic_media_icon)
fetchMediaFiles()
            Constants.isMedia=true


            }

            else{
                tv_fileHistory_mode.text="General Files"
                imb_fileHistory_mode.setImageResource(R.drawable.file_icon)
fetchGeneralFiles()
                Constants.isMedia=false

            }

        }



        if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.R){
            if(!Environment.isExternalStorageManager()){
                val intent= Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri= Uri.fromParts("package",mContext!!.packageName,null)
                intent.setData(uri)
                startActivity(intent)

            }

        }






    }

    fun fetchGeneralFiles(){

        var files = ContextCompat.getExternalFilesDirs(mContext!!, null)
        if(files.size>1){
var sdDirectory=""
try {
  sdDirectory = getSDirectory(files[1].path)
}
catch (e:Exception){
   sdDirectory=""
}

            if(Constants.currentHistoryLocation!="/storage/emulated/0" && Constants.currentHistoryLocation!=sdDirectory && sdDirectory.isNotEmpty()){

                Constants.currentHistoryLocation="/storage/emulated/0"

                val editor=mSharedPreferences!!.edit()

                editor.putString(Constants.SP_HISTORY_LOCATION,"/storage/emulated/0")



            }

        }


        val fileDir= File("${Constants.currentHistoryLocation}/Download/Maverick")
        val receivedFiles=fileDir.listFiles()
        val receivedAppFile=ArrayList<AppFile>()

        progress_history.visibility=View.VISIBLE

        val fetchThread = Thread {


            //Arrays.sort(receivedFiles, Comparator.comparingLong(File::lastModified))
            if (!receivedFiles.isNullOrEmpty()) {
                receivedFiles.let {
                    Arrays.sort(it) { f1, f2 ->
                        f2.lastModified().compareTo(f1.lastModified())
                    }

                    receivedFiles.forEach {
                        try {
                            if (it.name.endsWith(".apk")) {
                                val packageManager = mContext!!.packageManager
                                val packageInfo = packageManager.getPackageArchiveInfo(it.path, 0)
                                val drawable = packageInfo!!.applicationInfo.loadIcon(packageManager)
                                receivedAppFile.add(AppFile(it, drawable, null, null))
                            } else {
                                receivedAppFile.add(AppFile(it, null, null, null))
                            }
                        } catch (e: Exception) {

                        }
                    }


                }
            }
        }.also {
            it.start()
            it.join()
        }



        if(receivedAppFile.isEmpty()){
            tv_history_noFiles.visibility=View.VISIBLE
            rv_history.visibility=View.GONE
        }

        else {
            rv_history.visibility=View.VISIBLE
            tv_history_noFiles.visibility=View.GONE


            val adapter = HistoryFilesRecyclerAdapter(mContext!!, receivedAppFile)
            rv_history.layoutManager = LinearLayoutManager(mContext)
            rv_history.adapter = adapter
        }
        progress_history.visibility=View.GONE
    }

    fun fetchMediaFiles(){


        var files = ContextCompat.getExternalFilesDirs(mContext!!, null)
        if(files.size>1){


            val sdDirectory=getSDirectory(files[1].path)


            if(Constants.currentHistoryLocation!="/storage/emulated/0" && Constants.currentHistoryLocation!=sdDirectory){

                Constants.currentHistoryLocation="/storage/emulated/0"

                val editor=mSharedPreferences!!.edit()

                editor.putString(Constants.SP_HISTORY_LOCATION,"/storage/emulated/0")



            }

        }

        val fileDir= File("${Constants.currentHistoryLocation}/DCIM/Maverick")
        val receivedFiles=fileDir.listFiles()
        val receivedAppFile=ArrayList<AppFile>()

        progress_history.visibility=View.VISIBLE

        val fetchThread = Thread {


            //Arrays.sort(receivedFiles, Comparator.comparingLong(File::lastModified))
            if (!receivedFiles.isNullOrEmpty()) {
                receivedFiles.let {
                    Arrays.sort(it) { f1, f2 ->
                        f2.lastModified().compareTo(f1.lastModified())
                    }

                    receivedFiles.forEach {
                        try {
                            if (it.name.endsWith(".apk")) {
                                val packageManager = mContext!!.packageManager
                                val packageInfo = packageManager.getPackageArchiveInfo(it.path, 0)
                                val drawable = packageInfo!!.applicationInfo.loadIcon(packageManager)
                                receivedAppFile.add(AppFile(it, drawable, null, null))
                            } else {
                                receivedAppFile.add(AppFile(it, null, null, null))
                            }
                        } catch (e: Exception) {

                        }
                    }


                }
            }
        }.also {
            it.start()
            it.join()
        }



        if(receivedAppFile.isEmpty()){
            tv_history_noFiles.visibility=View.VISIBLE
            rv_history.visibility=View.GONE
        }

        else {
            rv_history.visibility=View.VISIBLE
            tv_history_noFiles.visibility=View.GONE


            val adapter = HistoryFilesRecyclerAdapter(mContext!!, receivedAppFile)
            rv_history.layoutManager = LinearLayoutManager(mContext)
            rv_history.adapter = adapter
        }
        progress_history.visibility=View.GONE
    }

    fun getSDirectory(path: String): String {
        var sdPath = ""
        var count = 0

        for (i in path) {

            if (i == '/') {

                count++

            }

            if (count == 3) {
                break
            }
            sdPath += i

        }

        return sdPath
    }

    fun refreshUI(){
        if(!Constants.isMedia) {
            fetchGeneralFiles()
        }
        else{
            fetchMediaFiles()
        }
    }



    override fun onResume() {
      refreshUI()

        super.onResume()
    }

}