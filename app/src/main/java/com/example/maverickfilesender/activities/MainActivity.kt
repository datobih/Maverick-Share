package com.example.maverickfilesender.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.format.Formatter
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maverickfilesender.R
import com.example.maverickfilesender.adapters.MainPagerFragmentAdapter
import com.example.maverickfilesender.adapters.SSIDListRecyclerAdapter
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.fragment.*
import com.example.maverickfilesender.handlers.ClientThread
import com.example.maverickfilesender.handlers.ServerThread
import com.example.maverickfilesender.model.FileMetaData
import com.example.maverickfilesender.receivers.WifiReceiver
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.dialog_hotspot_receiver.*
import kotlinx.android.synthetic.main.dialog_hotspot_sender.*
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    var ssid: String = ""
    var password: String = ""
    var mReservation: WifiManager.LocalOnlyHotspotReservation? = null
    var connectionType: String = ""
    var mIpAddress: String = ""
    var clientSocketThread: Thread? = null
    var serverSocketThread: Thread? = null
    var isClientConnected = false
    var mDialog: Dialog? = null
    var mHandler: Handler? = null
    var mNetworkSSID = ""
    var shouldScan=false
    var animationMoveUp:Animation?=null
    var transitionDown:Animation?=null
    var imageFragment:ImageFragment?=null
    var appsFragment:AppsFragment?=null
    var videosFragment:VideosFragment?=null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MaverickFileSender)
        setContentView(R.layout.activity_main)

//
//
//val fileDir=Constants.inStoragePath
//        val Testdir=File(fileDir+"/DCIM/Test")
//       val valid= Testdir!!.mkdirs()
//

Constants.mainActivity=this

        imv_drawer.setOnClickListener {

            if(!main_drawerLayout.isDrawerOpen(GravityCompat.START)){
                main_drawerLayout.openDrawer(GravityCompat.START)

            }else{
                main_drawerLayout.closeDrawer(GravityCompat.START)
            }


        }

        mHandler = Handler(Looper.getMainLooper())
        var receiver = WifiReceiver()

val mIntentFilter=IntentFilter()
        mIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        mIntentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)
registerReceiver(receiver,mIntentFilter)

        registerReceiver(receiver, IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION))

        registerReceiver(receiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))

        val adapter = MainPagerFragmentAdapter(supportFragmentManager, lifecycle)

        vp_main.adapter = adapter
        vp_main.offscreenPageLimit=3
        vp_main.isUserInputEnabled = false

        val appTab = tab_main.newTab().setText("Apps").setIcon(R.drawable.ic_baseline_android_24)
        val historyTab=tab_main.newTab().setText("History").setIcon(R.drawable.ic_baseline_history_24)
        tab_main.addTab(historyTab)
        tab_main.addTab(appTab)
        tab_main.addTab(tab_main.newTab().setText("Images").setIcon(R.drawable.ic_outline_image_24))
        tab_main.addTab(tab_main.newTab().setText("Videos").setIcon(R.drawable.ic_outline_video_library_24))
        tab_main.addTab(tab_main.newTab().setText("Files").setIcon(R.drawable.ic_baseline_folder_24))
