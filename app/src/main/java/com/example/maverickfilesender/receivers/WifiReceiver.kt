package com.example.maverickfilesender.receivers

import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.ScanResult
import android.net.wifi.SupplicantState
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pManager
import android.os.Handler
import android.os.Looper
import android.text.format.Formatter
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maverickfilesender.R
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.adapters.SSIDListRecyclerAdapter
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.handlers.ClientThread
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.dialog_hotspot_receiver.*
import java.net.InetAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder

class WifiReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

val action=intent.action



        if(action==WifiManager.SUPPLICANT_STATE_CHANGED_ACTION){

            if(intent.hasExtra(WifiManager.EXTRA_SUPPLICANT_ERROR)){
                if(Constants.isReconnected==null)
                Constants.isReconnected=true
            }

        }


if(action==WifiManager.SCAN_RESULTS_AVAILABLE_ACTION && (context as MainActivity).shouldScan){
    (context as MainActivity).shouldScan=false
    val wifiManager =
            context.getSystemService(Context.WIFI_SERVICE) as WifiManager



    val scanResult = wifiManager.scanResults
    val receiversResult = ArrayList<ScanResult>()
    for (i in scanResult) {

        Log.i("WIFII", i.SSID)

//if(i.SSID.contains("AndroidShare_")){
        receiversResult.add(i)
//}


    }


    if (receiversResult.isNotEmpty()) {

        val dialog = Dialog(context)
        (context as MainActivity).mDialog = dialog
        dialog.setContentView(R.layout.dialog_hotspot_receiver)
        val adapter = SSIDListRecyclerAdapter(context, receiversResult)

        dialog.rv_receiver_ssid.layoutManager = LinearLayoutManager(context)
        dialog.rv_receiver_ssid.setHasFixedSize(true)

        dialog.rv_receiver_ssid.adapter = adapter
        dialog.show()


    }

}

       else if(action== WifiManager.NETWORK_STATE_CHANGED_ACTION){


            if(Constants.onNetworkAvailable) {
                Constants.onNetworkAvailable = false
                Thread(object :Runnable{

                    override fun run() {
                        Log.d("TESTOS","Outside")
                        var wifiManager =
                                context.getSystemService(Context.WIFI_SERVICE) as WifiManager



                        if (wifiManager.isWifiEnabled) {

                            val connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                            val connectivityInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)


                            var wifiInfo = wifiManager.connectionInfo

                            while (wifiInfo.ssid != "\"${Constants.mNetworkSSID}\""){
                                wifiInfo=wifiManager.connectionInfo
                                Log.d("TESTOS","SPAM")
//                                if(Constants.noNetwork){
//                                    Constants.noNetwork=false
//                                    break
//                                }
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

                                    if(Constants.noNetwork){
                                        Constants.noNetwork=false
                                        return
                                    }

if(Constants.isReconnected!=null && Constants.isReconnected==true){
    Constants.isReconnected=false
    return
}

                                }
//                        if(wifiInfo.ssid != "\"${Constants.mNetworkSSID}\""){
//                            return
//                        }
                                Thread.sleep(5000)
                                Constants.clientThread = ClientThread(Constants.mainActivity!!)
                                Log.d("WIFITHREAD","Made")
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