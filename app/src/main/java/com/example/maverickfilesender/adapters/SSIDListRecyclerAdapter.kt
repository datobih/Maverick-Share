package com.example.maverickfilesender.adapters

import android.content.Context
import android.net.*
import android.net.wifi.ScanResult
import android.net.wifi.WifiNetworkSpecifier
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.maverickfilesender.R
import kotlinx.android.synthetic.main.item_ssid.view.*

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

                    connectToNetwork(scanResults[position].SSID, scanResults[position].BSSID, inputtedPassword)

                } else {

                    Toast.makeText(context, "You did not provide any input", Toast.LENGTH_SHORT).show()

                }


            }


        }


    }

    fun connectToNetwork(networkSSID: String, networkBSSID: String, networkPassword: String) {

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
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

                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        Log.i("WIFII", "onLost" + network)
                    }

                    override fun onUnavailable() {
                        super.onUnavailable()

                        Log.i("WIFII", "onUnavailable")
                    }

                }

                connectivityManager.requestNetwork(networkRequest, networkCallback)

            } else {
                TODO("VERSION.SDK_INT < Q")
            }
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