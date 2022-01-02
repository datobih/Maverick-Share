package com.example.maverickfilesender.handlers

import android.content.Context
import android.os.Looper
import android.util.Log
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.model.ParseFile
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_transfer.*
import java.io.*
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket
import java.text.DecimalFormat
import java.util.logging.Handler

class ServerThread(val context: Context) : Thread() {
    val mainContext = context as MainActivity
    var transferFile: ParseFile? = null
    var bytesTransferred: Int = 0
    var fileSize: Int = 0
    override fun run() {
        val serverSocket = ServerSocket(9999)

        serverSocket.soTimeout = 1000000000

        val socket = serverSocket.accept()





        if (socket!!.isConnected) {
            val outputStream = DataOutputStream(socket.getOutputStream())
            val inputStream = DataInputStream(socket.getInputStream())


            val userName = inputStream.readUTF()


            outputStream.writeUTF("Migaaac")
            val handler = android.os.Handler(Looper.getMainLooper())
            handler!!.post {
                mainContext.tv_connection_status.text = "Connected to $userName"

            }


            while (true) {
                try {
                    if (socket.isConnected) {

                        if (Constants.selectedFiles.isNotEmpty()) {

                            var dir = ""
                            transferFile = Constants.selectedFiles[0]
                            fileSize = transferFile!!.file.length().toInt()
                            Constants.selectedFiles.remove(Constants.selectedFiles[0])

                            val fileSize = deriveUnits(transferFile!!.file.length().toInt()).toString()

                            val fileInputStream =
                                    BufferedInputStream(FileInputStream(transferFile!!.file.path))
                            val bufferedOutputStream =
                              BufferedOutputStream(socket.getOutputStream())

                            var fileName = transferFile!!.file.name
                            if (transferFile!!.file.name.endsWith(".apk")) {
                                val packageManager = context!!.packageManager
                                val packageInfo = packageManager.getPackageArchiveInfo(transferFile!!.file.path, 0)
                                fileName = packageInfo!!.applicationInfo.loadLabel(packageManager).toString()
                            }

                            outputStream.writeUTF(fileName)
                            inputStream.readUTF()
                            outputStream.writeUTF(transferFile!!.file.length().toString())

                            if (transferFile!!.data != null) {
                            outputStream.writeUTF(transferFile!!.data!!.size.toString())



   bufferedOutputStream.write(transferFile!!.data,0,transferFile!!.data!!.size)

bufferedOutputStream.flush()
                                val response=inputStream.readUTF()







                            }
                            else{
                                outputStream.writeUTF("null")
                            }


                            val byteBuffer = ByteArray(1024 * 500)

                            var read = 0
                            while (fileInputStream.read(byteBuffer).also { read = it } > 0) {

                                Log.i("${transferFile!!.file.name}", "$read bytes")



                                bufferedOutputStream.write(byteBuffer, 0, read)
                                bytesTransferred = bytesTransferred + read

                                if (Constants.transferActivity != null) {
                                  handler.post {
                                        Constants.transferActivity!!.tv_incomingFile_name.text = transferFile!!.file.name
                                        Constants.transferActivity!!.tv_item_incomingFile_totalSize.text = "/$fileSize"
                                        Constants.transferActivity!!.tv_item_incomingFile_currentSize.text = deriveUnits(bytesTransferred)
                                        Constants.transferActivity!!.pb_incoming_file.max = transferFile!!.file.length().toInt()
                                        Constants.transferActivity!!.pb_incoming_file.progress = bytesTransferred

                                    }


                                }

                            }
                            if (read < 0) {
                                Thread.sleep(300)
                                bytesTransferred = 0
                                Log.i("TransferComplete", "Successful")
                            }

                            read = 0


//    if(transferFile!!.name.endsWith("apk")){
//dir=context.getExternalFilesDir(null)!!.path+"/Apps"
//
//        if(!File(dir).exists()){
//            File(dir).mkdirs()
//        }
//
//    }


                        }



                    } else {
                        fileSize = 0
                        bytesTransferred = 0

                        return
                    }
                } catch (e: Exception) {

                    return
                }


            }

        }


    }


    fun deriveUnits(bytes: Int): String {
        var units = ""
        var sizeKb = bytes.toFloat() / 1024f
        var sizeMb = sizeKb.toFloat() / 1024f
        var sizeGb = sizeMb.toFloat() / 1024f

        sizeMb = roundToTwoDecimals(sizeMb)
        sizeGb = roundToTwoDecimals(sizeGb)

        String.format("%.2f", sizeMb)
        if (sizeKb < 1000) {
            units = "${sizeKb}KB"
        } else if (sizeKb > 1000 && sizeMb < 1000) {
            units = "${sizeMb}MB"

        } else if (sizeMb > 1000) {
            units = "${sizeGb}GB"
        }

        return units
    }

    fun roundToTwoDecimals(value: Float): Float {
        val df = DecimalFormat("#.##")



        return df.format(value).toFloat()
    }


}

