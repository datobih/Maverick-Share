  package com.example.maverickfilesender.handlers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.maverickfilesender.R
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.model.FileMetaData
import com.example.maverickfilesender.model.ParseFile
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_transfer.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.*
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketTimeoutException
import java.text.DecimalFormat
import java.util.logging.Handler

class ServerThread(val context: Context) : Thread() {
    val mainContext = context as MainActivity
    var transferFile: ParseFile? = null
    var bytesTransferred: Int = 0
    var fileSize: Int = 0
    var serverSocket:ServerSocket?=null
    var bitmap:Bitmap?=null
    var socket:Socket?=null
 override fun run() {
         serverSocket = ServerSocket(9999)

        serverSocket!!.soTimeout = 1000000000

        try {
             socket = serverSocket!!.accept()
        }

        catch (e:Exception) {
return
        }
socket!!.soTimeout=700


        if (socket!!.isConnected) {

            val outputStream = DataOutputStream(socket!!.getOutputStream())
            var inputStream = DataInputStream(socket!!.getInputStream())


            val userName = inputStream.readUTF()


            outputStream.writeUTF(mainContext.navUserName!!.text.toString())
            val handler = android.os.Handler(Looper.getMainLooper())
            handler!!.post {
                mainContext.tv_connected_userName.text = "Connected to $userName"
                (context as MainActivity).setupUIconnected()
            }


            while (true) {
try {

    if (socket!!.isConnected) {

        if (Constants.selectedFiles.isNotEmpty()) {
socket!!.soTimeout=1000000000
            var dir = ""
            transferFile = Constants.selectedFiles[0]
            fileSize = transferFile!!.file.length().toInt()
            Constants.onSelectedMetaData.removeAt(0)
            Constants.selectedFiles.remove(Constants.selectedFiles[0])


            if (Constants.transferActivity != null) {


                handler.post {


                    Constants.transferActivity!!.adapter?.notifyDataSetChanged()

                }


            }

            val fileSizeUnit = deriveUnits(transferFile!!.file.length().toInt()).toString()
            var fileInputStream: BufferedInputStream? = null
            if (transferFile!!.path.isEmpty()) {
                fileInputStream =
                        BufferedInputStream(FileInputStream(transferFile!!.file.path))
            } else {
                fileInputStream =
                        BufferedInputStream(FileInputStream(transferFile!!.path))
            }
            val bufferedOutputStream =
                    BufferedOutputStream(socket!!.getOutputStream())


            var fileName = transferFile!!.file.name

            if (transferFile!!.file.name.endsWith(".apk")) {

                val packageManager = context!!.packageManager
                val packageInfo = packageManager.getPackageArchiveInfo(transferFile!!.file.path, 0)
                fileName = packageInfo!!.applicationInfo.loadLabel(packageManager).toString() + ".apk"

            }

            outputStream.writeUTF(fileName)
            //  outputStream.flush()
            inputStream.readUTF()
            val fileRemaining = Constants.selectedFiles.size + 1
            outputStream.writeUTF(fileRemaining.toString())
            Log.d("VERIFY", "$fileRemaining remaining")
            val mResponse = inputStream.readUTF()
            Log.d("VERIFY", "$mResponse response")
//handler.post {
//    if(Constants.transferActivity!=null) {
//        Constants.transferActivity!!.tv_transfer_toolbar_status.text="Sending ${Constants.selectedFiles.lastIndex+1} remaining files"
//
//    }
//}

            outputStream.writeUTF(transferFile!!.file.length().toString())
            Log.d("VERIFY", "length sent")
            inputStream.readUTF()
            if (transferFile!!.data != null) {
                outputStream.writeUTF(transferFile!!.data!!.size.toString())
                Log.d("VERIFY", "thumbnail size")
                inputStream.readUTF()




                outputStream.write(transferFile!!.data, 0, transferFile!!.data!!.size)

                Log.d("VERIFY", "thumbnail sent")
                val response = inputStream.readUTF()
                Log.d("RESPONSE", response)



                Thread.sleep(100)


            } else {
                outputStream.writeUTF("null")
            }


            val byteBuffer = ByteArray(1024 * 500)

            var read = 0
            var timer = 0
            while (fileInputStream.read(byteBuffer).also { read = it } > 0) {

                Log.i("${transferFile!!.file.name}", "$read bytes")



                bufferedOutputStream.write(byteBuffer, 0, read)
                bytesTransferred = bytesTransferred + read
                timer++
                Log.d("UITEST", "Outside")

                if (Constants.transferActivity != null) {
                    (context as MainActivity).runOnUiThread {
                        Log.d("UITEST", "Inside")

                        if (Constants.transferActivity?.vf_transfer_state?.displayedChild == 0) {
                            Constants.transferActivity?.transferViewModel?.isFileTransferring?.value = true
                            Constants.transferActivity?.pb_incoming_file?.max = transferFile!!.file.length().toInt()
                            Constants.transferActivity?.imv_incoming_file?.setImageDrawable(transferFile!!.drawable)
                            Constants.transferActivity?.tv_incomingFile_name?.text = fileName
                            Constants.transferActivity?.tv_item_incomingFile_totalSize?.text = "/$fileSizeUnit"
                            Constants.transferActivity!!.tv_transfer_toolbar_status.text = "Sending ${Constants.selectedFiles.lastIndex + 2} remaining files"

                        }



                        Constants.transferActivity?.transferViewModel?.mutableLiveData?.value = bytesTransferred


                    }


                }

            }
            if (read <= 0) {
                var done = true
                if (Constants.transferActivity != null) {
                    done = false
                    handler.post {
                        Constants.transferActivity?.transferViewModel?.isFileTransferring?.value = false
                        if (Constants.selectedFiles.isEmpty()) {
                            Constants.transferActivity!!.tv_transfer_toolbar_status.text = "No files remaining"
                        }
                        bytesTransferred = 0
                        Log.i("TransferComplete", "Successful")
                        fileSize = 0
                        bytesTransferred = 0
                        read = 0
                        done = true
                        Thread.sleep(100)
                    }
                } else {
                    bytesTransferred = 0
                    Log.i("TransferComplete", "Successful")
                    fileSize = 0
                    bytesTransferred = 0
                    read = 0

                }

                while (!done) {

                }
                val response = inputStream.readUTF()


                //  Thread.sleep(1000)
//                                var bitmap:Bitmap?=null
//                                if(transferFile!!.data!=null) {
//                                    bitmap = BitmapFactory.decodeByteArray(transferFile!!.data, 0, transferFile!!.data!!.size)
//                                }


            }


//    if(transferFile!!.name.endsWith("apk")){
//dir=context.getExternalFilesDir(null)!!.path+"/Apps"
//
//        if(!File(dir).exists()){
//            File(dir).mkdirs()
//        }
//
//    }


        } else {
            //**PREVIOUS IMPLEMENTATION**//
//socket.soTimeout=200
//            try{
//inputStream.readUTF()
//            }
//            catch (e:Exception){
//
//                if(e is SocketTimeoutException){
//
//                }
//                else{
//                 showErrorMessage()
//                   if( (context as MainActivity).mReservation!=null){
//                       (context as MainActivity).mReservation!!.close()
//                       Thread.sleep(3000)
//                       (context as MainActivity).mReservation=null
//
//
//                   }
//                    Constants.serverThread!!.serverSocket!!.close()
//
//                    return
//                }
//
//
//            }

            outputStream.writeUTF("..isClientActive")

            val response=inputStream.readUTF()

Log.d("RESPONSEEE",response)


        }

    } else {
        showErrorMessage()
        if( (context as MainActivity).mReservation!=null){
            (context as MainActivity).mReservation!!.close()
            Thread.sleep(3000)
            (context as MainActivity).mReservation=null


        }
        Constants.serverThread!!.serverSocket!!.close()
        var q=false
        handler.post {
            (context as MainActivity).setupUIdisconnected()
            q=true
        }
        while(!q){

        }
//        fileSize = 0
//        bytesTransferred = 0

        return
    }
}
catch (e:Exception){

    showErrorMessage()
    if( (context as MainActivity).mReservation!=null){
        (context as MainActivity).mReservation!!.close()
        Thread.sleep(3000)
        (context as MainActivity).mReservation=null


    }
  serverSocket!!.close()
socket!!.close()
    var q=false
    handler.post {
        (context as MainActivity).setupUIdisconnected()
        q=true
    }
    while(!q){

    }


    return


}


            }

        }


    }


    fun showErrorMessage(){
        val snackBar= if(Constants.transferActivity!=null){
            Snackbar.make(Constants.transferActivity!!.findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_SHORT)

        }else{
            Snackbar.make((context as MainActivity).findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_SHORT)
        }
        snackBar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.maverick_blue))
        snackBar.show()

        fileSize = 0
        bytesTransferred = 0

        return
    }

    fun noFiles(){


        Constants.transferActivity?.tv_fileTransfer_status?.visibility=View.VISIBLE
        Constants.transferActivity?.tv_fileTransfer_status?.text="No Files Incoming"

        Constants.transferActivity?.ll_transfer_stack?.visibility=View.INVISIBLE



    }

    fun filesSending(){
        Constants.transferActivity?.ll_transfer_stack?.visibility=View.VISIBLE
        Constants.transferActivity?.tv_fileTransfer_status?.visibility=View.INVISIBLE

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

