package com.example.maverickfilesender.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maverickfilesender.R
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.adapters.FilesRecyclerAdapter
import com.example.maverickfilesender.adapters.RelativePathRecyclerAdapter
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.listeners.FileOnClickListener
import com.example.maverickfilesender.model.AppFile
import com.example.maverickfilesender.model.RelativePath
import kotlinx.android.synthetic.main.fragment_files_directory.view.*
import java.io.File
import java.nio.file.Files


class FilesDirectoryFragment : Fragment() {


    var mContext: Context?=null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_files_directory, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
val bundle=this.arguments
val directory=bundle!!.getString(Constants.BUNDLE_STORAGE_DIRECTORY)




      val file= File(directory)

        val files:Array<File> =file.listFiles()

        val into=ArrayList<File>()




        val relativePathList=ArrayList<RelativePath>()
        relativePathList.add(RelativePath("Home",file))

        var relativePathAdapter=RelativePathRecyclerAdapter(mContext!!,relativePathList)


view.rv_relativePath.layoutManager=LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)
view.rv_relativePath.adapter=relativePathAdapter


        var filesAdapter=FilesRecyclerAdapter(mContext!!,generateAppFile(files))

        val mFileOnClickListener=object : FileOnClickListener{
            override fun onClick(file: File) {
                relativePathList.add(RelativePath(file.name,file))

                relativePathAdapter=RelativePathRecyclerAdapter(mContext!!,relativePathList)
                view.rv_relativePath.adapter=relativePathAdapter


                val mFiles=file.listFiles()



                filesAdapter=FilesRecyclerAdapter(mContext!!,generateAppFile(mFiles))
                filesAdapter.setOnClickListener(this)



                view.rv_files.adapter=filesAdapter
            }


        }
        filesAdapter.setOnClickListener(mFileOnClickListener)

        view.rv_files.layoutManager=LinearLayoutManager(mContext)
        view.rv_files.adapter=filesAdapter



    }

    fun generateAppFile(fileList:Array<File>): ArrayList<AppFile>{

        val appFiles=ArrayList<AppFile>()

        for (i in fileList){
            if( i.name.endsWith(".apk")){
                val packageManager=mContext!!.packageManager
                val packageInfo=packageManager.getPackageArchiveInfo(i.path,0)


                appFiles.add(AppFile(i,packageInfo!!.applicationInfo.loadIcon(packageManager)))

            }
            else{
                appFiles.add(AppFile(i))
            }

        }
        return appFiles
    }

}