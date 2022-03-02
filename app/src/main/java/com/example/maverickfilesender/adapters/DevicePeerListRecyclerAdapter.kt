package com.example.maverickfilesender.adapters

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WpsInfo
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.maverickfilesender.R
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.constants.Constants
import kotlinx.android.synthetic.main.item_ssid.view.*

class DevicePeerListRecyclerAdapter(val context:Context,val deviceList:ArrayList<WifiP2pDevice>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SSIDListRecyclerAdapter.SSIDViewHolder(LayoutInflater.from(context).inflate(R.layout.item_ssid, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val manager=   (context as MainActivity).p2pManager
        val channel=   (context as MainActivity).p2pChannel
        holder.itemView.tv_item_ssid.text =deviceList[position].deviceName


        holder.itemView.btn_item_connect.setOnClickListener {
//            openWifiLoginInput(holder)

val device=deviceList[position]


            val config=WifiP2pConfig().apply {
                deviceAddress=device.deviceAddress
                groupOwnerIntent=0
                wps.setup=WpsInfo.PBC


            }


            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

Thread.sleep(3000)

                manager!!.connect(channel,config,object:WifiP2pManager.ActionListener{
                    override fun onSuccess() {
                       Constants.connectedDevice=device.deviceName

                        Thread {
                            var i = 0
                            while (i < 10) {
                                Thread.sleep(1000)
                                i++
                            }

                            if(!Constants.p2pConnected){
                                (context as MainActivity).runOnUiThread {
                                    Toast.makeText(
                                            context,
                                            "Connection failed.",
                                            Toast.LENGTH_SHORT
                                    ).show()
                                    manager!!.cancelConnect(channel,object:WifiP2pManager.ActionListener{
                                        @SuppressLint("MissingPermission")
                                        override fun onSuccess() {

                                            manager!!.discoverPeers(channel,object:WifiP2pManager.ActionListener{
                                                override fun onSuccess() {

                                                }

                                                override fun onFailure(p0: Int) {

                                                }


                                            })


                                        }

                                        override fun onFailure(p0: Int) {

                                        }


                                    })
                                }


                            }

                            else{


                            }
                            Constants.p2pConnected=false
                        }.start()



                    }

                    override fun onFailure(p0: Int) {

                        Toast.makeText(
                              context,
                                "Connect failed. Retry.",
                                Toast.LENGTH_SHORT
                        ).show()
                    }

                })
            }





        }

//        holder.itemView.imb_item_ssid_close.setOnClickListener {
//
//            closeWifiLoginInput(holder)
//
//        }


    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

fun connect(){






}

    fun openWifiLoginInput(ssidViewHolder: RecyclerView.ViewHolder) {

        ssidViewHolder.itemView.btn_item_connect.visibility = View.GONE
        ssidViewHolder.itemView.imb_item_ssid_close.visibility = View.VISIBLE
        ssidViewHolder.itemView.view_item_ssid_divider.visibility = View.VISIBLE
        ssidViewHolder.itemView.et_wifi_password.visibility = View.VISIBLE
        ssidViewHolder.itemView.btn_itemPassword_connect.visibility = View.VISIBLE

    }

    fun closeWifiLoginInput(ssidViewHolder: RecyclerView.ViewHolder) {
        ssidViewHolder.itemView.imb_item_ssid_close.visibility = View.GONE
        ssidViewHolder.itemView.btn_item_connect.visibility = View.VISIBLE
        ssidViewHolder.itemView.view_item_ssid_divider.visibility = View.GONE
        ssidViewHolder.itemView.et_wifi_password.text?.clear()
        ssidViewHolder.itemView.et_wifi_password.visibility = View.GONE
        ssidViewHolder.itemView.btn_itemPassword_connect.visibility = View.GONE
    }

}


