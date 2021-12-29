package com.example.maverickfilesender.model

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri

data class Image(val id:Long, val name:String, val size:Int, val dateAdded:Int, val uri: Uri, var data: ByteArray?, var onSelect:Boolean) {

}