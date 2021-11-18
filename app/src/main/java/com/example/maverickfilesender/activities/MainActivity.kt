package com.example.maverickfilesender.activities

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maverickfilesender.R
import com.example.maverickfilesender.adapters.AppPackageRecyclerAdapter
import com.example.maverickfilesender.adapters.SSIDListRecyclerAdapter
import com.example.maverickfilesender.constants.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_hotspot_receiver.*
import kotlinx.android.synthetic.main.dialog_hotspot_sender.*
import java.io.File

class MainActivity : AppCompatActivity() {
    var ssid:String=""
    var password:String=""
    var mReservation:WifiManager.LocalOnlyHotspotReservation?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){


            val pm=packageManager

            val packages=pm.getInstalledApplications(PackageManager.GET_META_DATA)
val nonSystemPackages=ArrayList<ApplicationInfo>()
            for(i:ApplicationInfo in packages){

                if(i.sourceDir.startsWith("/data/app/")){

                    nonSystemPackages.add(i)

                }


            }

rv_apps.layoutManager=GridLayoutManager(this,4)

            val adapter=AppPackageRecyclerAdapter(this,nonSystemPackages)

            rv_apps.adapter=adapter

        }

        else{

if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)||
ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){

    var intent= Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    var uri= Uri.fromParts("package",packageName,null)
    intent.setData(uri)
    startActivity(intent)



}


            else{
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE),Constants.RQ_READ_WRITE_PERMISSION)



            }



        }




        send.setOnClickListener {

            if(verifyLocation()){

                if(isLocationEnabled()) {


                    val wifimanager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        wifimanager.startLocalOnlyHotspot(object : WifiManager.LocalOnlyHotspotCallback() {

                            override fun onStarted(reservation: WifiManager.LocalOnlyHotspotReservation?) {
                                super.onStarted(reservation)

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


        receive.setOnClickListener {

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