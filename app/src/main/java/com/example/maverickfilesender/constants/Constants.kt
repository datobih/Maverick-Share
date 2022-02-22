package com.example.maverickfilesender.constants

import android.app.Dialog
import android.graphics.Bitmap
import android.widget.ImageView
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.activities.TransferActivity
import com.example.maverickfilesender.handlers.ClientThread
import com.example.maverickfilesender.handlers.ServerThread
import com.example.maverickfilesender.model.FileMetaData
import com.example.maverickfilesender.model.ParseFile
import com.example.maverickfilesender.model.RelativePath
import java.io.File
import java.net.ServerSocket

object Constants{
val SHARED_PREFERENCES_NAME="maverick_file_sender"

    val SP_DARK_MODE="maverick_dark_mode"
  val SP_PROFILE_PIC_DATA="profile_pic"
    val SP_PROFILE_USERNAME="profile_username"
    val RQ_READ_WRITE_PERMISSION=1
    val RQ_LOCATION_PERMISSION=2
    val RQ_PERMISSION_ACTIVITY=3
    val RQ_PROFILE_ACTIVITY=4
    val RQ_PROFILE_PIC=5

var isServer=false

var isClose=false
    var isWriting=false
var serverSocket:ServerSocket?=null



    val CONNECTION_TYPE_HOTSPOT="Hotspot"
    val CONNECTION_TYPE_WIFI="Wifi"


    var userPictureOnChanged:Bitmap?=null
    var userNameOnChanged:String=""


    var ssidDialog:Dialog?=null
    var noNetwork=false
var onNetworkAvailable=false
    var mNetworkSSID=""
var isReconnected:Boolean?=false

    var mainActivity:MainActivity?=null

    val BUNDLE_STORAGE_DIRECTORY="storageDirectory"
    val TRANSFER_EXTRA="transferType"

    var mRelativePath=ArrayList<RelativePath>()
    var sendCount=0
    var countList=ArrayList<Int>()

    var heirarchyFiles=ArrayList<ArrayList<ParseFile>>()
var parentFiles=ArrayList<ParseFile>()
var selectedFiles=ArrayList<ParseFile>()
    val tempSelectedFiles=ArrayList<ParseFile>()


    var imagesSelected=ArrayList<Int>()
    var videosSelected=ArrayList<Int>()
    var audioSelected=ArrayList<Int>()
var appSelected=ArrayList<Int>()




    var serverThread:ServerThread?=null
var clientThread:ClientThread?=null
var onSelectedMetaData=ArrayList<FileMetaData>()
    val inStoragePath="/storage/emulated/0"
    var transferActivity:TransferActivity?=null

    val filesIconView=ArrayList<ImageView>()

}