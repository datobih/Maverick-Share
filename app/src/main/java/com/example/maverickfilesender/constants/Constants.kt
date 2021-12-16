package com.example.maverickfilesender.constants

import com.example.maverickfilesender.model.RelativePath
import java.io.File

object Constants{

    val RQ_READ_WRITE_PERMISSION=1
    val RQ_LOCATION_PERMISSION=2
    val CONNECTION_TYPE_HOTSPOT="Hotspot"
    val CONNECTION_TYPE_WIFI="Wifi"


    val BUNDLE_STORAGE_DIRECTORY="storageDirectory"


    var mRelativePath=ArrayList<RelativePath>()
    var sendCount=0
    var countList=ArrayList<Int>()

    var heirarchyFiles=ArrayList<ArrayList<File>>()
var parentFiles=ArrayList<File>()
var selectedFiles=ArrayList<File>()


}