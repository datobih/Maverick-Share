package com.example.maverickfilesender.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.example.maverickfilesender.R
import com.example.maverickfilesender.adapters.MainPagerFragmentAdapter
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.fragment.*
import com.example.maverickfilesender.model.FileMetaData
import com.example.maverickfilesender.receivers.WifiDirectBroadcastReceiver
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.dialog_hotspot_receiver.*
import kotlinx.android.synthetic.main.dialog_hotspot_sender.*
import kotlinx.android.synthetic.main.drawer_header.*
import kotlinx.android.synthetic.main.drawer_header.view.*
import kotlin.reflect.typeOf

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
    var musicFragment:MusicFragment?=null

var wifiManager:WifiManager?=null
    var mSharedPreferences : SharedPreferences?=null

    var mProfilePicEncoded:String?=""
    var mProfileName:String?=""
    var profileBitmap: Bitmap?=null

    var navUserImage:ImageView?=null
    var navUserName:TextView?=null

    var p2pManager:WifiP2pManager?=null
    var p2pChannel:WifiP2pManager.Channel?=null
var p2pReceiver:WifiDirectBroadcastReceiver?=null
    var p2pIntentFilter:IntentFilter?=null
    var p2pDevices=ArrayList<WifiP2pDevice>()


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

        val headerView=drawer_navigation.getHeaderView(0)
         navUserImage=headerView.findViewById<ImageView>(R.id.drawer_user_profile_pic)
         navUserName=headerView.findViewById<TextView>(R.id.tv_main_userName)

        mSharedPreferences= getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        val isDarkMode=mSharedPreferences!!.getBoolean(Constants.SP_DARK_MODE,true)

if(!isDarkMode){
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
}


Constants.isDarkMode=isDarkMode

        mProfilePicEncoded=mSharedPreferences!!.getString(Constants.SP_PROFILE_PIC_DATA,"")
        mProfileName=mSharedPreferences!!.getString(Constants.SP_PROFILE_USERNAME,"")
var historyLocation=mSharedPreferences!!.getString(Constants.SP_HISTORY_LOCATION,"")

        if(!historyLocation.isNullOrEmpty()){

            Constants.currentHistoryLocation=historyLocation

        }


        if(!mProfilePicEncoded.isNullOrEmpty()&& mProfilePicEncoded!="pic"){



            val userPictureByteArray= Base64.decode(mProfilePicEncoded, Base64.DEFAULT)
            profileBitmap=BitmapFactory.decodeByteArray(userPictureByteArray,0,userPictureByteArray.size)

navUserImage!!.setImageBitmap(profileBitmap)

        }else if(mProfilePicEncoded.isNullOrEmpty()){
            val editor=mSharedPreferences!!.edit()
editor.putString(Constants.SP_PROFILE_PIC_DATA,"pic")
            editor.putString(Constants.SP_PROFILE_USERNAME,"Guest")
            editor.putBoolean(Constants.SP_DARK_MODE,isDarkMode)
            editor.putString(Constants.SP_STORAGE_LOCATION,"/storage/emulated/0")
editor.putString(Constants.SP_HISTORY_LOCATION,"/storage/emulated/0")


            editor.apply()

            Constants.currentHistoryLocation="/storage/emulated/0"

        }


        if(!mProfileName.isNullOrEmpty()){

       navUserName!!.text = mProfileName

        }

        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

startActivityForResult(Intent(this,PermissionsActivity::class.java),Constants.RQ_PERMISSION_ACTIVITY)

        }
        else{

            val adapter = MainPagerFragmentAdapter(supportFragmentManager, lifecycle)

            vp_main.adapter = adapter
            vp_main.offscreenPageLimit=4
            vp_main.isUserInputEnabled = false



        }

        
        wifiManager =
                applicationContext.getSystemService(WIFI_SERVICE) as WifiManager




        imv_drawer.setOnClickListener {

            if(!main_drawerLayout.isDrawerOpen(GravityCompat.START)){
                main_drawerLayout.openDrawer(GravityCompat.START)

            }else{
                main_drawerLayout.closeDrawer(GravityCompat.START)
            }


        }

        mHandler = Handler(Looper.getMainLooper())
