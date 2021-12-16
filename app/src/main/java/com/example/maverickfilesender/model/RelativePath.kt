package com.example.maverickfilesender.model

import java.io.File

data class RelativePath(val name:String,val file: File,val appFileList:ArrayList<AppFile>) {
}