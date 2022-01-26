package com.example.maverickfilesender.fragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maverickfilesender.R
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.adapters.FilesRecyclerAdapter
import com.example.maverickfilesender.adapters.RelativePathRecyclerAdapter
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.listeners.FileOnClickListener
import com.example.maverickfilesender.listeners.RelativePathOnClickListener
import com.example.maverickfilesender.model.AppFile
import com.example.maverickfilesender.model.ParseFile
import com.example.maverickfilesender.model.RelativePath
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_files_directory.*
import kotlinx.android.synthetic.main.fragment_files_directory.view.*
import kotlinx.android.synthetic.main.fragment_videos.*
import kotlinx.android.synthetic.main.item_file.view.*
import java.io.ByteArrayOutputStream
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







        relativePathList.add(RelativePath("Home",file,generateAppFile(files)))
Constants.mRelativePath=relativePathList
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


while(true){
        if(Constants.countList.lastIndex>=position){
        Constants.sendCount=Constants.sendCount-Constants.countList[Constants.countList.lastIndex]
        Constants.countList.removeAt(Constants.countList.lastIndex)


        }

        else{
            break
        }
    }

    //IMPORTANT
    var i=0
while(true){
    if(i!=Constants.heirarchyFiles[Constants.heirarchyFiles.lastIndex].size) {

        val tes=Constants.tempSelectedFiles.remove(ParseFile( Constants.heirarchyFiles[Constants.heirarchyFiles.lastIndex][i].file,
                Constants.heirarchyFiles[Constants.heirarchyFiles.lastIndex][i].data,""))
        i++
    }


   else{
       i=0
       Constants.heirarchyFiles.removeAt(Constants.heirarchyFiles.lastIndex)
   }

    if((Constants.heirarchyFiles.isEmpty())||(Constants.heirarchyFiles.lastIndex==position-1)){
        if(Constants.tempSelectedFiles.isEmpty()){
            (mContext as MainActivity).ll_main_send.startAnimation((mContext as MainActivity).transitionDown)
            (mContext as MainActivity).ll_main_send.visibility=View.INVISIBLE
        }
        break
    }

}

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


Constants.countList.add(0)
                Constants.heirarchyFiles.add(ArrayList<ParseFile>())
                view.rv_files.adapter=filesAdapter
            }


        }


        filesAdapter!!.setOnClickListener(mFileOnClickListener!!)

        view.rv_files.layoutManager=LinearLayoutManager(mContext)
        view.rv_files.adapter=filesAdapter



    }

    fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
        val bitmap= Bitmap.createBitmap(drawable.intrinsicWidth,drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas= Canvas(bitmap)
        drawable.setBounds(0,0,canvas.width,canvas.height)
        drawable.draw(canvas)
        return bitmap

    }

    fun generateAppFile(fileList:Array<File>): ArrayList<AppFile>{

        val appFiles=ArrayList<AppFile>()

        for (i in fileList){
            if( i.name.endsWith(".apk")){
                val packageManager=mContext!!.packageManager
                val packageInfo=packageManager.getPackageArchiveInfo(i.path,0)


                appFiles.add(AppFile(i,packageInfo!!.applicationInfo.loadIcon(packageManager),false,null))

            }
            else{
                appFiles.add(AppFile(i,onSelect = false,data = null))
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


    fun onFragmentBackPressed(context: Context){
        relativePathList.removeAt(relativePathList.lastIndex)
        Constants.mRelativePath=relativePathList

        rv_relativePath.adapter!!.notifyItemRemoved(relativePathList.lastIndex+1)


        val appFileList=relativePathList[relativePathList.lastIndex].appFileList



        Constants.sendCount=Constants.sendCount-Constants.countList[Constants.countList.lastIndex]
        Constants.countList.removeAt(Constants.countList.lastIndex)


        //IMPORTANT
        var i=0
        val exitIndex=Constants.heirarchyFiles.lastIndex-1
        while(true){
            if(i!=Constants.heirarchyFiles[Constants.heirarchyFiles.lastIndex].size) {
                Constants.tempSelectedFiles.remove(ParseFile(Constants.heirarchyFiles[Constants.heirarchyFiles.lastIndex][i].file,
                        Constants.heirarchyFiles[Constants.heirarchyFiles.lastIndex][i].data,
                        ""
                        ))
                i++
            }


            else{
                i=0
                Constants.heirarchyFiles.removeAt(Constants.heirarchyFiles.lastIndex)
            }

            if(Constants.heirarchyFiles.lastIndex==exitIndex){
                break
            }

        }


        if(Constants.tempSelectedFiles.isEmpty() && (context as MainActivity).ll_main_send.visibility==View.VISIBLE){

            (context as MainActivity).ll_main_send.startAnimation((context as MainActivity).transitionDown)
            (context as MainActivity).ll_main_send.visibility=View.INVISIBLE
        }
filesAdapter=FilesRecyclerAdapter(mContext!!,appFileList)
        filesAdapter!!.setOnClickListener(mFileOnClickListener!!)
rv_files.adapter=filesAdapter

    }


   fun onFinalStack(context: Context){
var i=0
       while(true){
           if(Constants.heirarchyFiles.isNotEmpty()) {
               if (i != Constants.heirarchyFiles[Constants.heirarchyFiles.lastIndex].size) {
                  val tes= Constants.tempSelectedFiles.remove(ParseFile(Constants.heirarchyFiles[Constants.heirarchyFiles.lastIndex][i].file,
                           Constants.heirarchyFiles[Constants.heirarchyFiles.lastIndex][i].data,
                                   ""))
                   i++
               } else {
                   i = 0
                   Constants.heirarchyFiles.removeAt(Constants.heirarchyFiles.lastIndex)
               }

               if (Constants.heirarchyFiles.isEmpty()) {
                   break
               }
           }

else{
    break
           }

       }

       while(Constants.parentFiles.isNotEmpty()){


           Constants.tempSelectedFiles.remove(Constants.parentFiles[Constants.parentFiles.lastIndex])
Constants.parentFiles.removeAt(Constants.parentFiles.lastIndex)
       }


       if(Constants.tempSelectedFiles.isEmpty() && (context as MainActivity).ll_main_send.visibility==View.VISIBLE){

           Constants.sendCount=0

           (context as MainActivity).ll_main_send.startAnimation((context as MainActivity).transitionDown)
           (context as MainActivity).ll_main_send.visibility=View.INVISIBLE

       }

   }

}