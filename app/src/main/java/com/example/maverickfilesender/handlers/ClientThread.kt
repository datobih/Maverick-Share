    package com.example.maverickfilesender.handlers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.wifi.p2p.WifiP2pManager
import android.os.Environment
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.maverickfilesender.R
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.model.FileMetaData
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_transfer.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.*
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.Socket
import java.text.DecimalFormat


    class ClientThread(val context: Context,val ipAddress:String) : Thread() {
    val mainContext = context as MainActivity
    var transferFile: File? = null
    var fileTotalSize:Int=0
    var bytesReceived:Int = 0
    var fileName=""
    var bitmap:Bitmap?=null
    var filesRemaining=""
    var tempDir=""
    var socket:Socket?=null
    lateinit var outputStream: DataOutputStream
    lateinit var inputStream: DataInputStream
    override fun run() {
        val handler = android.os.Handler(Looper.getMainLooper())
       // mainContext.mIpAddress
        var socketAddress = InetSocketAddress(ipAddress, 9999)

       socket = Socket()


socket!!.soTimeout=1000000000

                Log.i("SOCKETT", "Client Connecting")

        try {
            socket!!.connect(socketAddress)
        }
        catch(e:Exception){
            Log.d("ERRORR",e.stackTraceToString())
            showErrorMessage("Can't connect,device already in use")



            mainContext.p2pManager!!.removeGroup(mainContext.p2pChannel,object: WifiP2pManager.ActionListener{
                override fun onSuccess() {
                    //
                }

                override fun onFailure(p0: Int) {
                    //
                }

            })




            var q=false
            if(!socket!!.isClosed) {
                socket!!.close()
            }
            handler.post {
                (context as MainActivity).civ_connected_profile.visibility=View.GONE
                (context as MainActivity).tv_connected_userName.visibility=View.GONE
                (context as MainActivity).tv_connection_status.visibility=View.VISIBLE
                (context as MainActivity).setupUIdisconnected()
                q=true
            }
            while(!q){

            }
            Constants.clientThread=null
            return
        }




        handler.post {
            (context as MainActivity).ll_loading.visibility=View.GONE

        }

socket!!.soTimeout=2000



        Log.i("SOCKETT", "Client Connected")

        if (socket!!.isConnected) {

            inputStream = DataInputStream(socket!!.getInputStream())

             outputStream =DataOutputStream(socket!!.getOutputStream())



            outputStream.writeUTF(mainContext.navUserName!!.text.toString())

            val userName = inputStream.readUTF()

           val tempBitmap=getBitmapFromDrawable(mainContext.navUserImage!!.drawable)


            val tempStream=ByteArrayOutputStream()

            tempBitmap!!.compress(Bitmap.CompressFormat.JPEG,100,tempStream)
            val tempData=tempStream.toByteArray()

            outputStream.writeUTF(tempData.size.toString())

            inputStream.readUTF()

            outputStream.write(tempData, 0, tempData.size)


            val userPictureSize=inputStream.readUTF()

            val tempByteArray=ByteArray(userPictureSize.toInt())

outputStream.writeUTF("done")

inputStream.readFully(tempByteArray,0,tempByteArray.size)


            val userBitmap=BitmapFactory.decodeByteArray(tempByteArray,0,tempByteArray.size)


            socket!!.soTimeout=1000

            var bufferedInputStream = BufferedInputStream(socket!!.getInputStream())

            handler!!.post {
                mainContext.tv_connection_status.visibility=View.GONE
                mainContext.civ_connected_profile.visibility=View.VISIBLE
                mainContext.civ_connected_profile.setImageBitmap(userBitmap)
                mainContext.tv_connected_userName.visibility=View.VISIBLE
                mainContext.tv_connected_userName.text = "Connected to $userName"
                (context as MainActivity).setupUIconnected()
            }




            if (socket!!.isConnected) {

                try{



                while (true) {
                    var fileSize=""


while(fileName=="") {



    fileName = inputStream.readUTF()

    if(fileName=="..isClientActive"){
        Thread.sleep(200)
      outputStream.writeUTF("..yes")
outputStream.flush()
        fileName=""
        Thread.sleep(100)
    }
}


outputStream.writeUTF("done")
                    Log.d("VERIFY","$fileName fileNameDone")

filesRemaining=inputStream.readUTF()
                    Log.d("VERIFY","$filesRemaining fileNameDone")
                    outputStream.flush()
                    outputStream.writeUTF("done")
                    Log.d("VERIFY","FileRemaining")
//                    handler.post {
//if(Constants.transferActivity!=null) {
//    Constants.transferActivity!!.tv_transfer_toolbar_status.text = "Receiving $filesRemaining remaining files"
//
//
//}
//
//                    }

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
                        bufferedInputStream =BufferedInputStream(socket!!.getInputStream())


                            Log.d("TESTOS","Init bitmap")
               bitmap=BitmapFactory.decodeByteArray(byteData,0,byteData.size)



                            Log.d("TESTOS","Setup bitmap")
//if(Constants.transferActivity!=null){
//handler.post {
//    Constants.transferActivity!!.imv_incoming_file.setImageBitmap(bitmap)
//}
//
//}
                        }





                        var fileOutputStream: FileOutputStream? = null
var fileDir:File?=null
                    if(fileName.endsWith(".jpg")||
                            fileName.endsWith(".jpeg")||
                            fileName.endsWith(".png")||
                            fileName.endsWith(".PNG")||
                            fileName.endsWith(".mp4")||
                            fileName.endsWith(".avi")||
                            fileName.endsWith(".mvk")) {


                        fileDir = File("/storage/emulated/0/DCIM/Maverick")
                    }else{
                        fileDir = File("/storage/emulated/0/Download/Maverick")
                    }

                        if(!fileDir!!.exists()){
                            fileDir!!.mkdirs()
                        }
tempDir=fileDir.path+"/$fileName"

                        fileOutputStream =
                            FileOutputStream(fileDir.path+"/$fileName")


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


Log.d("TRANSFER",bytesReceived.toString())

                            fileOutputStream!!.write(byteBuffer,0,count)

                            if(Constants.transferActivity!=null){
                                (context as MainActivity).runOnUiThread {

                                    if(Constants.transferActivity?.vf_transfer_state?.displayedChild==0){
                                        Constants.transferActivity?.transferViewModel?.isFileTransferring?.value=true
                                        Constants.transferActivity!!.pb_incoming_file.max = fileTotalSize
                                        Constants.transferActivity?.imv_incoming_file?.setImageBitmap(bitmap)
                                        Constants.transferActivity?.tv_incomingFile_name?.text =fileName
                                        Constants.transferActivity?.tv_item_incomingFile_totalSize?.text = "$fileSizeUnit"
                                        Constants.transferActivity!!.tv_transfer_toolbar_status.text = "Receiving $filesRemaining remaining files"

                                    }


                                    if(timer%10==0) {
                                        Constants.transferActivity?.tv_item_incomingFile_currentSize?.text = deriveUnits(bytesReceived)


                                        Constants.transferActivity!!.transferViewModel!!.mutableLiveData!!.value=bytesReceived
                                    }
                                    timer++
                                }





                            }


                            Log.i("${fileName}", "$count bytes")

                        }
                        if(bytesReceived>=fileSize.toInt()){

var done=true
                            if(Constants.transferActivity!=null) {
                                done=false
                                handler.post {

                                 Constants.transferActivity?.transferViewModel?.isFileTransferring?.value=false

                                    Constants.transferActivity!!.adapter!!.fileMetaDataList!!.add(FileMetaData(fileName,fileTotalSize.toLong(),bitmap))
                                    Constants.transferActivity!!.adapter!!.notifyDataSetChanged()
                                    fileName=""
                                    filesRemaining=(filesRemaining.toInt()-1).toString()
                                    bytesReceived=0
                                    fileTotalSize=0
                                    if(filesRemaining=="0"){
                                        Constants.transferActivity!!.tv_transfer_toolbar_status.text = "No files remaining"
                                    }
done=true
                                }


                            }else{
                                fileName=""
                                filesRemaining=(filesRemaining.toInt()-1).toString()
                                bytesReceived=0
                                fileTotalSize=0
                            }
while(!done){

}
outputStream.writeUTF("done")
                            Log.i("TransferComplete", "Successful")
                        }





                }
                }
                catch (e:Exception){
                    Log.d("ERRORR",e.stackTraceToString())
                   showErrorMessage("Something went wrong")



                    mainContext.p2pManager!!.removeGroup(mainContext.p2pChannel,object: WifiP2pManager.ActionListener{
                        override fun onSuccess() {
                            //
                        }

                        override fun onFailure(p0: Int) {
                            //
                        }

                    })


//                    try {
//                        socket!!.shutdownInput()
//                    }catch (e:Exception){
//
//                    }
                    val mFile=File(tempDir)

                    if(mFile.exists()){
                        val isDeleted=mFile.delete()


                    }
                    var q=false
                    if(!socket!!.isClosed) {
                        socket!!.close()
                    }
                    handler.post {
                        (context as MainActivity).civ_connected_profile.visibility=View.GONE
                        (context as MainActivity).tv_connected_userName.visibility=View.GONE
                        (context as MainActivity).tv_connection_status.visibility=View.VISIBLE
                        (context as MainActivity).setupUIdisconnected()
q=true
                    }
while(!q){

}
Constants.clientThread=null
                    return

                }

            }
        }


    }

    fun getBitmapFromDrawable(drawable: Drawable):Bitmap{
        val bitmap=Bitmap.createBitmap(drawable.intrinsicWidth,drawable.intrinsicHeight,Bitmap.Config.ARGB_8888)
        val canvas= Canvas(bitmap)
        drawable.setBounds(0,0,canvas.width,canvas.height)
        drawable.draw(canvas)
        return bitmap

    }


    fun showErrorMessage( message:String){
        val snackBar= if(Constants.transferActivity!=null){
            Snackbar.make(Constants.transferActivity!!.findViewById(android.R.id.content),message,Snackbar.LENGTH_SHORT)

        }else{
            Snackbar.make((context as MainActivity).findViewById(android.R.id.content),message,Snackbar.LENGTH_SHORT)
        }
        snackBar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.maverick_blue))
        snackBar.show()

        bytesReceived=0
        fileTotalSize=0

    }


    fun noFiles(){
        Constants.transferActivity?.ll_transfer_stack?.visibility=View.INVISIBLE
        Constants.transferActivity?.tv_fileTransfer_status?.visibility=View.VISIBLE
        Constants.transferActivity?.tv_fileTransfer_status?.text="No Files Incoming"
    }

    fun filesSending(){
        Constants.transferActivity?.ll_transfer_stack?.visibility=View.VISIBLE
        Constants.transferActivity?.tv_fileTransfer_status?.visibility=View.INVISIBLE

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