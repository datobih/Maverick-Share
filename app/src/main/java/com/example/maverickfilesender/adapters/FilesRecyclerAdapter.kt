package com.example.maverickfilesender.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maverickfilesender.R
import com.example.maverickfilesender.listeners.FileOnClickListener
import com.example.maverickfilesender.model.AppFile
import kotlinx.android.synthetic.main.item_file.view.*

class FilesRecyclerAdapter(val context: Context,val appFileList:ArrayList<AppFile>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mFileOnClickListener:FileOnClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FilesViewHolder(LayoutInflater.from(context).inflate(R.layout.item_file,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

if(holder is FilesViewHolder){

    holder.itemView.tv_item_fileName.text = appFileList[position].file.name
    if(appFileList[position].file.isDirectory) {

    holder.itemView.imv_fileIcon.setImageResource(R.drawable.folder_icon)

        holder.itemView.ll_itemFile.setOnClickListener {

mFileOnClickListener!!.onClick(appFileList[position].file)



        }

}
 else{

     if(appFileList[position].file.name.endsWith(".PNG")||
             appFileList[position].file.name.endsWith(".jpg")||
             appFileList[position].file.name.endsWith(".mp4")||
             appFileList[position].file.name.endsWith(".avi")||
             appFileList[position].file.name.endsWith(".mkv")||
             appFileList[position].file.name.endsWith(".mov")
     ){

         Glide.with(context)
                 .load(appFileList[position].file)
                 .centerCrop()
                 .into(holder.itemView.imv_fileIcon)



     }

        else if(appFileList[position].file.name.endsWith(".pdf")||
             appFileList[position].file.name.endsWith(".PDF")){

            Glide.with(context)
                    .load(R.drawable.pdf_icon)
                    .centerCrop()
                    .into(holder.itemView.imv_fileIcon)



        }

     else if(appFileList[position].file.name.endsWith(".zip")||
             appFileList[position].file.name.endsWith(".rar")||
             appFileList[position].file.name.endsWith(".xz")){

         Glide.with(context)
                 .load(R.drawable.zip_icon)
                 .centerCrop()
                 .into(holder.itemView.imv_fileIcon)



     }


     else if(appFileList[position].file.name.endsWith(".docx")){

         Glide.with(context)
                 .load(R.drawable.docx_icon)
                 .centerCrop()
                 .into(holder.itemView.imv_fileIcon)



     }
     else if(appFileList[position].file.name.endsWith(".pptx")){

         Glide.with(context)
                 .load(R.drawable.ppt_icon)
                 .centerCrop()
                 .into(holder.itemView.imv_fileIcon)



     }

     else if(appFileList[position].file.name.endsWith(".xlsx")){

         Glide.with(context)
                 .load(R.drawable.xlsx_icon)
                 .centerCrop()
                 .into(holder.itemView.imv_fileIcon)



     }


       else if(appFileList[position].file.name.endsWith(".apk")){


            Glide.with(context)
                    .load(appFileList[position].drawable)
                    .centerCrop()
                    .into(holder.itemView.imv_fileIcon)


        }
        else{


         Glide.with(context)
                 .load(R.drawable.unknown_file_icon)
                 .centerCrop()
                 .into(holder.itemView.imv_fileIcon)


     }


}


}
    }

    fun setOnClickListener( mFilesOnClickListener: FileOnClickListener){
        this.mFileOnClickListener=mFilesOnClickListener
    }

    override fun getItemCount(): Int {
        return appFileList.size
    }

    class FilesViewHolder(view: View):RecyclerView.ViewHolder(view)

}