package com.example.maverickfilesender.model

import android.graphics.Bitmap
import android.net.Uri

data class Image(val id:Long,val name:String,val size:Int,val dateAdded:Int,val uri: Uri,val bitmap: Bitmap?,var onSelect:Boolean) {

}