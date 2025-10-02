package com.example.maverickfilesender.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.maverickfilesender.R
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.databinding.ItemImageBinding
import com.example.maverickfilesender.model.Image
import com.example.maverickfilesender.model.ParseFile
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.ObjectOutputStream
import java.lang.Exception
import java.nio.ByteBuffer
import java.util.*
import kotlin.collections.ArrayList

class ImageFileRecyclerAdapter(val context: Context, val imageList: ArrayList<Image>) : RecyclerView.Adapter<ImageFileRecyclerAdapter.ImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Glide.with(context)
            .load(imageList[position].uri)
            .centerCrop()
            .into(holder.binding.imvItemImage)

        if (imageList[position].onSelect) {
            Constants.imagesSelected.add(position)
            holder.binding.imvImgSelect.visibility = android.view.View.VISIBLE
        } else {
            Constants.imagesSelected.remove(position)
            holder.binding.imvImgSelect.visibility = android.view.View.GONE
        }

        holder.binding.imvItemImage.setOnClickListener {
            imageList[position].onSelect = !imageList[position].onSelect

            if (imageList[position].onSelect) {
                var bitmap:Bitmap?=getBitmapFromDrawable(holder.binding.imvItemImage.drawable)

//                   if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
//                       val source= ImageDecoder.createSource(context.contentResolver,imageList[position].uri)
//                        bitmap=ImageDecoder.decodeBitmap(source)
//                    } else {
//                        bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageList[position].uri)
//                    }



                    val stream=ByteArrayOutputStream()

                    bitmap!!.compress(Bitmap.CompressFormat.JPEG,100,stream)
                    val data=stream.toByteArray()

                    imageList[position].data=data



                    Constants.tempSelectedFiles.add(ParseFile(File(imageList[position].path),imageList[position].data,imageList[position].path,null))


                    Constants.sendCount++
                    if(Constants.clientThread==null) {
                        (context as MainActivity).showSendPanel()
                    }

                } else{


                   val did= Constants.tempSelectedFiles.remove(ParseFile(File(imageList[position].path),imageList[position].data,imageList[position].path,null))
                    Constants.sendCount--

                    if (Constants.tempSelectedFiles.isEmpty()) {
                        (context as MainActivity).hideSendPanel()
                    }
                }

                notifyItemChanged(position)
            }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }



    fun getBitmapFromDrawable(drawable: Drawable):Bitmap{
        val bitmap=Bitmap.createBitmap(drawable.intrinsicWidth,drawable.intrinsicHeight,Bitmap.Config.ARGB_8888)
        val canvas= Canvas(bitmap)
        drawable.setBounds(0,0,canvas.width,canvas.height)
        drawable.draw(canvas)
        return bitmap

    }

    class ImageViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)
}