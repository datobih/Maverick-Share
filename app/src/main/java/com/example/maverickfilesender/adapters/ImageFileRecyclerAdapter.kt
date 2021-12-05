package com.example.maverickfilesender.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maverickfilesender.R
import com.example.maverickfilesender.model.Image
import kotlinx.android.synthetic.main.item_image.view.*

class ImageFileRecyclerAdapter(val context: Context,val imageList:ArrayList<Image>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

   val view=LayoutInflater.from(context).inflate(R.layout.item_image,parent,false)


           return ImageViewHolder(view)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ImageViewHolder){

Glide.with(context)
        .load(imageList[position].uri)
        .into(holder.itemView.imv_itemImage)

        }
    }

    override fun getItemCount(): Int {
return imageList.size
    }

class ImageViewHolder(view: View):RecyclerView.ViewHolder(view)


}