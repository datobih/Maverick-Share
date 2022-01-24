package com.example.maverickfilesender.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
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
import com.example.maverickfilesender.model.AppFile
import kotlinx.android.synthetic.main.item_file.view.*
import kotlinx.android.synthetic.main.item_file.view.imv_fileIcon
import kotlinx.android.synthetic.main.item_file.view.ll_itemFile
import kotlinx.android.synthetic.main.item_history_file.view.*
import java.io.ByteArrayOutputStream
import java.io.File

class HistoryFilesRecyclerAdapter(val context: Context,val appFileList:ArrayList<AppFile>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
return HistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_history_file,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {



        if(holder is HistoryViewHolder){


        holder.itemView.tv_item_history_fileName.text=appFileList[position].file.name

        if(appFileList[position].file.isDirectory) {

            holder.itemView.imv_history_fileIcon.setImageResource(R.drawable.folder_icon)



        }

        else{


            if(appFileList[position].file.name.endsWith(".PNG")||
                appFileList[position].file.name.endsWith(".jpg")||
                appFileList[position].file.name.endsWith(".mp4")

            ){

                Glide.with(context)
                    .load(appFileList[position].file)
                    .centerCrop()
                    .into(holder.itemView.imv_history_fileIcon)



            }


            else if(appFileList[position].file.name.endsWith(".avi")||
                appFileList[position].file.name.endsWith(".mkv")){

                Glide.with(context)
                    .load(R.drawable.video_icon)
                    .centerCrop()
                    .into(holder.itemView.imv_history_fileIcon)



            }


            else if(appFileList[position].file.name.endsWith(".pdf")||
                appFileList[position].file.name.endsWith(".PDF")){

                Glide.with(context)
                    .load(R.drawable.pdf_icon)
                    .centerCrop()
                    .into(holder.itemView.imv_history_fileIcon)



            }

            else if(appFileList[position].file.name.endsWith(".zip")||
                appFileList[position].file.name.endsWith(".rar")||
                appFileList[position].file.name.endsWith(".xz")){

                Glide.with(context)
                    .load(R.drawable.zip_icon)
                    .centerCrop()
                    .into(holder.itemView.imv_history_fileIcon)



            }

            else if(appFileList[position].file.name.endsWith(".docx")){

                Glide.with(context)
                    .load(R.drawable.docx_icon)
                    .centerCrop()
                    .into(holder.itemView.imv_history_fileIcon)



            }

            else if(appFileList[position].file.name.endsWith(".ppt")){

                Glide.with(context)
                    .load(R.drawable.ppt_icon)
                    .centerCrop()
                    .into(holder.itemView.imv_history_fileIcon)



            }

            else if(appFileList[position].file.name.endsWith(".xlsx")){

                Glide.with(context)
                    .load(R.drawable.xlsx_icon)
                    .centerCrop()
                    .into(holder.itemView.imv_history_fileIcon)



            }
            else if(appFileList[position].file.name.endsWith(".apk")){

                Glide.with(context)
                    .load(appFileList[position].drawable)
                    .centerCrop()
                    .into(holder.itemView.imv_history_fileIcon)



            }

            else{
                Glide.with(context)
                    .load(R.drawable.ic_twotone_file_24)
                    .centerCrop()
                    .into(holder.itemView.imv_history_fileIcon)

            }



        }
        }

    }

    override fun getItemCount(): Int {
        return appFileList.size
    }

class HistoryViewHolder(view:View):RecyclerView.ViewHolder(view)
}