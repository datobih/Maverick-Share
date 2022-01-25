package com.example.maverickfilesender.model

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import java.time.Duration

data class Video(val id:Long, val name:String, val size:Int, val dateAdded:Int, val uri: Uri, val path:String,val duration: Int,val durationStr:String,var data: ByteArray?,val bitmap: Bitmap?,var onSelect:Boolean) {
}