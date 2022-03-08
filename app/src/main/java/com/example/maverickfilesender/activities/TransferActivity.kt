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
import com.example.maverickfilesender.model.FileMetaData
import com.example.maverickfilesender.viewmodels.TransferViewModel
import kotlinx.android.synthetic.main.activity_transfer.*
import kotlinx.android.synthetic.main.fragment_history.*
import java.io.File
import java.text.DecimalFormat

class TransferActivity : AppCompatActivity() {
    var adapter:QueueFileRecyclerAdapter?=null

var transferViewModel:TransferViewModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MaverickFileSender)
        setContentView(R.layout.activity_transfer)




         transferViewModel=ViewModelProvider(this).get(TransferViewModel::class.java)
        transferViewModel!!.mutableLiveData!!.observe(this,{
     tv_item_incomingFile_currentSize?.text = deriveUnits(it)
           pb_incoming_file.progress = it
        })
        transferViewModel!!.isFileTransferring!!.observe(this,{

            if(it==true){

                vf_transfer_state.displayedChild=1

//             ll_transfer_stack?.visibility=View.VISIBLE
//                tv_fileTransfer_status?.visibility=View.INVISIBLE

            }
            else{

                vf_transfer_state.displayedChild=0
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
            rv_queue.layoutManager=LinearLayoutManager(this)
        rv_queue.adapter=adapter


        imv_transfer_back.setOnClickListener {

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