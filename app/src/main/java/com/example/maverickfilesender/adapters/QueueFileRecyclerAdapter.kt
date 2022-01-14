package com.example.maverickfilesender.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.maverickfilesender.R
import com.example.maverickfilesender.model.FileMetaData
import kotlinx.android.synthetic.main.item_queue_files.view.*
import java.io.File
import java.text.DecimalFormat

class QueueFileRecyclerAdapter(val context: Context,val fileMetaDataList:ArrayList<FileMetaData>,val isSender:Boolean) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
return MyQueueViewHolder(LayoutInflater.from(context).inflate(R.layout.item_queue_files,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is MyQueueViewHolder){
        if(fileMetaDataList[position].bitmap!=null){
            holder.itemView.imv_queue_file.setImageBitmap(fileMetaDataList[position].bitmap)
        }
            else{

        }


            if(isSender){
                holder.itemView.tv_item_queueFile_status.text="Sent"
            }
            else{
                holder.itemView.tv_item_queueFile_status.text="Received"
            }


            holder.itemView.tv_item_queueFile_name.text=fileMetaDataList[position].name

            holder.itemView.tv_item_queueFile_totalSize.text=deriveUnits(fileMetaDataList[position].size.toInt())


        }


    }

    override fun getItemCount(): Int {
return fileMetaDataList.size
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

    fun roundToTwoDecimals(value:Float):Float{
        val df= DecimalFormat("#.##")



        return df.format(value).toFloat()
    }



    class MyQueueViewHolder(view: View):RecyclerView.ViewHolder(view)

}
