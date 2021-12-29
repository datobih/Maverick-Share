package com.example.maverickfilesender.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.maverickfilesender.R
import com.example.maverickfilesender.constants.Constants
import kotlinx.android.synthetic.main.activity_transfer.*
import java.io.File
import java.text.DecimalFormat

class TransferActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer)

        Constants.transferActivity=this



//val connectionType=intent.getStringExtra(Constants.TRANSFER_EXTRA)
//
//        if(connectionType==Constants.CONNECTION_TYPE_WIFI){
//            while(true){
//
//                if(Constants.clientThread!!.fileTotalSize==-1){
//                    continue
//                }
//                val sizeKb=Constants.clientThread!!.fileTotalSize/1024
//                var sizeMb=sizeKb.toFloat()/1024f
//                var sizeGb=sizeMb.toFloat()/1024f
//
//                sizeMb=roundToTwoDecimals(sizeMb)
//                sizeGb=roundToTwoDecimals(sizeGb)
//
//                String.format("%.2f",sizeMb)
//                if(sizeKb<1000){
//                    tv_item_incomingFile_totalSize.text="/${sizeKb}KB "
//                }
//
//                else if(sizeKb>1000 && sizeMb<1000){
//                    tv_item_incomingFile_totalSize.text="/${sizeMb}MB "
//
//                }
//
//                else if(sizeMb>1000){
//                    tv_item_incomingFile_totalSize.text="/${sizeGb}GB "
//                }
//
//
//
//
//
//                while ((Constants.clientThread!!.bytesReceived)+1 <Constants.clientThread!!.fileTotalSize){
//if(Constants.clientThread!!.bytesReceived != -1){
//val currentPos=Constants!!.clientThread!!.bytesReceived
//tv_incomingFile_name.text=Constants!!.clientThread!!.fileName
//
//    pb_incoming_file.progress=((currentPos/Constants.clientThread!!.fileTotalSize)*100).toInt()
//
//
//    var progSizeKb=Constants.clientThread!!.fileTotalSize/1024
//    var progSizeMb=sizeKb.toFloat()/1024f
//    var progSizeGb=sizeMb.toFloat()/1024f
//
//    progSizeMb=roundToTwoDecimals(sizeMb)
//    progSizeGb=roundToTwoDecimals(sizeGb)
//
//    String.format("%.2f",sizeMb)
//    if(sizeKb<1000){
//        tv_item_incomingFile_totalSize.text="/${progSizeKb}KB "
//    }
//
//    else if(sizeKb>1000 && sizeMb<1000){
//        tv_item_incomingFile_totalSize.text="/${progSizeMb}MB "
//
//    }
//
//    else if(sizeMb>1000){
//        tv_item_incomingFile_totalSize.text="/${progSizeGb}GB "
//    }
//
//
//
//}
//
//
//                }
//
//            }
//        }
//
//        if(connectionType==Constants.CONNECTION_TYPE_HOTSPOT){
//
//        }
//
//imv_transfer_back.setOnClickListener {
//    onBackPressed()
//
//}


    }

    fun roundToTwoDecimals(value:Float):Float{
        val df= DecimalFormat("#.##")



        return df.format(value).toFloat()
    }


    override fun onDestroy() {

    Constants.transferActivity=null
        super.onDestroy()


    }

}