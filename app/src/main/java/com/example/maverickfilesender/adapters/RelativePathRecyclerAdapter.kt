package com.example.maverickfilesender.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.maverickfilesender.R
import com.example.maverickfilesender.model.RelativePath
import kotlinx.android.synthetic.main.item_relative_path.view.*

class RelativePathRecyclerAdapter(val context: Context,val relativePathList:ArrayList<RelativePath>) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
return RelativePathViewHolder(LayoutInflater.from(context).inflate(R.layout.item_relative_path,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is RelativePathViewHolder) {

            holder.itemView.tv_item_pathName.text = relativePathList[position].name




            if(position==relativePathList.lastIndex){
                holder.itemView.imv_arrow.visibility=View.GONE

            }


        }







    }

    override fun getItemCount(): Int {
       return relativePathList.size
    }


    class RelativePathViewHolder(view: View):RecyclerView.ViewHolder(view)

}