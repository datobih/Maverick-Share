package com.example.maverickfilesender.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.maverickfilesender.R
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.databinding.ItemVideoBinding
import com.example.maverickfilesender.model.Image
import com.example.maverickfilesender.model.ParseFile
import com.example.maverickfilesender.model.Video
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.ObjectOutputStream
import java.text.DecimalFormat

class VideoFileRecyclerAdapter(val context: Context, val videoList: ArrayList<Video>) : RecyclerView.Adapter<VideoFileRecyclerAdapter.VideoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.binding.tvItemVideoName.isSelected = true
        if (videoList[position].path.endsWith(".mp4")) {
            Glide.with(context)
                .load(File(videoList[position].path))
                .centerCrop()
                .into(holder.binding.imvItemVideo)
        } else {
            holder.binding.imvItemVideo.setImageResource(R.drawable.video_placeholder_bg)
            val bitmap = getBitmapFromDrawable(ContextCompat.getDrawable(context,R.drawable.video_placeholder_bg)!!)
            val stream=ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            val data=stream.toByteArray()
            videoList[position].data=data
        }

        holder.binding.tvItemVideoName.text = videoList[position].name
        holder.binding.tvItemVideoDuration.text = videoList[position].durationStr
        val sizeKb = videoList[position].size/ (1024)
        var sizeMb = sizeKb.toFloat() / 1024f
        var sizeGb = sizeMb.toFloat() / 1024f

        sizeMb = roundToTwoDecimals(sizeMb)
        sizeGb = roundToTwoDecimals(sizeGb)

        String.format("%.2f", sizeMb)
        if (sizeKb < 1000) {
            holder.binding.tvItemVideoSize.text = sizeKb.toString() + "KB"
        } else if (sizeKb > 1000 && sizeMb < 1000) {
            holder.binding.tvItemVideoSize.text=sizeMb.toString() + "MB"
        } else if (sizeMb > 1000) {
            holder.binding.tvItemVideoSize.text = sizeGb.toString() + "GB"
        }

        if (videoList[position].onSelect) {
            Constants.videosSelected.add(position)
            holder.binding.imvItemVideoSelect.visibility = android.view.View.VISIBLE
        } else {
            Constants.videosSelected.remove(position)
            holder.binding.imvItemVideoSelect.visibility = android.view.View.INVISIBLE
        }

        holder.binding.llItemVideo.setOnClickListener {
            videoList[position].onSelect = !videoList[position].onSelect
                if (videoList[position].onSelect) {
                    Constants.sendCount++

                    val bitmap=getBitmapFromDrawable(holder.binding.imvItemVideo.drawable)
                    val stream=ByteArrayOutputStream()
                    bitmap!!.compress(Bitmap.CompressFormat.PNG,100,stream)
                    val data=stream.toByteArray()
                    videoList[position].data=data

                    Constants.tempSelectedFiles.add(ParseFile( File(videoList[position].path),data,"",null))

                    if(Constants.clientThread==null) {
                        (context as MainActivity).showSendPanel()
                    }

                } else {
                    Constants.sendCount--
                    Constants.tempSelectedFiles.remove(ParseFile(File(videoList[position].path),videoList[position].data,"",null))


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
        return videoList.size
    }


    fun roundToTwoDecimals(value: Float): Float {
        val df = DecimalFormat("#.##")



        return df.format(value).toFloat()
    }

    class VideoViewHolder(val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root)
}