package com.example.maverickfilesender.adapters

import android.content.Context
import android.net.*
import android.net.wifi.*
import android.opengl.Visibility
import android.os.Looper
import android.text.format.Formatter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.maverickfilesender.R
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.handlers.ClientThread
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.item_ssid.view.*
import java.util.logging.Handler

class SSIDListRecyclerAdapter(val context: Context, val scanResults: ArrayList<ScanResult>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SSIDViewHolder(LayoutInflater.from(context).inflate(R.layout.item_ssid, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is SSIDViewHolder) {

            holder.itemView.tv_item_ssid.text = scanResults[position].SSID
            holder.itemView.btn_item_connect.setOnClickListener {
                openWifiLoginInput(holder)

            }

            holder.itemView.imb_item_ssid_close.setOnClickListener {

                closeWifiLoginInput(holder)

            }


            holder.itemView.btn_itemPassword_connect.setOnClickListener {


                val inputtedPassword = holder.itemView.et_wifi_password.text.toString()

                if (inputtedPassword.isNotEmpty()) {

                    connectThread(scanResults[position].SSID, scanResults[position].BSSID, inputtedPassword)

                } else {

                    Toast.makeText(context, "You did not provide any input", Toast.LENGTH_SHORT).show()

                }


            }


        }


    }

    fun connectThread(networkSSID: String, networkBSSID: String, networkPassword: String){


                connectToNetwork(networkSSID,networkBSSID,networkPassword)






    }

    fun connectToNetwork(networkSSID: String, networkBSSID: String, networkPassword: String) {
       Constants.mNetworkSSID=networkSSID
        Constants.ssidDialog?.dismiss()
        (context as MainActivity).ll_loading.visibility=View.VISIBLE
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                (context as MainActivity).isClientConnected=true
                Constants.onNetworkAvailable=true
                val wifiNetworkSpecifier = WifiNetworkSpecifier.Builder()
                        .setSsid(networkSSID)

                        .setWpa2Passphrase(networkPassword)
                        .build()


                val networkRequest = NetworkRequest.Builder()
                        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                        .setNetworkSpecifier(wifiNetworkSpecifier)
                        .build()


                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkCallback = object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {

                        Log.i("WIFII", "onAvailable" + network)

                        super.onAvailable(network)

                        connectivityManager.bindProcessToNetwork(network)






context.runOnUiThread {

//    (context as MainActivity).setupUIconnected()


//    context.btn_connect_status.visibility=View.VISIBLE
    context.connectionType=Constants.CONNECTION_TYPE_WIFI


}



                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        Log.i("WIFII", "onLost" + network)
                    }

                    override fun onUnavailable() {
                        super.onUnavailable()
Constants.noNetwork=true
                        Log.i("WIFII", "onUnavailable")
                        (context as MainActivity).ll_loading.visibility=View.GONE
                    }

                }

                connectivityManager.requestNetwork(networkRequest, networkCallback)


            } else {
                Constants.isReconnected=null
                val wifiConfiguration=WifiConfiguration()
                wifiConfiguration.SSID="\"${networkSSID}\""
                wifiConfiguration.preSharedKey="\"${networkPassword}\""

                wifiConfiguration.status=WifiConfiguration.Status.ENABLED
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP)
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP)
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.KeyMgmt.WPA_PSK)
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.PairwiseCipher.TKIP)
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.PairwiseCipher.CCMP)

                Log.i("WIFII Connecting",wifiConfiguration.SSID+" "+wifiConfiguration.preSharedKey)

                val wifiManager=context.getSystemService(Context.WIFI_SERVICE) as WifiManager
                Constants.onNetworkAvailable=true
                val netID=wifiManager.addNetwork(wifiConfiguration)


//wifiManager.dhcpInfo.ipAddress IMPORTANT




                wifiManager.disconnect()
                wifiManager.enableNetwork(netID,true)
      wifiManager.reconnect()



//
//
//                if(wifiManager.isWifiEnabled){
//
//                    val wifiInfo=wifiManager.connectionInfo
//                    if(wifiInfo.ssid=="\"${networkSSID}\""){
//
//                    }
//                    else{
//                        Constants.noNetwork=true
//                    }
//
//                }

            }
//            if(context is MainActivity) {
//                if (context.isClientConnected == true) {
//
//                  Constants.clientThread= ClientThread(context)
//                    Constants.clientThread!!.start()
//
//
//                }
//            }

        } catch (e: Exception) {

        }


    }


    fun openWifiLoginInput(ssidViewHolder: SSIDViewHolder) {

        ssidViewHolder.itemView.btn_item_connect.visibility = View.GONE
        ssidViewHolder.itemView.imb_item_ssid_close.visibility = View.VISIBLE
        ssidViewHolder.itemView.view_item_ssid_divider.visibility = View.VISIBLE
        ssidViewHolder.itemView.et_wifi_password.visibility = View.VISIBLE
        ssidViewHolder.itemView.btn_itemPassword_connect.visibility = View.VISIBLE

    }

    fun closeWifiLoginInput(ssidViewHolder: SSIDViewHolder) {
        ssidViewHolder.itemView.imb_item_ssid_close.visibility = View.GONE
        ssidViewHolder.itemView.btn_item_connect.visibility = View.VISIBLE
        ssidViewHolder.itemView.view_item_ssid_divider.visibility = View.GONE
        ssidViewHolder.itemView.et_wifi_password.text?.clear()
        ssidViewHolder.itemView.et_wifi_password.visibility = View.GONE
        ssidViewHolder.itemView.btn_itemPassword_connect.visibility = View.GONE
    }


    override fun getItemCount(): Int {
        return scanResults.size
    }


    class SSIDViewHolder(view: View) : RecyclerView.ViewHolder(view)

}