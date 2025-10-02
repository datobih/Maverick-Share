package com.example.maverickfilesender.activities

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maverickfilesender.R
import com.example.maverickfilesender.adapters.QueueFileRecyclerAdapter
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.databinding.ActivityTransferBinding
import com.example.maverickfilesender.model.FileMetaData
import com.example.maverickfilesender.viewmodels.TransferViewModel
import java.io.File
import java.text.DecimalFormat

class TransferActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransferBinding
    
    // Expose binding for handler classes
    fun getBinding() = binding
    
    var adapter:QueueFileRecyclerAdapter?=null

var transferViewModel:TransferViewModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MaverickFileSender)
        binding = ActivityTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)




         transferViewModel=ViewModelProvider(this).get(TransferViewModel::class.java)
        transferViewModel!!.mutableLiveData!!.observe(this,{
     binding.tvItemIncomingFileCurrentSize?.text = deriveUnits(it)
           binding.pbIncomingFile.progress = it
        })
        transferViewModel!!.isFileTransferring!!.observe(this,{

            if(it==true){

                binding.vfTransferState.displayedChild=1

//             ll_transfer_stack?.visibility=View.VISIBLE
//                tv_fileTransfer_status?.visibility=View.INVISIBLE

            }
            else{

                binding.vfTransferState.displayedChild=0
//                ll_transfer_stack?.visibility= View.INVISIBLE
//
//              tv_fileTransfer_status?.visibility= View.VISIBLE
//             tv_fileTransfer_status?.text="No Files Incoming"


            }

        })



        var isSender:Boolean = Constants.clientThread == null
        if(isSender){

            adapter= QueueFileRecyclerAdapter(this,Constants.onSelectedMetaData,isSender)
        }
        else {
            adapter = QueueFileRecyclerAdapter(this,Constants.onSelectedMetaData, isSender)
        }
            binding.rvQueue.layoutManager=LinearLayoutManager(this)
        binding.rvQueue.adapter=adapter


        binding.imvTransferBack.setOnClickListener {

            onBackPressed()

        }

        Constants.transferActivity= this


    }

    fun roundToTwoDecimals(value:Float):Float{
        val df= DecimalFormat("#.##")



        return df.format(value).toFloat()
    }

    fun deriveUnits(bytes:Int):String{
        var units=""
        var sizeKb=bytes.toFloat()/1024f
        var sizeMb=sizeKb.toFloat()/1024f
        var sizeGb=sizeMb.toFloat()/1024f

        sizeMb=roundToTwoDecimals(sizeMb)
        sizeGb=roundToTwoDecimals(sizeGb)

        String.format("%.2f",sizeMb)
        if(sizeKb<1000){
            units="${sizeKb}KB"
        }

        else if(sizeKb>1000 && sizeMb<1000){
            units="${sizeMb}MB"

        }

        else if(sizeMb>1000){
            units="${sizeGb}GB"
        }

        return units
    }



    override fun onBackPressed() {

        Constants.transferActivity=null
        finish()

    }

}