//
//        tab_main.selectTab(appTab)
//        vp_main.currentItem=1


        val receiveAnimation=AnimationUtils.loadAnimation(this,R.anim.spawn_recieve)
        val sendAnimation=AnimationUtils.loadAnimation(this,R.anim.spawn_send)
         transitionDown=AnimationUtils.loadAnimation(this,R.anim.transition_down)
        animationMoveUp = AnimationUtils.loadAnimation(this,R.anim.transition_up)
        val bounceAnimation=AnimationUtils.loadAnimation(this,R.anim.bounce_loop)
        val transferAnimation=AnimationUtils.loadAnimation(this,R.anim.righttoleft)



        mHandler!!.postDelayed(Runnable {      btn_receiver.startAnimation(receiveAnimation)
            btn_sender.startAnimation(sendAnimation)

        },400)



        drawer_navigation.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener {
            when(it.itemId){
               R.id.d_menu_settings->{


                   return@OnNavigationItemSelectedListener true
               }

                else->{


                    return@OnNavigationItemSelectedListener true
                }

            }


        })

        ll_transfering.setOnClickListener {
var intent=Intent(this,TransferActivity::class.java)
            intent.putExtra(Constants.TRANSFER_EXTRA,connectionType)

            startActivityForResult(intent,3)

        }


        btn_send.setOnClickListener {
            val list=ArrayList<FileMetaData>()
            for(i in Constants.tempSelectedFiles){
                list.add(FileMetaData(i.file.name,i.file.length(), BitmapFactory.decodeByteArray(i.data,0,i.data!!.size)))
            }

            Constants.onSelectedMetaData=list

            Constants.selectedFiles.addAll(Constants.tempSelectedFiles)





            Constants.tempSelectedFiles.clear()

ll_transfering.visibility=View.VISIBLE
            ll_transfering.startAnimation(transferAnimation)
            imv_downloading.startAnimation(bounceAnimation)

            for(i in Constants.selectedFiles)   Log.i("SendingFiles",i.file.name)

//Constants.selectedFiles.clear()
Constants.heirarchyFiles.clear()
Constants.sendCount=0




                    while (Constants.imagesSelected.isNotEmpty()) {

                        imageFragment!!.adapter!!.imageList[Constants.imagesSelected[0]].onSelect=false
                        imageFragment!!.adapter!!.notifyItemChanged(Constants.imagesSelected[0])
                        Constants.imagesSelected.removeAt(0)
                    }




            while (Constants.appSelected.isNotEmpty()){

                appsFragment!!.adapter!!.appPackagePackageList[Constants.appSelected[0]].onSelect=false
                appsFragment!!.adapter!!.notifyItemChanged(Constants.appSelected[0])
                Constants.appSelected.removeAt(0)


            }

            while(Constants.videosSelected.isNotEmpty()){



                videosFragment!!.adapter!!.videoList[Constants.videosSelected[0]].onSelect=false
                videosFragment!!.adapter!!.notifyItemChanged(Constants.videosSelected[0])
                Constants.videosSelected.removeAt(0)

            }


            supportFragmentManager.beginTransaction().apply {

                replace(R.id.holder_files_fragment,StorageDirectoryFragment())
                    .commit()



            }


if(ll_main_send.visibility==View.VISIBLE){

    ll_main_send.startAnimation(transitionDown)
    ll_main_send.visibility=View.INVISIBLE

}
tab_main.selectTab(historyTab)
        }


        btn_send_close.setOnClickListener {
ll_main_send.startAnimation(transitionDown)
            ll_main_send.visibility=View.INVISIBLE


        }

        tab_main.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                vp_main.currentItem = tab!!.position


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

