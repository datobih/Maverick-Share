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
import com.example.maverickfilesender.model.Image
import com.example.maverickfilesender.model.ParseFile
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_images.view.*
import kotlinx.android.synthetic.main.item_app.view.*
import kotlinx.android.synthetic.main.item_image.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.ObjectOutputStream
import java.lang.Exception
import java.nio.ByteBuffer
import java.util.*
import kotlin.collections.ArrayList

class ImageFileRecyclerAdapter(val context: Context, val imageList: ArrayList<Image>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)


        return ImageViewHolder(view)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ImageViewHolder) {



            Glide.with(context)

                    .load(imageList[position].uri)
                    .centerCrop()

                    .into(holder.itemView.imv_itemImage)


            if (imageList[position].onSelect) {
                Constants.imagesSelected.add(position)
                holder.itemView.imv_imgSelect.visibility = View.VISIBLE

            } else {
                Constants.imagesSelected.remove(position)
                holder.itemView.imv_imgSelect.visibility = View.GONE


            }


            holder.itemView.imv_itemImage.setOnClickListener {

                imageList[position].onSelect = !imageList[position].onSelect

                if (imageList[position].onSelect) {
var bitmap:Bitmap?=getBitmapFromDrawable(holder.itemView.imv_itemImage.drawable)

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



                    Constants.tempSelectedFiles.add(ParseFile(File(imageList[position].path),imageList[position].data,imageList[position].path))


                    Constants.sendCount++
                    if ((context as MainActivity).ll_main_send.visibility != View.VISIBLE) {
                        (context as MainActivity).ll_main_send.visibility = View.VISIBLE
                        (context as MainActivity).ll_main_send.startAnimation((context as MainActivity).animationMoveUp)
                    }
                } else {


                   val did= Constants.tempSelectedFiles.remove(ParseFile(File(imageList[position].uri.path),imageList[position].data,imageList[position].path))
                    Constants.sendCount--

                    if (Constants.tempSelectedFiles.isEmpty()) {
                        (context as MainActivity).ll_main_send.startAnimation((context as MainActivity).transitionDown)
                        (context as MainActivity).ll_main_send.visibility = View.INVISIBLE
                    }
                }

                notifyItemChanged(position)
            }

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

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view)


}