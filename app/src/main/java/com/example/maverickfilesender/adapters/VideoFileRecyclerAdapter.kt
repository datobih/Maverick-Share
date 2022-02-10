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
import com.example.maverickfilesender.model.Image
import com.example.maverickfilesender.model.ParseFile
import com.example.maverickfilesender.model.Video
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.item_video.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.ObjectOutputStream
import java.text.DecimalFormat

class VideoFileRecyclerAdapter(val context: Context, val videoList: ArrayList<Video>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VideoViewHolder(LayoutInflater.from(context).inflate(R.layout.item_video, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is VideoViewHolder) {

            holder.itemView.tv_ItemVideoName.isSelected = true
            if (videoList[position].path.endsWith(".mp4")) {
                Glide.with(context)

                        .load(File(videoList[position].path))

                        .centerCrop()

                        .into(holder.itemView.imv_itemVideo)

            } else {
                holder.itemView.imv_itemVideo.setImageResource(R.drawable.video_placeholder_bg)


                val bitmap = getBitmapFromDrawable(ContextCompat.getDrawable(context,R.drawable.video_placeholder_bg)!!)
                val stream=ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
                val data=stream.toByteArray()
                videoList[position].data=data



            }

            holder.itemView.tv_ItemVideoName.text = videoList[position].name
            holder.itemView.tv_itemVideo_duration.text = videoList[position].durationStr
            val sizeKb = videoList[position].size/ (1024)
            var sizeMb = sizeKb.toFloat() / 1024f
            var sizeGb = sizeMb.toFloat() / 1024f

            sizeMb = roundToTwoDecimals(sizeMb)
            sizeGb = roundToTwoDecimals(sizeGb)

            String.format("%.2f", sizeMb)
            if (sizeKb < 1000) {
                holder.itemView.tv_ItemVideoSize.text = sizeKb.toString() + "KB"
            } else if (sizeKb > 1000 && sizeMb < 1000) {
                holder.itemView.tv_ItemVideoSize.text=sizeMb.toString() + "MB"

            } else if (sizeMb > 1000) {
                holder.itemView.tv_ItemVideoSize.text = sizeGb.toString() + "GB"
            }

            if (videoList[position].onSelect) {
                Constants.videosSelected.add(position)



                holder.itemView.imv_itemVideoSelect.visibility = View.VISIBLE

            } else {
                Constants.videosSelected.remove(position)
                holder.itemView.imv_itemVideoSelect.visibility = View.INVISIBLE
            }



            holder.itemView.ll_itemVideo.setOnClickListener {


                videoList[position].onSelect = !videoList[position].onSelect
                if (videoList[position].onSelect) {
                    Constants.sendCount++

//                    val metaDataReceiver = MediaMetadataRetriever()
//                    metaDataReceiver.setDataSource(videoList[position].path)
//                   val bitmap = metaDataReceiver.frameAtTime

                    val bitmap=getBitmapFromDrawable(holder.itemView.imv_itemVideo.drawable)
                    val stream=ByteArrayOutputStream()
                    bitmap!!.compress(Bitmap.CompressFormat.PNG,100,stream)
                    val data=stream.toByteArray()
                    videoList[position].data=data

                    Constants.tempSelectedFiles.add(ParseFile( File(videoList[position].path),data,"",null))

                    if ((context as MainActivity).ll_main_send.visibility != View.VISIBLE) {

                        (context as MainActivity).ll_main_send.visibility = View.VISIBLE
                        (context as MainActivity).ll_main_send.startAnimation((context as MainActivity).animationMoveUp)


                    }

                } else {
                    Constants.sendCount--
                    Constants.tempSelectedFiles.remove(ParseFile(File(videoList[position].path),videoList[position].data,"",null))


                    if (Constants.tempSelectedFiles.isEmpty()) {
                        (context as MainActivity).ll_main_send.startAnimation((context as MainActivity).transitionDown)
                        (context as MainActivity).ll_main_send.visibility = View.INVISIBLE
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
        return videoList.size
    }


    fun roundToTwoDecimals(value: Float): Float {
        val df = DecimalFormat("#.##")



        return df.format(value).toFloat()
    }


    class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view)

}