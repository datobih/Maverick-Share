package com.example.maverickfilesender.receivers

import android.Manifest
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.NetworkInfo
import android.net.wifi.p2p.*
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maverickfilesender.R
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.adapters.DevicePeerListRecyclerAdapter
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.handlers.ClientThread
import com.example.maverickfilesender.handlers.ServerThread
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.dialog_hotspot_receiver.*
import java.net.ServerSocket

class WifiDirectBroadcastReceiver(val manager:WifiP2pManager,val channel:WifiP2pManager.Channel,val activity: MainActivity):BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {


        when (intent!!.action){

            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION->{
               val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1)

                if(state==WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                    //P2P ENABLED
                   Log.d("P2PP","Connected")





                }

                else{
                //P2P DISABLED
                    Log.d("P2PP","Disconnected")

                }


            }
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION->{

                if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && !(Constants.isServer)) {
                          manager.requestPeers(channel,object :WifiP2pManager.PeerListListener{
                        override fun onPeersAvailable(peerList: WifiP2pDeviceList?) {
                            val devices = ArrayList<WifiP2pDevice>(peerList!!.deviceList)

//                            if ((context as MainActivity).p2pDevices != devices) {

                            if(Constants.scanDevices&& devices.isNotEmpty()) {
                                    Constants.scanDevices=false
                                (context as MainActivity).p2pDevices = devices


                                val dialog = Dialog(context!!)
                                dialog.setContentView(R.layout.dialog_hotspot_receiver)
                                val adapter = DevicePeerListRecyclerAdapter(context, devices)
                                dialog.rv_receiver_ssid.layoutManager = LinearLayoutManager(context)

                                dialog.rv_receiver_ssid.adapter = adapter


                                dialog.show()
                            }


//                            }
                        }

                    })
                }



            }
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION->{

                val networkInfo=intent.getParcelableExtra<NetworkInfo>(WifiP2pManager.EXTRA_NETWORK_INFO)
val groupList=intent.getParcelableExtra<WifiP2pGroup>(WifiP2pManager.EXTRA_WIFI_P2P_GROUP)

if(networkInfo!!.isConnected){


    manager.requestConnectionInfo(channel,object :WifiP2pManager.ConnectionInfoListener{



        override fun onConnectionInfoAvailable(info: WifiP2pInfo?) {
            Constants.p2pConnected=true
Toast.makeText(context,"Connected to ${Constants.connectedDevice}",Toast.LENGTH_SHORT).show()
            val groupOwnerAddress=info!!.groupOwnerAddress.hostAddress

            if(info.groupFormed && info.isGroupOwner){

                if(Constants.serverThread==null) {
                    Constants.serverThread = ServerThread(context!!)
                    Constants.serverThread!!.start()
                }

Toast.makeText(context,"THIS IS GROUP OWNER",Toast.LENGTH_SHORT).show()

            }
            else if(info.groupFormed){

                if(Constants.clientThread==null) {
                    Constants.clientThread = ClientThread(context!!,groupOwnerAddress)
                    Constants.clientThread!!.start()
                }

                Toast.makeText(context,"THIS IS GROUP MEMBER",Toast.LENGTH_SHORT).show()
            }
else{

                Toast.makeText(context,"COULDNT CONNECT",Toast.LENGTH_SHORT).show()

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