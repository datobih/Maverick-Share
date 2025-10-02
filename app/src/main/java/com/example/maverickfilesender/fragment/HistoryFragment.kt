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
import com.example.maverickfilesender.databinding.FragmentHistoryBinding
import java.io.Console
import java.io.File
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

var mContext:Context?=null
    var mSharedPreferences : SharedPreferences?=null
    override fun onAttach(context: Context) {
        super.onAttach(context)

    mContext=context
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (context as MainActivity).historyFragment=this
        mSharedPreferences= mContext!!.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        binding.llFileHistoryMode.setOnClickListener {

            if(!Constants.isMedia){
                binding.tvFileHistoryMode.text="Media Files"
           binding.imbFileHistoryMode.setImageResource(R.drawable.ic_media_icon)
fetchMediaFiles()
            Constants.isMedia=true


            }

            else{
                binding.tvFileHistoryMode.text="General Files"
                binding.imbFileHistoryMode.setImageResource(R.drawable.file_icon)
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

        binding.progressHistory.visibility=View.VISIBLE

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
                                val drawable = packageInfo?.applicationInfo?.loadIcon(packageManager)
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
            binding.tvHistoryNoFiles.visibility=View.VISIBLE
            binding.rvHistory.visibility=View.GONE
        }

        else {
            binding.rvHistory.visibility=View.VISIBLE
            binding.tvHistoryNoFiles.visibility=View.GONE


            val adapter = HistoryFilesRecyclerAdapter(mContext!!, receivedAppFile)
            binding.rvHistory.layoutManager = LinearLayoutManager(mContext)
            binding.rvHistory.adapter = adapter
        }
        binding.progressHistory.visibility=View.GONE
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

        binding.progressHistory.visibility=View.VISIBLE

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
                                val drawable = packageInfo?.applicationInfo?.loadIcon(packageManager)
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
            binding.tvHistoryNoFiles.visibility=View.VISIBLE
            binding.rvHistory.visibility=View.GONE
        }

        else {
            binding.rvHistory.visibility=View.VISIBLE
            binding.tvHistoryNoFiles.visibility=View.GONE


            val adapter = HistoryFilesRecyclerAdapter(mContext!!, receivedAppFile)
            binding.rvHistory.layoutManager = LinearLayoutManager(mContext)
            binding.rvHistory.adapter = adapter
        }
        binding.progressHistory.visibility=View.GONE
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

    override fun onDestroy() {

        (context as MainActivity).historyFragment=null
        super.onDestroy()
    }

    override fun onResume() {
      refreshUI()

        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}