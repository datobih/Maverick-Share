package com.example.maverickfilesender.activities

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maverickfilesender.R
import com.example.maverickfilesender.adapters.QueueFileRecyclerAdapter
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.model.FileMetaData
import kotlinx.android.synthetic.main.activity_transfer.*
import kotlinx.android.synthetic.main.fragment_history.*
import java.io.File
import java.text.DecimalFormat

class TransferActivity : AppCompatActivity() {
    var adapter:QueueFileRecyclerAdapter?=null



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MaverickFileSender)
        setContentView(R.layout.activity_transfer)

        var isSender:Boolean = Constants.clientThread == null
        if(isSender==true){

            adapter= QueueFileRecyclerAdapter(this,Constants.onSelectedMetaData,isSender)
        }
        else {
            adapter = QueueFileRecyclerAdapter(this, ArrayList<FileMetaData>(), isSender)
        }
            rv_queue.layoutManager=LinearLayoutManager(this)
        rv_queue.adapter=adapter


        Constants.transferActivity= this


    }

    fun roundToTwoDecimals(value:Float):Float{
        val df= DecimalFormat("#.##")



        return df.format(value).toFloat()
    }





    override fun onBackPressed() {

        Constants.transferActivity=null
        super.onBackPressed()
    }

}