package com.example.maverickfilesender.constants

import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.activities.TransferActivity
import com.example.maverickfilesender.handlers.ClientThread
import com.example.maverickfilesender.handlers.ServerThread
import com.example.maverickfilesender.model.FileMetaData
import com.example.maverickfilesender.model.ParseFile
import com.example.maverickfilesender.model.RelativePath
import java.io.File

object Constants{

    val RQ_READ_WRITE_PERMISSION=1
    val RQ_LOCATION_PERMISSION=2
    val CONNECTION_TYPE_HOTSPOT="Hotspot"
    val CONNECTION_TYPE_WIFI="Wifi"

var onNetworkAvailable=false
    var mNetworkSSID=""


    var mainActivity:MainActivity?=null

    val BUNDLE_STORAGE_DIRECTORY="storageDirectory"
    val TRANSFER_EXTRA="transferType"

    var mRelativePath=ArrayList<RelativePath>()
    var sendCount=0
    var countList=ArrayList<Int>()

    var heirarchyFiles=ArrayList<ArrayList<File>>()
var parentFiles=ArrayList<File>()
var selectedFiles=ArrayList<ParseFile>()
    val tempSelectedFiles=ArrayList<ParseFile>()


    var imagesSelected=ArrayList<Int>()
    var videosSelected=ArrayList<Int>()
var appSelected=ArrayList<Int>()




    var serverThread:ServerThread?=null
var clientThread:ClientThread?=null
var onSelectedMetaData=ArrayList<FileMetaData>()
    val inStoragePath="/storage/emulated/0"
    var transferActivity:TransferActivity?=null
}