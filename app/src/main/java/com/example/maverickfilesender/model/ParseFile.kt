package com.example.maverickfilesender.model

import android.graphics.drawable.Drawable
import java.io.File

data class ParseFile(val file: File,var data:ByteArray?,var path:String,val drawable: Drawable) {

}