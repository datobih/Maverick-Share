package com.example.maverickfilesender.activities

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
        setContentView(R.layout.activity_transfer)

        Constants.transferActivity=this
adapter= QueueFileRecyclerAdapter(this,ArrayList<FileMetaData>())
rv_queue.layoutManager=LinearLayoutManager(this)
        rv_queue.adapter=adapter



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