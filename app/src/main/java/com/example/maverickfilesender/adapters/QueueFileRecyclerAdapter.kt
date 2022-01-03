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

class QueueFileRecyclerAdapter(val context: Context,val fileMetaDataList:ArrayList<FileMetaData>) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
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

            holder.itemView.tv_item_queueFile_name.text=fileMetaDataList[position].name

            holder.itemView.tv_item_queueFile_totalSize.text=fileMetaDataList[position].size.toString()


        }


    }

    override fun getItemCount(): Int {
return fileMetaDataList.size
    }


    class MyQueueViewHolder(view: View):RecyclerView.ViewHolder(view)

}
