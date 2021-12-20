package com.example.maverickfilesender.handlers

import android.content.Context
import android.os.Looper
import android.util.Log
import com.example.maverickfilesender.activities.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.ServerSocket
import java.util.logging.Handler

class ServerThread(val context: Context):Thread() {
val mainContext=context as MainActivity
    override fun run() {
        val serverSocket = ServerSocket(9999)

        val socket = serverSocket.accept()

        if (socket.isConnected) {

            val inputStream = DataInputStream(socket.getInputStream())
            val outputStream = DataOutputStream(socket.getOutputStream())

            val userName = inputStream.readUTF()
val handler=android.os.Handler(Looper.getMainLooper())
            handler!!.post {
                mainContext.tv_connection_status.text = "Connected to $userName"

            }

            outputStream.writeUTF("David")


        }


        Log.i("SOCKETT", "Server Connected")






    }

}