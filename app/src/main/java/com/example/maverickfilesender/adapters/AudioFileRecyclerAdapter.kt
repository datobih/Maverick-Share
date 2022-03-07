package com.example.maverickfilesender.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maverickfilesender.R
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.model.Music
import com.example.maverickfilesender.model.ParseFile
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.item_image.view.*
import kotlinx.android.synthetic.main.item_music.view.*
import kotlinx.android.synthetic.main.item_video.view.*
import java.io.ByteArrayOutputStream
import java.io.File

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


    if (audioList[position].onSelect) {
        Constants.audioSelected.add(position)



        holder.itemView.imv_itemAudioSelect.visibility = View.VISIBLE

    } else {
        Constants.audioSelected.remove(position)
        holder.itemView.imv_itemAudioSelect.visibility = View.INVISIBLE
    }



    holder.itemView.ll_audioItem.setOnClickListener {


        audioList[position].onSelect = !audioList[position].onSelect
        if (audioList[position].onSelect) {
            Constants.sendCount++

//                    val metaDataReceiver = MediaMetadataRetriever()
//                    metaDataReceiver.setDataSource(videoList[position].path)
//                   val bitmap = metaDataReceiver.frameAtTime



val bitmap=getBitmapFromDrawable(holder.itemView.imv_audioCover.drawable)
            val stream= ByteArrayOutputStream()
           bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            val data=stream.toByteArray()
            audioList[position].data=data

            Constants.tempSelectedFiles.add(ParseFile( File(audioList[position].path),data,"",null))
if(Constants.clientThread==null) {


    if ((context as MainActivity).ll_main_send.visibility != View.VISIBLE) {

        (context as MainActivity).ll_main_send.visibility = View.VISIBLE
        (context as MainActivity).ll_main_send.startAnimation((context as MainActivity).animationMoveUp)


    }

}

        } else {
            Constants.sendCount--
            Constants.tempSelectedFiles.remove(ParseFile(File(audioList[position].path),audioList[position].data,"",null))


            if (Constants.tempSelectedFiles.isEmpty()) {
                (context as MainActivity).ll_main_send.startAnimation((context as MainActivity).transitionDown)
                (context as MainActivity).ll_main_send.visibility = View.GONE
            }

        }

        notifyItemChanged(position)
    }



}
    }



    fun getBitmapFromDrawable(drawable: Drawable):Bitmap{
        val bitmap=Bitmap.createBitmap(drawable.intrinsicWidth,drawable.intrinsicHeight,Bitmap.Config.ARGB_8888)
        val canvas= Canvas(bitmap)
        drawable.setBounds(0,0,canvas.width,canvas.height)
        drawable.draw(canvas)
        return bitmap

    }
    override fun getItemCount(): Int {
        return audioList.size
    }



    class audioViewHolder(view: View):RecyclerView.ViewHolder(view)

}