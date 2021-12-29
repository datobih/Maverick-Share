package com.example.maverickfilesender.handlers

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Environment
import android.os.Looper
import android.util.Log
import android.view.View
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.constants.Constants
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
    override fun run() {
        var socketAddress = InetSocketAddress(mainContext.mIpAddress, 9999)

        var socket = Socket()




                Log.i("SOCKETT", "Client Connecting")
                socket.connect(socketAddress, 1000000000)





        Log.i("SOCKETT", "Client Connected")

        if (socket.isConnected) {

            val inputStream = DataInputStream(socket.getInputStream())
            val outputStream = DataOutputStream(socket.getOutputStream())



            outputStream.writeUTF("David")

            val userName = inputStream.readUTF()

            val bufferedInputStream = DataInputStream(BufferedInputStream(socket.getInputStream()))
            val handler = android.os.Handler(Looper.getMainLooper())
            handler!!.post {
                mainContext.tv_connection_status.text = "Connected to $userName"

            }




            if (socket.isConnected) {
                while (true) {
                    var fileSize=""
                    try {

                        fileName = inputStream.readUTF()

                        fileSize = inputStream.readUTF()


                        val thumbnailSize=inputStream.readUTF()

                        if(thumbnailSize!="null"){
                            val tempRead=0
                            val byteBlob=ByteArray(1024*500)
while(tempRead<thumbnailSize.toInt()){

    bufferedInputStream.read(byteBlob)


}
               val bitmap=BitmapFactory.decodeByteArray(byteBlob,0,byteBlob.size)

                        }



                        var fileOutputStream: FileOutputStream? = null

                        val fileDir=File(context.getExternalFilesDir(null)!!.path + "/Received")
                        if(!fileDir.exists()){
                            fileDir.mkdirs()
                        }

                        if (fileName.endsWith("apk")) fileOutputStream =
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


                            if(Constants.transferActivity!=null){
                             handler.post {
                                    Constants.transferActivity!!.tv_incomingFile_name.text=fileName
                                    Constants.transferActivity!!.tv_item_incomingFile_totalSize.text="$fileSizeUnit"
                                    Constants.transferActivity!!.tv_item_incomingFile_currentSize.text=deriveUnits(bytesReceived)
if(timer%10==0) {
    Constants.transferActivity!!.pb_incoming_file.max = fileTotalSize
    Constants.transferActivity!!.pb_incoming_file.progress = bytesReceived
}
timer++
                                }





                            }

                            fileOutputStream!!.write(byteBuffer,0,count)


                            Log.i("${fileName}", "$count bytes")

                        }
                        if(bytesReceived==fileSize.toInt()){
                            (context as MainActivity).runOnUiThread {

                                Constants.transferActivity!!.pb_incoming_file.max=fileTotalSize
                                Constants.transferActivity!!.pb_incoming_file.progress=bytesReceived


                            }
fileName=""
                            bytesReceived=0
                            fileTotalSize=0
                            Log.i("TransferComplete", "Successful")
                        }


                    } catch (e: Exception) {

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