package com.example.maverickfilesender.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.maverickfilesender.R
import com.example.maverickfilesender.constants.Constants
import kotlinx.android.synthetic.main.fragment_holder_files.view.*
import kotlinx.android.synthetic.main.fragment_storage_directory.view.*
import java.io.File
import java.text.DecimalFormat


class StorageDirectoryFragment : Fragment() {

    var mContext: Context?=null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_storage_directory, container, false)
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





        var files= ContextCompat.getExternalFilesDirs(mContext!!,null)

        var sdPath:String=""
        var sdTotalSize=0f
        var sdAvailableSpace=0f
        var sdUsedSpace=0f
        if(files.size>1){
            sdPath= getSDirectory(files[1].path)



        }

        val inStoragePath="/storage/emulated/0"

        var internalTotalSize=getTotalSpace(inStoragePath)



        var internalAvailableSpace=getAvailableSpace(inStoragePath)
        var internalUsedSpace=internalTotalSize-internalAvailableSpace

        if(internalTotalSize>=1000) {
            val internalTotalSizeTB =internalTotalSize/ 1024

            view.tv_intenalUsed.text = if(internalUsedSpace<1000) roundToTwoDecimals(internalUsedSpace).toString() + "GB"
            else  roundToTwoDecimals(internalUsedSpace/1024f).toString() + "TB"
            view.tv_internalTotalSize.text = roundToTwoDecimals(internalTotalSizeTB).toString() + "TB"

        }else{
            view.tv_intenalUsed.text=roundToTwoDecimals(internalUsedSpace).toString()+"GB"
            view.tv_internalTotalSize.text=roundToTwoDecimals(internalTotalSize).toString()+"GB"
        }


        view.progress_internalStorage.progress=((internalUsedSpace/internalTotalSize)*100).toInt()


        if(sdPath.isNotEmpty()){
            view.ll_SD_storage.visibility=View.VISIBLE
            sdTotalSize=getTotalSpace(sdPath)
            sdAvailableSpace=getAvailableSpace(sdPath)
            sdUsedSpace=sdTotalSize-sdAvailableSpace

            if(sdTotalSize>=1000){
                val sdTotalSizeTB=sdTotalSize/1024
                view.tv_sdUsed.text= if(sdUsedSpace<1000) roundToTwoDecimals(sdUsedSpace).toString()+"GB"
                else roundToTwoDecimals(sdUsedSpace/1024f).toString()+"TB"

                view.tv_SD_totalSize.text=roundToTwoDecimals(sdTotalSizeTB).toString()+"TB"

            }

            else{
                view.tv_sdUsed.text=roundToTwoDecimals(sdUsedSpace).toString()+"GB"
                view.tv_SD_totalSize.text=roundToTwoDecimals(sdTotalSize).toString()+"GB"
            }



            view.progress_SD.progress=((sdUsedSpace/sdTotalSize)*100).toInt()

        }


        view.ll_internalStorage.setOnClickListener {

            val bundle=Bundle()

            bundle.putString(Constants.BUNDLE_STORAGE_DIRECTORY,inStoragePath)


            val directoryFragment=FilesDirectoryFragment()
            directoryFragment.arguments=bundle



            requireActivity().supportFragmentManager.beginTransaction().apply {

                addToBackStack(null)
                replace(R.id.holder_files_fragment,directoryFragment)


                commit()

            }


        }

        view.ll_SD_storage.setOnClickListener {

            val bundle=Bundle()

            bundle.putString(Constants.BUNDLE_STORAGE_DIRECTORY,sdPath)


            val directoryFragment=FilesDirectoryFragment()
            directoryFragment.arguments=bundle



            requireActivity().supportFragmentManager.beginTransaction().apply {

                addToBackStack(null)
                replace(R.id.holder_files_fragment,directoryFragment)


                commit()

            }


        }







    }

    fun roundToTwoDecimals(value:Float):Float{
        val df= DecimalFormat("#.##")



        return df.format(value).toFloat()
    }


    fun getSDirectory(path:String):String{
        var sdPath=""
        var count=0

        for(i in path){

            if(i == '/'){

                count++

            }

            if(count==3){
                break
            }
            sdPath += i

        }

        return sdPath
    }


    fun getTotalSpace(path:String):Float{

        val iPath: File = File(path)
        val iStat = StatFs(iPath.path)
        val iBlockSize=iStat.blockSizeLong
        val iTotalBlocks = iStat.blockCountLong
        val iTotalSpace = iTotalBlocks * iBlockSize






        val spaceKB=iTotalSpace.toFloat()/1024f
        val spaceMB=spaceKB/1024f


        return spaceMB/1024f
    }

    fun getAvailableSpace(path:String):Float{

        val iPath: File = File(path)
        val iStat = StatFs(iPath.path)
        val iBlockSize = iStat.blockSizeLong
        val iAvailableBlocks = iStat.availableBlocksLong
        val iAvailableSpace = iAvailableBlocks * iBlockSize


        val spaceKB=iAvailableSpace.toFloat()/1024f
        val spaceMB=spaceKB/1024f


        return spaceMB/1024f
    }




}