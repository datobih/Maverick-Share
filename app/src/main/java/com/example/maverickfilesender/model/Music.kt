package com.example.maverickfilesender.model

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

 data class Music(
         val uri: Uri?,
         val id:Long,
         val name: String?,
         val artist: String?,
         val album:String?,
         val albumArt:Bitmap?,
         val duration: Int,
         val size: Int
)