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
import com.example.maverickfilesender.databinding.ItemSsidBinding

class DevicePeerListRecyclerAdapter(val context:Context,val deviceList:ArrayList<WifiP2pDevice>):RecyclerView.Adapter<DevicePeerListRecyclerAdapter.DeviceViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val binding = ItemSsidBinding.inflate(LayoutInflater.from(context), parent, false)
        return DeviceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val manager=   (context as MainActivity).p2pManager
        val channel=   (context as MainActivity).p2pChannel
        holder.binding.tvItemSsid.text =deviceList[position].deviceName


        holder.binding.btnItemConnect.setOnClickListener {
//            openWifiLoginInput(holder)
val mainContext=context as MainActivity

            mainContext.showLoading()
            Constants.ssidDialog!!.dismiss()
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
                                    mainContext.hideLoading()
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

    fun openWifiLoginInput(ssidViewHolder: DeviceViewHolder) {

        ssidViewHolder.binding.btnItemConnect.visibility = View.GONE
        ssidViewHolder.binding.imbItemSsidClose.visibility = View.VISIBLE
        ssidViewHolder.binding.viewItemSsidDivider.visibility = View.VISIBLE
        ssidViewHolder.binding.etWifiPassword.visibility = View.VISIBLE
        ssidViewHolder.binding.btnItemPasswordConnect.visibility = View.VISIBLE

    }

    fun closeWifiLoginInput(ssidViewHolder: DeviceViewHolder) {
        ssidViewHolder.binding.imbItemSsidClose.visibility = View.GONE
        ssidViewHolder.binding.btnItemConnect.visibility = View.VISIBLE
        ssidViewHolder.binding.viewItemSsidDivider.visibility = View.GONE
        ssidViewHolder.binding.etWifiPassword.text?.clear()
        ssidViewHolder.binding.etWifiPassword.visibility = View.GONE
        ssidViewHolder.binding.btnItemPasswordConnect.visibility = View.GONE
    }

    class DeviceViewHolder(val binding: ItemSsidBinding) : RecyclerView.ViewHolder(binding.root)

}


