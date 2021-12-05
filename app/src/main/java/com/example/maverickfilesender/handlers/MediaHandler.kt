package com.example.maverickfilesender.handlers

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.example.maverickfilesender.model.Image
import java.lang.Exception

class MediaHandler(val context: Context) {

    val imageCollection =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)

        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        }

    val imageProjection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media.DATE_ADDED,

    )

    val videoCollection=  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)

    } else {
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI

    }


    val videoProjection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATE_ADDED,

            )




    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"


    fun fetchImageFiles() : ArrayList<Image>{

        val query = context.contentResolver.query(
            imageCollection, imageProjection,
            null,
            null,
            sortOrder
        )

val imageList=ArrayList<Image>()


        query.use { cursor ->

            cursor!!.moveToFirst()

            val idColumn = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)


 while (cursor.moveToNext()){

     val id=cursor.getLong(idColumn)
     val name=cursor.getString(nameColumn)
     val size=cursor.getInt(sizeColumn)
     val date=cursor.getInt(dateAddedColumn)


val contentUri=ContentUris.withAppendedId( MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL),id)

 //    var bitmap:Bitmap?=null

// if(Build.VERSION.SDK_INT>=28){
//try {
//    val source = ImageDecoder.createSource(context.contentResolver, contentUri)
//    bitmap = ImageDecoder.decodeBitmap(source)
//
//}
//catch (e:Exception){
//bitmap=null
//}



// }
//     else{
//
//bitmap=MediaStore.Images.Media.getBitmap(context.contentResolver,contentUri)
//
// }

imageList.add(Image(id,name,size,date,contentUri,null))


 }

        }

return imageList
    }
}