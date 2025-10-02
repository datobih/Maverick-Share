package com.example.maverickfilesender.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.maverickfilesender.R
import com.example.maverickfilesender.databinding.ItemQueueFilesBinding
import com.example.maverickfilesender.model.FileMetaData
import java.io.File
import java.text.DecimalFormat

class QueueFileRecyclerAdapter(val context: Context,val fileMetaDataList:ArrayList<FileMetaData>,val isSender:Boolean) :RecyclerView.Adapter<QueueFileRecyclerAdapter.MyQueueViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyQueueViewHolder {
        val binding = ItemQueueFilesBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyQueueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyQueueViewHolder, position: Int) {
        if(fileMetaDataList[position].bitmap!=null){
            holder.binding.imvQueueFile.setImageBitmap(fileMetaDataList[position].bitmap)
        }
            else{

        }


            if(isSender){
                holder.binding.tvItemQueueFileStatus.text="Sending"
            }
            else{
                holder.binding.tvItemQueueFileStatus.text="Received"
            }


            holder.binding.tvItemQueueFileName.text=fileMetaDataList[position].name

            holder.binding.tvItemQueueFileTotalSize.text=deriveUnits(fileMetaDataList[position].size.toInt())


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



    class MyQueueViewHolder(val binding: ItemQueueFilesBinding):RecyclerView.ViewHolder(binding.root)

}
