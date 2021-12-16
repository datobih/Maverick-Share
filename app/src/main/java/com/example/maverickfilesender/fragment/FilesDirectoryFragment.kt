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
import com.example.maverickfilesender.listeners.RelativePathOnClickListener
import com.example.maverickfilesender.model.AppFile
import com.example.maverickfilesender.model.RelativePath
import kotlinx.android.synthetic.main.fragment_files_directory.*
import kotlinx.android.synthetic.main.fragment_files_directory.view.*
import kotlinx.android.synthetic.main.fragment_videos.*
import java.io.File
import java.nio.file.Files


class FilesDirectoryFragment : Fragment() {


    var mContext: Context?=null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context

    }
    val relativePathList=ArrayList<RelativePath>()
    var mFileOnClickListener:FileOnClickListener?=null
    var mRelativePathOnClickListener:RelativePathOnClickListener?=null
    var filesAdapter:FilesRecyclerAdapter?=null

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





        relativePathList.add(RelativePath("Home",file,generateAppFile(files)))

        var relativePathAdapter=RelativePathRecyclerAdapter(mContext!!,relativePathList)




view.rv_relativePath.layoutManager=LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)


        mRelativePathOnClickListener=object :RelativePathOnClickListener{

            override fun onClick(appFileList:ArrayList<AppFile>,position:Int) {
var list=((view.rv_relativePath.adapter) as RelativePathRecyclerAdapter).relativePathList

if(position!=(list.lastIndex)){

list=removeElementsFrom(list,position)
   view.rv_relativePath.adapter!!.notifyDataSetChanged()

    filesAdapter= FilesRecyclerAdapter(mContext!!,appFileList)
    filesAdapter!!.setOnClickListener(mFileOnClickListener!!)
rv_files.adapter=filesAdapter

}


            }
        }
        relativePathAdapter.setOnClickListener(mRelativePathOnClickListener!!)

view.rv_relativePath.adapter=relativePathAdapter


         filesAdapter=FilesRecyclerAdapter(mContext!!,relativePathList[0].appFileList)



         mFileOnClickListener=object : FileOnClickListener{
            override fun onClick(file: File) {


val appFileList=generateAppFile(file.listFiles())
                relativePathList.add(RelativePath(file.name,file,appFileList))
Constants.mRelativePath=relativePathList


                relativePathAdapter=RelativePathRecyclerAdapter(mContext!!,relativePathList)


relativePathAdapter.setOnClickListener(mRelativePathOnClickListener!!)

                view.rv_relativePath.adapter=relativePathAdapter


                val mFiles=file.listFiles()



                filesAdapter=FilesRecyclerAdapter(mContext!!,appFileList)
                filesAdapter!!.setOnClickListener(this)



                view.rv_files.adapter=filesAdapter
            }


        }


        filesAdapter!!.setOnClickListener(mFileOnClickListener!!)

        view.rv_files.layoutManager=LinearLayoutManager(mContext)
        view.rv_files.adapter=filesAdapter



    }



    fun generateAppFile(fileList:Array<File>): ArrayList<AppFile>{

        val appFiles=ArrayList<AppFile>()

        for (i in fileList){
            if( i.name.endsWith(".apk")){
                val packageManager=mContext!!.packageManager
                val packageInfo=packageManager.getPackageArchiveInfo(i.path,0)


                appFiles.add(AppFile(i,packageInfo!!.applicationInfo.loadIcon(packageManager),false))

            }
            else{
                appFiles.add(AppFile(i,onSelect = false))
            }

        }
        return appFiles
    }




    fun removeElementsFrom(list:ArrayList<RelativePath>,position:Int):ArrayList<RelativePath>{
val i=position+1
        var remainder=list.lastIndex-i
        while(true){
list.removeAt(i)
            if(remainder==0){
                break
            }

remainder--
        }

return list
    }


    fun onFragmentBackPressed(){
        relativePathList.removeAt(relativePathList.lastIndex)
        rv_relativePath.adapter!!.notifyItemRemoved(relativePathList.lastIndex+1)


        val appFileList=relativePathList[relativePathList.lastIndex].appFileList



filesAdapter=FilesRecyclerAdapter(mContext!!,appFileList)
        filesAdapter!!.setOnClickListener(mFileOnClickListener!!)
rv_files.adapter=filesAdapter
    }


}