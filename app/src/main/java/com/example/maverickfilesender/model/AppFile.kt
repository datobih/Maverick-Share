package com.example.maverickfilesender.model

import android.graphics.drawable.Drawable
import java.io.File

data class AppFile(val file: File,var drawable: Drawable?=null,var onSelect:Boolean,var data:ByteArray?)
