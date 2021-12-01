package com.example.maverickfilesender.receivers

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log

class WifiAPReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

    if(intent.action =="android.net.wifi.WIFI_AP_STATE_CHANGED"){

        val apState=intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,0)

        if(apState==13){
Log.i("Broadd","Received")
        }
        else{

        }


        }




    }
}