//        var receiver = WifiReceiver()
//
//val mIntentFilter=IntentFilter()
//        mIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
//        mIntentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)
//registerReceiver(receiver,mIntentFilter)
//
//        registerReceiver(receiver, IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION))
//
//        registerReceiver(receiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))



        p2pManager=getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager

        p2pChannel=p2pManager!!.initialize(this,mainLooper,null)

        p2pChannel.also {
            p2pReceiver=WifiDirectBroadcastReceiver(p2pManager!!,p2pChannel!!,this)

        }
        p2pIntentFilter=IntentFilter().apply {
            addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
        }

        val appTab = tab_main.newTab().setText("Apps").setIcon(R.drawable.ic_baseline_android_24)
        val historyTab=tab_main.newTab().setText("History").setIcon(R.drawable.ic_baseline_history_24)
        tab_main.addTab(historyTab)
        tab_main.addTab(appTab)

        tab_main.addTab(tab_main.newTab().setText("Images").setIcon(R.drawable.ic_outline_image_24))
        tab_main.addTab(tab_main.newTab().setText("Videos").setIcon(R.drawable.ic_outline_video_library_24))
        tab_main.addTab(tab_main.newTab().setText("Audio").setIcon(R.drawable.ic_outline_audiotrack_24))
        tab_main.addTab(tab_main.newTab().setText("Files").setIcon(R.drawable.ic_baseline_folder_24))
//
//        tab_main.selectTab(appTab)
//        vp_main.currentItem=1

