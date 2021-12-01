package com.example.maverickfilesender.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.maverickfilesender.R
import com.example.maverickfilesender.model.IncomingFile
import kotlinx.android.synthetic.main.item_history_files.view.*

class IncomingFileRecyclerAdapter(val incomingFile: ArrayList<IncomingFile>, val context: Context):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
  return HistoryFileViewHolder(LayoutInflater.from(context).inflate(R.layout.item_history_files,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder.itemView.tv_item_incomingFile_name.text=incomingFile[position].name
        holder.itemView.pb_incoming_file.progress=incomingFile[position].progress!!
        holder.itemView.tv_item_incomingFile_currentSize.text=incomingFile[position].currentSize.toString()
        holder.itemView.tv_item_incomingFile_totalSize.text="/"+incomingFile[position].totalSize.toString()
    }

    override fun getItemCount(): Int {
     return incomingFile.size
    }



    class HistoryFileViewHolder(view:View):RecyclerView.ViewHolder(view)


}