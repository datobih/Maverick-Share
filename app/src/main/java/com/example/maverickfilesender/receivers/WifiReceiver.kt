package com.example.maverickfilesender.receivers

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pManager
import android.os.Handler
import android.os.Looper
import android.text.format.Formatter
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.handlers.ClientThread
import kotlinx.android.synthetic.main.activity_main.*
import java.net.InetAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder

class WifiReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

val action=intent.action

        if(action== WifiManager.NETWORK_STATE_CHANGED_ACTION){


            if(Constants.onNetworkAvailable) {
                Constants.onNetworkAvailable = false
                Thread(object :Runnable{

                    override fun run() {
                        Log.d("TESTOS","Outside")
                        var wifiManager =
                                context.getSystemService(Context.WIFI_SERVICE) as WifiManager

                        if (wifiManager.isWifiEnabled) {

                            var wifiInfo = wifiManager.connectionInfo

                            while (wifiInfo.ssid != "\"${Constants.mNetworkSSID}\""){
                                wifiInfo=wifiManager.connectionInfo
                                Log.d("TESTOS","SPAM")
                            }

                            if (wifiInfo.ssid == "\"${Constants.mNetworkSSID}\"") {
                                Log.d("TESTOS","Insidee")

                                val handler=Handler(Looper.getMainLooper())

                                Constants.mainActivity!!.mIpAddress = InetAddress.getByAddress(ByteBuffer
                                        .allocate(Integer.BYTES)
                                        .order(ByteOrder.LITTLE_ENDIAN)
                                        .putInt(wifiManager.dhcpInfo.gateway)
                                        .array()).hostAddress


                                while(   Constants.mainActivity!!.mIpAddress=="0.0.0.0"){
                                    Log.i("TESTOS",Constants.mainActivity!!.mIpAddress)
                                    Constants.mainActivity!!.mIpAddress = InetAddress.getByAddress(ByteBuffer
                                            .allocate(Integer.BYTES)
                                            .order(ByteOrder.LITTLE_ENDIAN)
                                            .putInt(wifiManager.dhcpInfo.gateway)
                                            .array()).hostAddress
                                }
                                Thread.sleep(5000)
                                Constants.clientThread = ClientThread(Constants.mainActivity!!)
                                Constants.clientThread!!.start()
                                (context as MainActivity).runOnUiThread {
                                    Constants.mainActivity!!.btn_receiver.visibility = View.GONE
                                    Constants.mainActivity!!.btn_sender.visibility = View.GONE

                                }


//                    btn_connect_status.visibility = View.VISIBLE
                                Constants.mainActivity!!.connectionType = Constants.CONNECTION_TYPE_WIFI

                            }


                        }

                        else {
                            Log.d("TESTOS","Else")
                        }

                    }

                }).start()
            }
        }



    }
}