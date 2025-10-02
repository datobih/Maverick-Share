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
import com.example.maverickfilesender.databinding.FragmentFilesDirectoryBinding
import com.example.maverickfilesender.listeners.FileOnClickListener
import com.example.maverickfilesender.listeners.RelativePathOnClickListener
import com.example.maverickfilesender.model.AppFile
import com.example.maverickfilesender.model.ParseFile
import com.example.maverickfilesender.model.RelativePath
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files


class FilesDirectoryFragment : Fragment() {

    private var _binding: FragmentFilesDirectoryBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentFilesDirectoryBinding.inflate(inflater, container, false)
        return binding.root
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




binding.rvRelativePath.layoutManager=LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)


        mRelativePathOnClickListener=object :RelativePathOnClickListener{

            override fun onClick(appFileList:ArrayList<AppFile>,position:Int) {
var list=((binding.rvRelativePath.adapter) as RelativePathRecyclerAdapter).relativePathList

if(position!=(list.lastIndex)){

list=removeElementsFrom(list,position)
   binding.rvRelativePath.adapter!!.notifyDataSetChanged()

    filesAdapter= FilesRecyclerAdapter(mContext!!,appFileList)
    filesAdapter!!.setOnClickListener(mFileOnClickListener!!)
binding.rvFiles.adapter=filesAdapter


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
                Constants.heirarchyFiles[Constants.heirarchyFiles.lastIndex][i].data,"",
               null))
        i++
    }


   else{
       i=0
       Constants.heirarchyFiles.removeAt(Constants.heirarchyFiles.lastIndex)
   }

    if((Constants.heirarchyFiles.isEmpty())||(Constants.heirarchyFiles.lastIndex==position-1)){
        if(Constants.tempSelectedFiles.isEmpty()){
            (mContext as MainActivity).hideSendPanel()
        }
        break
    }

}

}


            }
        }
        relativePathAdapter.setOnClickListener(mRelativePathOnClickListener!!)

binding.rvRelativePath.adapter=relativePathAdapter


         filesAdapter=FilesRecyclerAdapter(mContext!!,relativePathList[0].appFileList)



         mFileOnClickListener=object : FileOnClickListener{
            override fun onClick(file: File) {


val appFileList=generateAppFile(file.listFiles())
                relativePathList.add(RelativePath(file.name,file,appFileList))
Constants.mRelativePath=relativePathList


                relativePathAdapter=RelativePathRecyclerAdapter(mContext!!,relativePathList)


relativePathAdapter.setOnClickListener(mRelativePathOnClickListener!!)

                binding.rvRelativePath.adapter=relativePathAdapter


                val mFiles=file.listFiles()



                filesAdapter=FilesRecyclerAdapter(mContext!!,appFileList)
                filesAdapter!!.setOnClickListener(this)


Constants.countList.add(0)
                Constants.heirarchyFiles.add(ArrayList<ParseFile>())
                binding.rvFiles.adapter=filesAdapter
            }


        }


        filesAdapter!!.setOnClickListener(mFileOnClickListener!!)

        binding.rvFiles.layoutManager=LinearLayoutManager(mContext)
        binding.rvFiles.adapter=filesAdapter



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
var icon:Drawable?=null
try {
    icon= packageInfo?.applicationInfo?.loadIcon(packageManager)
}
catch (e:Exception){
    continue
}
                appFiles.add(AppFile(i,icon,false,null))

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

        binding.rvRelativePath.adapter!!.notifyItemRemoved(relativePathList.lastIndex+1)


        val appFileList=relativePathList[relativePathList.lastIndex].appFileList



        Constants.sendCount=Constants.sendCount-Constants.countList[Constants.countList.lastIndex]
        Constants.countList.removeAt(Constants.countList.lastIndex)


        //IMPORTANT
        var i=0
        val exitIndex=Constants.heirarchyFiles.lastIndex-1
        while(true){
            if(i!=Constants.heirarchyFiles[Constants.heirarchyFiles.lastIndex].size) {
               val did= Constants.tempSelectedFiles.remove(ParseFile(Constants.heirarchyFiles[Constants.heirarchyFiles.lastIndex][i].file,
                        Constants.heirarchyFiles[Constants.heirarchyFiles.lastIndex][i].data,
                        "", null
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


        if(Constants.tempSelectedFiles.isEmpty() && (context as MainActivity).isSendPanelVisible()){
            (context as MainActivity).hideSendPanel()
        }
filesAdapter=FilesRecyclerAdapter(mContext!!,appFileList)
        filesAdapter!!.setOnClickListener(mFileOnClickListener!!)
binding.rvFiles.adapter=filesAdapter

    }


   fun onFinalStack(context: Context){
var i=0
       while(true){
           if(Constants.heirarchyFiles.isNotEmpty()) {
               if (i != Constants.heirarchyFiles[Constants.heirarchyFiles.lastIndex].size) {
                  val tes= Constants.tempSelectedFiles.remove(ParseFile(Constants.heirarchyFiles[Constants.heirarchyFiles.lastIndex][i].file,
                           Constants.heirarchyFiles[Constants.heirarchyFiles.lastIndex][i].data,
                                   "",       null))
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


       if(Constants.tempSelectedFiles.isEmpty() && (context as MainActivity).isSendPanelVisible()){

           Constants.sendCount=0
           (context as MainActivity).hideSendPanel()

       }

   }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}