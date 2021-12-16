package com.example.maverickfilesender.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maverickfilesender.R
import com.example.maverickfilesender.model.Image
import com.example.maverickfilesender.model.Video
import kotlinx.android.synthetic.main.item_video.view.*
import java.io.File
import java.text.DecimalFormat

class VideoFileRecyclerAdapter (val context: Context, val videoList:ArrayList<Video>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
return VideoViewHolder(LayoutInflater.from(context).inflate(R.layout.item_video,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is VideoViewHolder) {

holder.itemView.tv_ItemVideoName.isSelected=true

            Glide.with(context)

                    .load(File(videoList[position].path))
                    .centerCrop()
                    .into(holder.itemView.imv_itemVideo)

            holder.itemView.tv_ItemVideoName.text=videoList[position].name
            holder.itemView.tv_itemVideo_duration.text=videoList[position].durationStr
            val sizeKb= (videoList[position].size/(1024))
            var sizeMb=sizeKb.toFloat()/1024f
            var sizeGb=sizeMb.toFloat()/1024f

            sizeMb=roundToTwoDecimals(sizeMb)
            sizeGb=roundToTwoDecimals(sizeGb)

            String.format("%.2f",sizeMb)
            if(sizeKb<1000){
                holder.itemView.tv_ItemVideoSize.text=sizeKb.toString()+"KB"
            }

            else if(sizeKb>1000 && sizeMb<1000){
                holder.itemView.tv_ItemVideoSize.text.toString()+"MB"

            }

            else if(sizeMb>1000){
                holder.itemView.tv_ItemVideoSize.text=sizeGb.toString()+"GB"
            }



        }
        }



    override fun getItemCount(): Int {
       return videoList.size
    }


    fun roundToTwoDecimals(value:Float):Float{
        val df= DecimalFormat("#.##")



        return df.format(value).toFloat()
    }






    class VideoViewHolder(view: View):RecyclerView.ViewHolder(view)

}