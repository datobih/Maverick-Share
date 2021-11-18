package com.example.maverickfilesender.receivers

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pManager

class WifiDirectBroadcastReceiver(
        val manager: WifiP2pManager,
        val channel: WifiP2pManager.Channel,
        activity: Activity
) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val action: String = intent.action!!

        when (action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                // Check to see if Wi-Fi is enabled and notify appropriate activity
            }

            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
// Call WifiP2pManager.requestPeers() to get a list of current peers

            }
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {

                // Respond to new connection or disconnections

            }

            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {

                // Respond to this device's wifi state changing


            }


        }


    }
}