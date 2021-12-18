package com.example.maverickfilesender.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maverickfilesender.R
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.model.Image
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_image.view.*
import java.io.File

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


            if(imageList[position].onSelect){
Constants.imagesSelected.add(position)
                holder.itemView.imv_imgSelect.visibility=View.VISIBLE

            }
            else{
                Constants.imagesSelected.remove(position)
                holder.itemView.imv_imgSelect.visibility=View.GONE
            }


            holder.itemView.imv_itemImage.setOnClickListener {

imageList[position].onSelect=!imageList[position].onSelect

                if(imageList[position].onSelect){
Constants.selectedFiles.add(File(imageList[position].uri.path))
                    Constants.sendCount++
                    if((context as MainActivity).ll_main_send.visibility!=View.VISIBLE) {
                        (context as MainActivity).ll_main_send.visibility = View.VISIBLE
                        (context as MainActivity).ll_main_send.startAnimation( (context as MainActivity).animationMoveUp)
                    }
                }
                else{

                    Constants.selectedFiles.remove(File(imageList[position].uri.path))
                    Constants.sendCount--

                    if(Constants.sendCount==0){
                        (context as MainActivity).ll_main_send.startAnimation((context as MainActivity).transitionDown)
                        (context as MainActivity).ll_main_send.visibility=View.INVISIBLE
                    }
                }

notifyItemChanged(position)
            }

        }
    }

    override fun getItemCount(): Int {
return imageList.size
    }

class ImageViewHolder(view: View):RecyclerView.ViewHolder(view)


}