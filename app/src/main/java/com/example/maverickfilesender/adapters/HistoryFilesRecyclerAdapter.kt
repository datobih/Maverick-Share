package com.example.maverickfilesender.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maverickfilesender.BuildConfig
import com.example.maverickfilesender.R
import com.example.maverickfilesender.model.AppFile
import kotlinx.android.synthetic.main.item_history_file.view.*

class HistoryFilesRecyclerAdapter(val context: Context, val appFileList: ArrayList<AppFile>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
return HistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_history_file, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {



        if(holder is HistoryViewHolder){



        holder.itemView.tv_item_history_fileName.text=appFileList[position].file.name


            holder.itemView.ll_historyItemFile.setOnClickListener {
val intent= Intent(Intent.ACTION_VIEW)

               val mUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", appFileList[position].file)
                } else{
                    Uri.fromFile(appFileList[position].file)
                }

                val type=if(appFileList[position].file.path.endsWith(".apk")){
                    "application/vnd.android.package-archive"
                }else if(appFileList[position].file.path.endsWith(".doc")||appFileList[position].file.path.endsWith(".docx")){
                    "application/msword"
                }
                else if(appFileList[position].file.path.endsWith(".ppt")||appFileList[position].file.path.endsWith(".pptx")){
                    "application/vnd.ms-powerpoint"
                }
                else if(appFileList[position].file.path.endsWith(".xls")||appFileList[position].file.path.endsWith(".xlsx")){
                    "application/vnd.ms-excel"
                }
                else if(appFileList[position].file.path.endsWith(".zip")){
                    "application/zip"
                }
                else if(appFileList[position].file.path.endsWith(".rar")){
                    "application/x-rar-compressed"
                }
                else if(appFileList[position].file.path.endsWith(".rtf")){
                    "application/rtf"
                }
                else if(appFileList[position].file.path.endsWith(".wav")||appFileList[position].file.path.endsWith(".mp3")){
                    "audio/x-wav"
                }
                else if(appFileList[position].file.path.endsWith(".gif")){
                    "image/gif"
                }
                else if(appFileList[position].file.path.endsWith(".jpg")||appFileList[position].file.path.endsWith(".jpeg")||
                        appFileList[position].file.path.endsWith(".png")){
                    "image/jpeg"
                }
                else if(appFileList[position].file.path.endsWith(".txt")){
                    "text/plain"
                }
                else if(appFileList[position].file.path.endsWith(".mp4")||appFileList[position].file.path.endsWith(".mpg")||
                        appFileList[position].file.path.endsWith(".3gp")||appFileList[position].file.path.endsWith(".mpeg")||
                        appFileList[position].file.path.endsWith(".mpe")||appFileList[position].file.path.endsWith(".avi")){

                    "video/*"
                }


                else{
                    ""
                }
if(type.isNotEmpty()) {
    intent.setDataAndType(mUri, type)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    context.startActivity(intent)
}

            }

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

class HistoryViewHolder(view: View):RecyclerView.ViewHolder(view)
}