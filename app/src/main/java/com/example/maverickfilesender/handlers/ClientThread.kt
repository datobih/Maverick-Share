    package com.example.maverickfilesender.handlers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.maverickfilesender.R
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.model.FileMetaData
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_transfer.*
import java.io.*
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.Socket
import java.text.DecimalFormat

class ClientThread(val context: Context) : Thread() {
    val mainContext = context as MainActivity
    var transferFile: File? = null
    var fileTotalSize:Int=0
    var bytesReceived:Int = 0
    var fileName=""
    var bitmap:Bitmap?=null
    override fun run() {
        var socketAddress = InetSocketAddress(mainContext.mIpAddress, 9999)

        var socket = Socket()




                Log.i("SOCKETT", "Client Connecting")
                socket.connect(socketAddress)





        Log.i("SOCKETT", "Client Connected")

        if (socket.isConnected) {

            val inputStream = DataInputStream(socket.getInputStream())

            val outputStream = DataOutputStream(socket.getOutputStream())



            outputStream.writeUTF("David")

            val userName = inputStream.readUTF()

            var bufferedInputStream = BufferedInputStream(socket.getInputStream())
            val handler = android.os.Handler(Looper.getMainLooper())
            handler!!.post {
                mainContext.tv_connection_status.text = "Connected to $userName"

            }




            if (socket.isConnected) {
                while (true) {
                    var fileSize=""


while(fileName=="") {


    fileName = inputStream.readUTF()

}


outputStream.writeUTF("done")
                    Log.d("VERIFY","fileNameDone")

val filesRemaining=inputStream.readUTF()
                    outputStream.writeUTF("done")
                    Log.d("VERIFY","FileRemaining")
                    handler.post {
if(Constants.transferActivity!=null) {
    Constants.transferActivity!!.tv_transfer_toolbar_status.text = "Receiving $filesRemaining remaining files"

}

                    }

                        fileSize = inputStream.readUTF()
outputStream.writeUTF("done")
                    Log.d("VERIFY","fileSize")
                        val thumbnailSize=inputStream.readUTF()
                    outputStream.writeUTF("done")
                    Log.d("VERIFY","ThumbnailSize")


                        Log.d("TESTOS","Thumbnail  $thumbnailSize bytes")
                        if(thumbnailSize!="null"){
                            var tempRead=0

                            val byteData=ByteArray(thumbnailSize.toInt())
                            var offset=0
                            Log.d("TESTOS","Size $thumbnailSize")
//while(tempRead<thumbnailSize.toInt()){
//    val byteBlob=ByteArray(1024*1000)
//    val count=bufferedInputStream.read(byteBlob)
//
//tempRead=tempRead+count
//
//    Log.d("TESTOS","thumbnail bytes $tempRead")
//    for(i in 0..count){
//        byteData[offset]=byteBlob[i]
//        offset++
//    }
//
//
//}

                            inputStream.readFully(byteData,0,byteData.size)
                            outputStream.writeUTF("done")
                        bufferedInputStream =BufferedInputStream(socket.getInputStream())


                            Log.d("TESTOS","Init bitmap")
               bitmap=BitmapFactory.decodeByteArray(byteData,0,byteData.size)



                            Log.d("TESTOS","Setup bitmap")
if(Constants.transferActivity!=null){
handler.post {
    Constants.transferActivity!!.imv_incoming_file.setImageBitmap(bitmap)
}

}
                        }





                        var fileOutputStream: FileOutputStream? = null

                        val fileDir=File(context.getExternalFilesDir(null)!!.path + "/Received")
                        if(!fileDir.exists()){
                            fileDir.mkdirs()
                        }


                        fileOutputStream =
                            FileOutputStream(context.getExternalFilesDir(null)!!.path + "/Received"+"/$fileName")


val byteBuffer=ByteArray((1024*500)+1)

                        fileTotalSize=fileSize.toInt()
                        val fileSizeUnit="/${deriveUnits(fileTotalSize)}"



                        handler!!.post {
                            mainContext.ll_transfering.visibility = View.VISIBLE

                        }
var timer=0

                        while ((bytesReceived) < fileTotalSize){
var count=0
                            bufferedInputStream.read(byteBuffer).also { count=it }


                            bytesReceived += count




                            fileOutputStream!!.write(byteBuffer,0,count)

                            if(Constants.transferActivity!=null){
                                (context as MainActivity).runOnUiThread {
                                    Constants.transferActivity!!.tv_incomingFile_name!!.text=fileName
                                    Constants.transferActivity!!.tv_item_incomingFile_totalSize!!.text="$fileSizeUnit"
                                    Constants.transferActivity!!.tv_item_incomingFile_currentSize!!.text=deriveUnits(bytesReceived)
                                Constants.transferActivity!!.imv_incoming_file.setImageBitmap(bitmap)

                                    if(timer%10==0) {
                                        Constants.transferActivity!!.pb_incoming_file.max = fileTotalSize
                                        Constants.transferActivity!!.pb_incoming_file.progress = bytesReceived
                                    }
                                    timer++
                                }





                            }


                            Log.i("${fileName}", "$count bytes")

                        }
                        if(bytesReceived==fileSize.toInt()){

                            if(Constants.transferActivity!=null) {
                                handler.post {

                                    Constants.transferActivity!!.pb_incoming_file.max = fileTotalSize
                                    Constants.transferActivity!!.pb_incoming_file.progress = bytesReceived
                                    Constants.transferActivity!!.imv_incoming_file.setImageBitmap(bitmap)

                                    Constants.transferActivity!!.adapter!!.fileMetaDataList!!.add(FileMetaData(fileName,fileTotalSize.toLong(),bitmap))
                                    Constants.transferActivity!!.adapter!!.notifyDataSetChanged()
                                }


                            }
                            Thread.sleep(1000)
fileName=""
                            bytesReceived=0
                            fileTotalSize=0
                            Log.i("TransferComplete", "Successful")
                        }





                }


            }
        }


    }


    fun deriveUnits(bytes:Int):String{
        var units=""
        var sizeKb=bytes.toFloat()/1024f
        var sizeMb=sizeKb.toFloat()/1024f
        var sizeGb=sizeMb.toFloat()/1024f

        sizeMb=roundToTwoDecimals(sizeMb)
        sizeGb=roundToTwoDecimals(sizeGb)

        String.format("%.2f",sizeMb)
        if(sizeKb<1000){
            units="${sizeKb}KB"
        }

        else if(sizeKb>1000 && sizeMb<1000){
            units="${sizeMb}MB"

        }

        else if(sizeMb>1000){
            units="${sizeGb}GB"
        }

        return units
    }

    fun roundToTwoDecimals(value:Float):Float{
        val df= DecimalFormat("#.##")



        return df.format(value).toFloat()
    }



}