package com.example.maverickfilesender.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maverickfilesender.R
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.databinding.ItemMusicBinding
import com.example.maverickfilesender.model.Music
import com.example.maverickfilesender.model.ParseFile
import java.io.ByteArrayOutputStream
import java.io.File

class AudioFileRecyclerAdapter(val context: Context,val audioList:ArrayList<Music>):RecyclerView.Adapter<AudioFileRecyclerAdapter.AudioViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val binding = ItemMusicBinding.inflate(LayoutInflater.from(context), parent, false)
        return AudioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        if(audioList[position].albumArt!=null){
            holder.binding.imvAudioCover.setImageBitmap(audioList[position].albumArt)
        } else {
            holder.binding.imvAudioCover.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.audio_icon))
        }

        holder.binding.tvAudioTitle.text = audioList[position].name
        holder.binding.tvAudioArtist.text = audioList[position].artist

        if (audioList[position].onSelect) {
            Constants.audioSelected.add(position)
            holder.binding.imvItemAudioSelect.visibility = android.view.View.VISIBLE
        } else {
            Constants.audioSelected.remove(position)
            holder.binding.imvItemAudioSelect.visibility = android.view.View.INVISIBLE
        }

        holder.binding.llAudioItem.setOnClickListener {
            audioList[position].onSelect = !audioList[position].onSelect
            if (audioList[position].onSelect) {
                Constants.sendCount++

                val bitmap=getBitmapFromDrawable(holder.binding.imvAudioCover.drawable)
                val stream= ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
                val data=stream.toByteArray()
                audioList[position].data=data

                Constants.tempSelectedFiles.add(ParseFile( File(audioList[position].path),data,"",null))
                if(Constants.clientThread==null) {
                    (context as MainActivity).showSendPanel()
                }
            } else {
                Constants.sendCount--
                Constants.tempSelectedFiles.remove(ParseFile(File(audioList[position].path),audioList[position].data,"",null))

                if (Constants.tempSelectedFiles.isEmpty()) {
                    (context as MainActivity).hideSendPanel()
                }
            }

            notifyItemChanged(position)
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

    class AudioViewHolder(val binding: ItemMusicBinding) : RecyclerView.ViewHolder(binding.root)
}