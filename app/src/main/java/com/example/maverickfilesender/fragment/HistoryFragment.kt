package com.example.maverickfilesender.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maverickfilesender.R
import com.example.maverickfilesender.adapters.HistoryFilesRecyclerAdapter
import com.example.maverickfilesender.model.AppFile
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import java.io.File
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class HistoryFragment : Fragment() {
var mContext:Context?=null
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

        if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.R){
            if(!Environment.isExternalStorageManager()){
                val intent= Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri= Uri.fromParts("package",mContext!!.packageName,null)
                intent.setData(uri)
                startActivity(intent)

            }

        }

fetchFiles()





    }

    fun fetchFiles(){


        val fileDir= File("/storage/emulated/0/Download/Maverick")
        val receivedFiles=fileDir.listFiles()
        val receivedAppFile=ArrayList<AppFile>()
        //Arrays.sort(receivedFiles, Comparator.comparingLong(File::lastModified))
if(!receivedFiles.isNullOrEmpty()){
        receivedFiles.let{
    Arrays.sort(it){
        f1,f2-> f2.lastModified().compareTo(f1.lastModified())
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
}
catch (e:Exception){

}
    }




}
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
    }

    override fun onResume() {
fetchFiles()

        super.onResume()
    }

}