package com.example.maverickfilesender.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
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
import com.example.maverickfilesender.databinding.ItemFileBinding
import com.example.maverickfilesender.listeners.FileOnClickListener
import com.example.maverickfilesender.model.AppFile
import com.example.maverickfilesender.model.ParseFile
import java.io.ByteArrayOutputStream
import java.lang.Exception

class FilesRecyclerAdapter(val context: Context,val appFileList:ArrayList<AppFile>):RecyclerView.Adapter<FilesRecyclerAdapter.FilesViewHolder>() {

    var mFileOnClickListener:FileOnClickListener?=null
val animation=AnimationUtils.loadAnimation(context,R.anim.bounce)
   val animationMoveUp=AnimationUtils.loadAnimation(context,R.anim.transition_up)
    var position=-1



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilesViewHolder {
        val binding = ItemFileBinding.inflate(LayoutInflater.from(context), parent, false)
        return FilesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilesViewHolder, position: Int) {

Constants.filesIconView.add(holder.binding.imvFileIcon)


if(appFileList[position].onSelect!!){

    holder.binding.imbFileOnSelect.background=ContextCompat.getDrawable(context,R.drawable.circle_selected)
    if(this.position!=-1 && position==this.position) {
        holder.binding.imbFileOnSelect.startAnimation(animation)
        this.position=-1
    }
}
    else{
    holder.binding.imbFileOnSelect.background=ContextCompat.getDrawable(context,R.drawable.circle_selector)
    }

    holder.binding.imbFileOnSelect.setOnClickListener {
        val mainContext = context as MainActivity

        if (Constants.clientThread == null) {


            if (!appFileList[position]!!.file.isDirectory) {




            if (!mainContext.isSendPanelVisible()) {
                mainContext.showSendPanel()
            }





            appFileList[position].onSelect = !appFileList[position].onSelect!!

            if (appFileList[position].onSelect!!) {

                var data: ByteArray? = null

//if(appFileList[position].drawable!=null){
                try {
                    val bitmap = getBitmapFromDrawable(holder.binding.imvFileIcon.drawable)
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    data = stream.toByteArray()
                    appFileList[position].data = data
                } catch (e: Exception) {
                    appFileList[position].onSelect = !appFileList[position].onSelect!!
                    data = null
                    mainContext.hideSendPanel()
                }
                if (data != null) {
                    Constants.sendCount++

//}

                    Constants.tempSelectedFiles.add(ParseFile(appFileList[position].file, appFileList[position].data, "", null))

                    if (Constants.countList.isNotEmpty() && Constants.heirarchyFiles.isNotEmpty()) {
                        Constants.countList[Constants.countList.lastIndex] = Constants.countList[Constants.countList.lastIndex] + 1
                        Constants.heirarchyFiles[Constants.heirarchyFiles.lastIndex].add(ParseFile(appFileList[position].file, data, appFileList[position].file.path, null))
                    } else {
                        Constants.parentFiles.add(ParseFile(appFileList[position].file, data, "", null))
                    }
                }
            } else {
                Constants.sendCount--
                Constants.tempSelectedFiles.remove(ParseFile(appFileList[position].file, appFileList[position].data, "", null))

                if (Constants.countList.isNotEmpty() && Constants.heirarchyFiles.isNotEmpty()) {
                    Constants.countList[Constants.countList.lastIndex] = Constants.countList[Constants.countList.lastIndex] - 1
                    Constants.heirarchyFiles[Constants.heirarchyFiles.lastIndex].remove(ParseFile(appFileList[position].file, appFileList[position].data, "", null))
                } else {
                    Constants.parentFiles.remove(ParseFile(appFileList[position].file, appFileList[position].data, "", null))
                }

                if (Constants.tempSelectedFiles.isEmpty()) {
                    (context as MainActivity).hideSendPanel()
                }

            }

            this.notifyItemChanged(position)


        }
            else{
                mainContext.showErrorMessage("Can't send directories.")
            }
    }


    }


    holder.binding.tvItemFileName.text = appFileList[position].file.name
    if(appFileList[position].file.isDirectory) {

    holder.binding.imvFileIcon.setImageResource(R.drawable.folder_icon)

        holder.binding.llItemFile.setOnClickListener {

mFileOnClickListener!!.onClick(appFileList[position].file)



        }

}
 else{

     if(appFileList[position].file.name.endsWith(".PNG")||
             appFileList[position].file.name.endsWith(".jpg")||
             appFileList[position].file.name.endsWith(".mp4")

     ){

         Glide.with(context)
                 .load(appFileList[position].file)
                 .centerCrop()
                 .into(holder.binding.imvFileIcon)





     }

        else if(appFileList[position].file.name.endsWith(".avi")||
             appFileList[position].file.name.endsWith(".mkv")){
         Glide.with(context)
                 .load(R.drawable.video_icon)
                 .centerCrop()
                 .into(holder.binding.imvFileIcon)


     }

        else if(appFileList[position].file.name.endsWith(".pdf")||
             appFileList[position].file.name.endsWith(".PDF")){

            Glide.with(context)
                    .load(R.drawable.pdf_icon)
                    .centerCrop()
                    .into(holder.binding.imvFileIcon)





     }

     else if(appFileList[position].file.name.endsWith(".zip")||
             appFileList[position].file.name.endsWith(".rar")||
             appFileList[position].file.name.endsWith(".xz")){

         Glide.with(context)
                 .load(R.drawable.zip_icon)
                 .centerCrop()
                 .into(holder.binding.imvFileIcon)


     }


     else if(appFileList[position].file.name.endsWith(".docx")){

         Glide.with(context)
                 .load(R.drawable.docx_icon)
                 .centerCrop()
                 .into(holder.binding.imvFileIcon)



     }
     else if(appFileList[position].file.name.endsWith(".pptx")){

         Glide.with(context)
                 .load(R.drawable.ppt_icon)
                 .centerCrop()
                 .into(holder.binding.imvFileIcon)


     }

     else if(appFileList[position].file.name.endsWith(".xlsx")){

         Glide.with(context)
                 .load(R.drawable.xlsx_icon)
                 .centerCrop()
                 .into(holder.binding.imvFileIcon)



     }


       else if(appFileList[position].file.name.endsWith(".apk")){


            Glide.with(context)
                    .load(appFileList[position].drawable)
                    .centerCrop()
                    .into(holder.binding.imvFileIcon)




        }
        else{
         holder.binding.imvFileIcon.setImageResource(R.drawable.ic_twotone_file_24)

     }


}

    }



    fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
        val bitmap= Bitmap.createBitmap(drawable.intrinsicWidth,drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas= Canvas(bitmap)
        drawable.setBounds(0,0,canvas.width,canvas.height)
        drawable.draw(canvas)
        return bitmap

    }

    fun setOnClickListener( mFilesOnClickListener: FileOnClickListener){
        this.mFileOnClickListener=mFilesOnClickListener
    }

    override fun getItemCount(): Int {
        return appFileList.size
    }

    class FilesViewHolder(val binding: ItemFileBinding):RecyclerView.ViewHolder(binding.root)

}