tab_main.selectTab(historyTab)
        val receiveAnimation=AnimationUtils.loadAnimation(this,R.anim.spawn_recieve)
        val sendAnimation=AnimationUtils.loadAnimation(this,R.anim.spawn_send)
         transitionDown=AnimationUtils.loadAnimation(this,R.anim.transition_down)
        animationMoveUp = AnimationUtils.loadAnimation(this,R.anim.transition_up)
        val bounceAnimation=AnimationUtils.loadAnimation(this,R.anim.bounce_loop)
        val transferAnimation=AnimationUtils.loadAnimation(this,R.anim.righttoleft)



        mHandler!!.postDelayed(Runnable {      btn_receiver.startAnimation(receiveAnimation)
            btn_sender.startAnimation(sendAnimation)

        },400)




        btn_disconnect.setOnClickListener {


            if(!Constants.isServer) {
                Constants.clientThread!!.socket!!.close()


//
//Constants.clientThread!!.socket!!.close()


// if(android.os.Build.VERSION.SDK_INT<android.os.Build.VERSION_CODES.Q) {
//

// }
//    else{
//     Constants.clientThread!!.socket!!.close()
//
//
// }

            }

            else{
//                Constants.isClose=true
Constants.serverThread!!.socket!!.close()
            }

        }



        drawer_navigation.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.d_menu_profile->{
startActivityForResult(Intent(this,ProfileActivity::class.java),Constants.RQ_PROFILE_ACTIVITY)


                    return@OnNavigationItemSelectedListener true
                }

               R.id.d_menu_settings->{

                   startActivityForResult(Intent(this, SettingsActivity::class.java),Constants.RQ_SETTINGS_ACTIVITY)
                   return@OnNavigationItemSelectedListener true
               }


                R.id.d_menu_feedback->{


                    startActivity(Intent(this, FeedbackActivity::class.java))
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



            while (Constants.audioSelected.isNotEmpty()) {

                musicFragment!!.adapter!!.audioList[Constants.audioSelected[0]].onSelect=false
              musicFragment!!.adapter!!.notifyItemChanged(Constants.audioSelected[0])
                Constants.audioSelected.removeAt(0)
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





        btn_sender.setOnClickListener {

//            if (verifyLocation()) {
//
//                if (isLocationEnabled()) {
//
//if(mReservation!=null){
//    mReservation!!.close()
//    Thread.sleep(3000)
//    mReservation=null
//
//}
//                    val wifimanager =
//                        applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
//
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                        wifimanager.startLocalOnlyHotspot(object :
//                            WifiManager.LocalOnlyHotspotCallback() {
//
//                            override fun onStarted(reservation: WifiManager.LocalOnlyHotspotReservation?) {
//                                super.onStarted(reservation)
//                                connectionType = Constants.CONNECTION_TYPE_HOTSPOT
//                                Constants.serverThread = ServerThread(this@MainActivity)
//                                Constants.serverThread!!.start()
//
//
//                                mReservation = reservation
//                                ssid = reservation!!.wifiConfiguration!!.SSID
//                                password = reservation!!.wifiConfiguration!!.preSharedKey
//
//
//                                val dialog = Dialog(this@MainActivity)
//                                dialog.setContentView(R.layout.dialog_hotspot_sender)
//                                dialog.tv_hotspot_SSID.text = ssid
//                                dialog.tv_hotspot_password.text = password
//
//
//                                    dialog.show()
//
//
//                            }
//
//                        }, null)
//
//                    } else {
//
//                        val intent = Intent()
//                        intent.setClassName(
//                            "com.android.settings",
//                            "com.android.settings.TetherSettings"
//                        )
//                        startActivityForResult(intent, 0)
//                    }
//
//
//                } else {
//
//                    Toast.makeText(this, "Your location needs to be turned on", Toast.LENGTH_SHORT)
//                        .show()
//                    var intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                    startActivity(intent)
//
//                }
//            } else {
//                if (ActivityCompat.shouldShowRequestPermissionRationale(
//                        this,
//                        Manifest.permission.ACCESS_FINE_LOCATION
//                    )
//                ) {
//
//                    var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                    var uri = Uri.fromParts("package", packageName, null)
//                    intent.setData(uri)
//                    startActivity(intent)
//
//                } else {
//                    ActivityCompat.requestPermissions(
//                        this,
//                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                        Constants.RQ_LOCATION_PERMISSION
//                    )
//                }
//
//
//            }

    p2pManager!!.requestConnectionInfo(p2pChannel,object:WifiP2pManager.ConnectionInfoListener{
        override fun onConnectionInfoAvailable(info: WifiP2pInfo?) {
if(info!!.groupFormed) {


    p2pManager!!.removeGroup(p2pChannel, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {

        }

        override fun onFailure(p0: Int) {

        }


    })
}

        }


    })


            Constants.noNetwork=false
            if (verifyLocation()) {


                if (!wifiManager!!.isWifiEnabled) {
                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
                        wifiManager!!.setWifiEnabled(true)

                    } else {
                        startActivity(Intent(android.provider.Settings.Panel.ACTION_WIFI))

                    }
                } else {
                    Constants.isServer = true



                    p2pManager!!.createGroup(p2pChannel, object : WifiP2pManager.ActionListener {
                        override fun onSuccess() {
Toast.makeText(this@MainActivity,"Creation successful",Toast.LENGTH_SHORT).show()
                            //INIT SERVER THREAD
                        }

                        override fun onFailure(p0: Int) {

                        }


                    })

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
//            Constants.noNetwork=false
//            if (verifyLocation()) {
//
//                val wifiManager =
//                    applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//                if(!wifiManager.isWifiEnabled) {
//                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
//                        wifiManager.setWifiEnabled(true)
//                        shouldScan = true
//                        wifiManager.startScan()
//                    } else {
//                        startActivity(Intent(android.provider.Settings.Panel.ACTION_WIFI))
//
//                    }
//                }
//                else {
//                    shouldScan = true
//                    wifiManager.startScan()
//                }
//
//
//            } else {
//                if (ActivityCompat.shouldShowRequestPermissionRationale(
//                        this,
//                        Manifest.permission.ACCESS_FINE_LOCATION
//                    )
//                ) {
//
//                    var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                    var uri = Uri.fromParts("package", packageName, null)
//                    intent.setData(uri)
//                    startActivity(intent)
//
//                } else {
//                    ActivityCompat.requestPermissions(
//                        this,
//                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                        Constants.RQ_LOCATION_PERMISSION
//                    )
//                }
//
//            }

            p2pManager!!.requestConnectionInfo(p2pChannel,object:WifiP2pManager.ConnectionInfoListener{
                override fun onConnectionInfoAvailable(info: WifiP2pInfo?) {

                    if(info!!.groupFormed) {


                        p2pManager!!.removeGroup(p2pChannel, object : WifiP2pManager.ActionListener {
                            override fun onSuccess() {





                            }

                            override fun onFailure(p0: Int) {

                            }


                        })
                    }


                }


            })

Constants.isServer=false
            Constants.connectedDevice=""

            var files = ContextCompat.getExternalFilesDirs(this, null)

var currentLocation=mSharedPreferences!!.getString(Constants.SP_STORAGE_LOCATION,"")

            if(currentLocation!="/storage/emulated/0") {
                if (files.size > 1) {

                    val path = getSDirectory(files[1].path)



                    if(currentLocation!=path){

                        val editor=mSharedPreferences!!.edit()
                        editor.putString(Constants.SP_STORAGE_LOCATION,path)
                        editor.apply()
currentLocation=path


                    }


                }
            else{

                val editor=mSharedPreferences!!.edit()
                editor.putString(Constants.SP_STORAGE_LOCATION,"/storage/emulated/0")
                    editor.apply()
currentLocation="/storage/emulated/0"
                }



            }
            Constants.currentDownloadLocation=currentLocation

            Constants.noNetwork=false
            if (verifyLocation()) {

                if(!wifiManager!!.isWifiEnabled) {
                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
                        wifiManager!!.setWifiEnabled(true)

                    } else {
                        startActivity(Intent(android.provider.Settings.Panel.ACTION_WIFI))

                    }


                }
                else {
Constants.scanDevices=true

                    p2pManager!!.discoverPeers(p2pChannel,object: WifiP2pManager.ActionListener{
                        override fun onSuccess() {
                            Log.d("PEERSS","Successful")

                        }

                        override fun onFailure(p0: Int) {


//                            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
//                               wifiManager!!.setWifiEnabled(false)
//
//                            } else {
//                               startActivity(Intent(Settings.Panel.ACTION_WIFI))
//
//                            }

                            p2pManager!!.cancelConnect(p2pChannel,object:WifiP2pManager.ActionListener{
                                override fun onSuccess() {

                                    p2pManager!!.discoverPeers(p2pChannel,object:WifiP2pManager.ActionListener{
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

                    })
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

    fun setupUIdisconnected(){
        btn_disconnect.visibility=View.GONE
        btn_receiver.visibility=View.VISIBLE
        btn_sender.visibility=View.VISIBLE
tv_connection_status.text="Not Connected"
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

    fun getSDirectory(path: String): String {
        var sdPath = ""
        var count = 0

        for (i in path) {

            if (i == '/') {

                count++

            }

            if (count == 3) {
                break
            }
            sdPath += i

        }

        return sdPath
    }

    fun showErrorMessage( message:String){
        val snackBar= if(Constants.transferActivity!=null){
            Snackbar.make(Constants.transferActivity!!.findViewById(android.R.id.content),message, Snackbar.LENGTH_SHORT)

        }else{
            Snackbar.make(findViewById(android.R.id.content),message, Snackbar.LENGTH_SHORT)
        }
        snackBar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.maverick_blue))
        snackBar.show()


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

if(resultCode==Activity.RESULT_OK){

    if(requestCode==Constants.RQ_PERMISSION_ACTIVITY){


        val adapter = MainPagerFragmentAdapter(supportFragmentManager, lifecycle)

        vp_main.adapter = adapter
        vp_main.offscreenPageLimit=4
        vp_main.isUserInputEnabled = false


    }

    else if(requestCode==Constants.RQ_PROFILE_ACTIVITY){

if(Constants.userPictureOnChanged!=null){
navUserImage!!.setImageBitmap(Constants.userPictureOnChanged)
    Constants.userPictureOnChanged=null
}
        if(!Constants.userNameOnChanged.isNullOrEmpty()){
navUserName!!.text=Constants.userNameOnChanged

            Constants.userNameOnChanged=""
        }



    }



}




    }

    override fun onDestroy() {


        p2pManager!!.requestConnectionInfo(p2pChannel,object:WifiP2pManager.ConnectionInfoListener{
            override fun onConnectionInfoAvailable(info: WifiP2pInfo?) {

                if(info!!.groupFormed) {


                    p2pManager!!.removeGroup(p2pChannel, object : WifiP2pManager.ActionListener {
                        override fun onSuccess() {

                        }

                        override fun onFailure(p0: Int) {

                        }


                    })
                }


            }


        })
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
unregisterReceiver(p2pReceiver)

    }

    override fun onResume() {
        super.onResume()
        registerReceiver(p2pReceiver,p2pIntentFilter)
    }

    override fun onBackPressed() {
        val f=supportFragmentManager.findFragmentById(R.id.holder_files_fragment)
if(Constants.mRelativePath.size>1 && vp_main.currentItem==5){



    if(f is FilesDirectoryFragment){


        f.onFragmentBackPressed(this)

    }



}

        else {

     if(Constants.mRelativePath.size==1 && vp_main.currentItem==5){

if( f is FilesDirectoryFragment)
f.onFinalStack(this)


    }


    super.onBackPressed()
}


    }


}