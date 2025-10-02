package com.example.maverickfilesender.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.maverickfilesender.R
import com.example.maverickfilesender.databinding.ItemRelativePathBinding
import com.example.maverickfilesender.listeners.RelativePathOnClickListener
import com.example.maverickfilesender.model.RelativePath

class RelativePathRecyclerAdapter(val context: Context,val relativePathList:ArrayList<RelativePath>) :RecyclerView.Adapter<RelativePathRecyclerAdapter.RelativePathViewHolder>(){

var mRelativePathOnClickListener:RelativePathOnClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelativePathViewHolder {
        val binding = ItemRelativePathBinding.inflate(LayoutInflater.from(context), parent, false)
        return RelativePathViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RelativePathViewHolder, position: Int) {

            holder.binding.tvItemPathName.text = relativePathList[position].name




            if(position==relativePathList.lastIndex){
                holder.binding.imvArrow.visibility=View.GONE

            }

holder.binding.llRelativePath.setOnClickListener {

    mRelativePathOnClickListener!!.onClick(relativePathList[position].appFileList,position)

}

    }

    fun setOnClickListener(mRelativePathOnClickListener: RelativePathOnClickListener){

        this.mRelativePathOnClickListener=mRelativePathOnClickListener

    }

    override fun getItemCount(): Int {
       return relativePathList.size
    }


    class RelativePathViewHolder(val binding: ItemRelativePathBinding):RecyclerView.ViewHolder(binding.root)

}