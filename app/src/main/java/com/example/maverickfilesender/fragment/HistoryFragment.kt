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
import com.example.maverickfilesender.R
import java.io.File


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


        val fileDir= File(mContext!!.getExternalFilesDir(null)!!.path+"/Received")
        val receivedFiles=fileDir.listFiles()



    }


}