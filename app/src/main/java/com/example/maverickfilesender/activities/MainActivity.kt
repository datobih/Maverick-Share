package com.example.maverickfilesender.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maverickfilesender.R
import com.example.maverickfilesender.adapters.AppPackageRecyclerAdapter
import com.example.maverickfilesender.adapters.MainPagerFragmentAdapter
import com.example.maverickfilesender.adapters.SSIDListRecyclerAdapter
import com.example.maverickfilesender.constants.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_hotspot_receiver.*
import kotlinx.android.synthetic.main.dialog_hotspot_sender.*
import java.io.File

class MainActivity : AppCompatActivity() {
    var ssid:String=""
    var password:String=""
    var mReservation:WifiManager.LocalOnlyHotspotReservation?=null
    var connectionType:String=""

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



val adapter=MainPagerFragmentAdapter(supportFragmentManager,lifecycle)
        vp_main.adapter=adapter
        vp_main.isUserInputEnabled=false


tab_main.addTab(tab_main.newTab().setText("Apps"))
        tab_main.addTab(tab_main.newTab().setText("Media"))
        tab_main.addTab(tab_main.newTab().setText("Files"))



        tab_main.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                vp_main.currentItem=tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }


        })

        /*
bottomNavigation_main.setOnNavigationItemSelectedListener(object:BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onNavigationItemSelected(item: MenuItem): Boolean {


        when(item.itemId){

            R.id.nav_apps-> {

                vp_main.currentItem = 0
return true
            }
            R.id.nav_media-> {
                vp_main.currentItem = 1
            return true
            }


            else -> {
                vp_main.currentItem = 2
                return true
            }
            }
        return false
    }


})

         */

btn_connect_status.setOnClickListener {


    if(connectionType==Constants.CONNECTION_TYPE_HOTSPOT){
        mReservation?.close()


        btn_connect_status.visibility= View.GONE
        btn_send.visibility=View.VISIBLE
        btn_receive.visibility=View.VISIBLE
    }

    else if(connectionType==Constants.CONNECTION_TYPE_WIFI){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {

            val connectivityManager=getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.unregisterNetworkCallback(object:ConnectivityManager.NetworkCallback(){

            })
        }


        else{
            val wifiManager= applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager


            wifiManager.setWifiEnabled(false)

            btn_connect_status.visibility= View.GONE
            btn_send.visibility=View.VISIBLE
            btn_receive.visibility=View.VISIBLE
        }





    }

}


        btn_send.setOnClickListener {

            if(verifyLocation()){

                if(isLocationEnabled()) {


                    val wifimanager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        wifimanager.startLocalOnlyHotspot(object : WifiManager.LocalOnlyHotspotCallback() {

                            override fun onStarted(reservation: WifiManager.LocalOnlyHotspotReservation?) {
                                super.onStarted(reservation)
connectionType=Constants.CONNECTION_TYPE_HOTSPOT
                                mReservation = reservation
                                ssid = reservation!!.wifiConfiguration!!.SSID
                                password = reservation!!.wifiConfiguration!!.preSharedKey



                                val dialog = Dialog(this@MainActivity)
                                dialog.setContentView(R.layout.dialog_hotspot_sender)
                                dialog.tv_hotspot_SSID.text = ssid
                                dialog.tv_hotspot_password.text = password
                                dialog.show()

                                dialog.setOnDismissListener {

                                    mReservation?.close()

                                }
                            }

                        }, null)

                    }



                    

                }

                else{

                    Toast.makeText(this,"Your location needs to be turned on",Toast.LENGTH_SHORT).show()
                    var intent=Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)

                }
            }
            else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

                    var intent= Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    var uri= Uri.fromParts("package",packageName,null)
                    intent.setData(uri)
                    startActivity(intent)

                }
else{
    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),Constants.RQ_LOCATION_PERMISSION)
                }


            }


        }


        btn_receive.setOnClickListener {

            if(verifyLocation()){

val wifiManager=getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
val scanResult=wifiManager.scanResults
val receiversResult=ArrayList<ScanResult>()
                for(i in scanResult){

                    Log.i("WIFII",i.SSID)

//if(i.SSID.contains("AndroidShare_")){
receiversResult.add(i)
//}


                }


    if(receiversResult.isNotEmpty()){

val dialog=Dialog(this)
dialog.setContentView(R.layout.dialog_hotspot_receiver)
                    val adapter=SSIDListRecyclerAdapter(this,receiversResult)

dialog.rv_receiver_ssid.layoutManager=LinearLayoutManager(this)
        dialog.rv_receiver_ssid.setHasFixedSize(true)

        dialog.rv_receiver_ssid.adapter=adapter
                    dialog.show()


               }


            }

            else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

                    var intent= Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    var uri= Uri.fromParts("package",packageName,null)
                    intent.setData(uri)
                    startActivity(intent)

                }
                else{
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),Constants.RQ_LOCATION_PERMISSION)
                }

            }



        }


    }


    fun isLocationEnabled():Boolean{

val locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager

return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)||locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }


    fun verifyLocation():Boolean{
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){

            return true

        }
        return false


    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    if(requestCode==Constants.RQ_READ_WRITE_PERMISSION){

        if(grantResults[0]==PackageManager.PERMISSION_GRANTED &&grantResults[1]==PackageManager.PERMISSION_GRANTED){

            Toast.makeText(this,"Your permission is granted",Toast.LENGTH_SHORT).show()

        }

        else if(requestCode==Constants.RQ_LOCATION_PERMISSION){

            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){

                Toast.makeText(this,"Your permission is granted",Toast.LENGTH_SHORT).show()
            }

        }


    }

    }


}