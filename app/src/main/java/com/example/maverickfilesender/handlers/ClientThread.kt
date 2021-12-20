package com.example.maverickfilesender.handlers

import android.content.Context
import android.os.Looper
import android.util.Log
import com.example.maverickfilesender.activities.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket

class ClientThread(context: Context):Thread() {
val mainContext=context as MainActivity
    override fun run() {
        var socketAddress = InetSocketAddress(mainContext.mIpAddress, 9999)
        var socket = Socket()
        socket.connect(socketAddress, 100000)
        Log.i("SOCKETT", "Client Connected")
        if (socket.isConnected) {
            val outputStream = DataOutputStream(socket.getOutputStream())
            val inputStream = DataInputStream(socket.getInputStream())
            outputStream.writeUTF("Migaaac")

            val userName = inputStream.readUTF()
            val handler=android.os.Handler(Looper.getMainLooper())
            handler!!.post {
                mainContext.tv_connection_status.text = "Connected to $userName"

            }


            while (true){
                if(socket.isConnected){




                }



            }

        }

    }




}