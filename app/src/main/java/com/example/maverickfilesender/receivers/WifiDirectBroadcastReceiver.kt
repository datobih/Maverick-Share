package com.example.maverickfilesender.receivers

import android.Manifest
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maverickfilesender.R
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.adapters.DevicePeerListRecyclerAdapter
import com.example.maverickfilesender.constants.Constants
import kotlinx.android.synthetic.main.dialog_hotspot_receiver.*

class WifiDirectBroadcastReceiver(val manager:WifiP2pManager,val channel:WifiP2pManager.Channel,val activity: MainActivity):BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {


        when (intent!!.action){

            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION->{
               val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1)

                if(state==WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                    //P2P ENABLED
                    Toast.makeText(activity,"P2P CONNECTED",Toast.LENGTH_SHORT).show()





                }

                else{
                //P2P DISABLED
                    Toast.makeText(activity,"P2P DISCONNECTED",Toast.LENGTH_SHORT).show()

                }


            }
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION->{

                if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && !(Constants.isServer)) {
                          manager.requestPeers(channel,object :WifiP2pManager.PeerListListener{
                        override fun onPeersAvailable(peerList: WifiP2pDeviceList?) {
                            val devices = ArrayList<WifiP2pDevice>(peerList!!.deviceList)
                            if ((context as MainActivity).p2pDevices != devices) {
                                (context as MainActivity).p2pDevices=devices



                                val dialog = Dialog(context!!)
                                dialog.setContentView(R.layout.dialog_hotspot_receiver)
                                val adapter = DevicePeerListRecyclerAdapter(context, devices)
                                dialog.rv_receiver_ssid.layoutManager = LinearLayoutManager(context)

                                dialog.rv_receiver_ssid.adapter = adapter

                                dialog.show()
                            }
                        }

                    })
                }



            }
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION->{

                val networkInfo=intent.getParcelableExtra<NetworkInfo>(WifiP2pManager.EXTRA_NETWORK_INFO)


if(networkInfo!!.isConnected){


    manager.requestConnectionInfo(channel,object :WifiP2pManager.ConnectionInfoListener{
        override fun onConnectionInfoAvailable(info: WifiP2pInfo?) {

            val groupOwnerAddress=info!!.groupOwnerAddress.hostAddress

            if(info.groupFormed && info.isGroupOwner){

Toast.makeText(context,"THIS IS GROUP OWNER",Toast.LENGTH_SHORT).show()

            }
            else if(info.groupFormed){

                Toast.makeText(context,"THIS IS GROUP MEMBER",Toast.LENGTH_SHORT).show()
            }


        }


    })
}





            }
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION->{

            }


        }



    }







}