//        btn_connect_status.setOnClickListener {
//
//
//            if (connectionType == Constants.CONNECTION_TYPE_HOTSPOT) {
//                mReservation?.close()
//
//
//                btn_connect_status.visibility = View.GONE
//                btn_send.visibility = View.VISIBLE
//                btn_receive.visibility = View.VISIBLE
//            } else if (connectionType == Constants.CONNECTION_TYPE_WIFI) {
//
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//
//                    val connectivityManager =
//                        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
///*     connectivityManager.unregisterNetworkCallback(object:ConnectivityManager.NetworkCallback(){
//
//            })
//
//
// */
//                } else {
//                    val wifiManager =
//                        applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//
//
//                    wifiManager.setWifiEnabled(false)
//
//                    btn_connect_status.visibility = View.GONE
//                    btn_send.visibility = View.VISIBLE
//                    btn_receive.visibility = View.VISIBLE
//                }
//
//
//            }
//
//        }


        btn_sender.setOnClickListener {

            if (verifyLocation()) {

                if (isLocationEnabled()) {

if(mReservation!=null){
    mReservation!!.close()
    Thread.sleep(3000)
    mReservation=null
    Constants.serverThread!!.serverSocket!!.close()
}
                    val wifimanager =
                        applicationContext.getSystemService(WIFI_SERVICE) as WifiManager

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        wifimanager.startLocalOnlyHotspot(object :
                            WifiManager.LocalOnlyHotspotCallback() {

                            override fun onStarted(reservation: WifiManager.LocalOnlyHotspotReservation?) {
                                super.onStarted(reservation)
                                connectionType = Constants.CONNECTION_TYPE_HOTSPOT
                                Constants.serverThread = ServerThread(this@MainActivity)
                                Constants.serverThread!!.start()


                                mReservation = reservation
                                ssid = reservation!!.wifiConfiguration!!.SSID
                                password = reservation!!.wifiConfiguration!!.preSharedKey


                                val dialog = Dialog(this@MainActivity)
                                dialog.setContentView(R.layout.dialog_hotspot_sender)
                                dialog.tv_hotspot_SSID.text = ssid
                                dialog.tv_hotspot_password.text = password


                                    dialog.show()


                            }

                        }, null)

                    } else {

                        val intent = Intent()
                        intent.setClassName(
                            "com.android.settings",
                            "com.android.settings.TetherSettings"
                        )
                        startActivityForResult(intent, 0)
                    }


                } else {

                    Toast.makeText(this, "Your location needs to be turned on", Toast.LENGTH_SHORT)
                        .show()
                    var intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)

                }
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {

                    var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    var uri = Uri.fromParts("package", packageName, null)
                    intent.setData(uri)
                    startActivity(intent)

                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        Constants.RQ_LOCATION_PERMISSION
                    )
                }


            }


        }


        btn_receiver.setOnClickListener {

            if (verifyLocation()) {

                val wifiManager =
                    applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                shouldScan=true
wifiManager.startScan()



            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {

                    var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    var uri = Uri.fromParts("package", packageName, null)
                    intent.setData(uri)
                    startActivity(intent)

                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        Constants.RQ_LOCATION_PERMISSION
                    )
                }

            }


        }


    }

//    fun setupFont() {
//
//
//        tv_connection_status.typeface = gilroyTypeface
//        tv_toolbar_userName.typeface = gilroyTypeface
//        btn_receive.typeface=gilroyLightTypeface
//        btn_send.typeface=gilroyLightTypeface
//
//    }


fun setupUIconnected(){
    btn_receiver.visibility=View.GONE
    btn_sender.visibility=View.GONE
btn_disconnect.visibility=View.VISIBLE


}



    fun isLocationEnabled(): Boolean {

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.GPS_PROVIDER
        )
    }


    fun verifyLocation(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            return true

        }
        return false


    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constants.RQ_READ_WRITE_PERMISSION) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Your permission is granted", Toast.LENGTH_SHORT).show()

            } else if (requestCode == Constants.RQ_LOCATION_PERMISSION) {

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Your permission is granted", Toast.LENGTH_SHORT).show()
                }

            }


        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


    }

    override fun onPause() {
        super.onPause()


    }

    override fun onResume() {


//        if (Constants.onNetworkAvailable) {
//         mHandler!!.postDelayed({
//             Constants.onNetworkAvailable = false
//             var wifiManager =
//                     getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
//             if (wifiManager.isWifiEnabled) {
//
//                 val wifiInfo = wifiManager.connectionInfo
//
//                     if (wifiInfo.ssid == "\"${Constants.mNetworkSSID}\"") {
//                         isClientConnected = true
//                         mIpAddress = Formatter.formatIpAddress(wifiManager.dhcpInfo.serverAddress)
//                         Constants.clientThread = ClientThread(this)
//                         Constants.clientThread!!.start()
//                         btn_receiver.visibility = View.GONE
//                         btn_send.visibility = View.GONE
//
////                    btn_connect_status.visibility = View.VISIBLE
//                         connectionType = Constants.CONNECTION_TYPE_WIFI
//                     }
//
//
//             }},1000)
//
//        }

        super.onResume()
    }

    override fun onBackPressed() {
        val f=supportFragmentManager.findFragmentById(R.id.holder_files_fragment)
if(Constants.mRelativePath.size>1 && vp_main.currentItem==4){



    if(f is FilesDirectoryFragment){


        f.onFragmentBackPressed(this)

    }



}

        else {

     if(Constants.mRelativePath.size==1 && vp_main.currentItem==4){

if( f is FilesDirectoryFragment)
f.onFinalStack(this)


    }


    super.onBackPressed()
}


    }


}