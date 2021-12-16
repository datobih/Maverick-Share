package com.example.maverickfilesender.listeners

import com.example.maverickfilesender.model.AppFile
import java.io.File

interface RelativePathOnClickListener {

    fun onClick(appFileList:ArrayList<AppFile>,position:Int)
}