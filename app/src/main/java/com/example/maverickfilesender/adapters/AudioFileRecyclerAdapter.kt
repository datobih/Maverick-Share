package com.example.maverickfilesender.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.maverickfilesender.R
import com.example.maverickfilesender.model.Music
import kotlinx.android.synthetic.main.item_music.view.*

class AudioFileRecyclerAdapter(val context: Context,val audioList:ArrayList<Music>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
return audioViewHolder(LayoutInflater.from(context).inflate(R.layout.item_music,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
if(holder is audioViewHolder ){

    if(audioList[position].albumArt!=null){

        holder.itemView.imv_audioCover.setImageBitmap(audioList[position].albumArt)

    }

    else{
holder.itemView.imv_audioCover.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.audio_icon))
    }


holder.itemView.tv_audioTitle.text=audioList[position].name
    holder.itemView.tv_audioArtist.text=audioList[position].artist



}
    }

    override fun getItemCount(): Int {
        return audioList.size
    }



    class audioViewHolder(view: View):RecyclerView.ViewHolder(view)

}