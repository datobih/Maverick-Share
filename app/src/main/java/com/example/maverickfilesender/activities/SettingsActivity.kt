package com.example.maverickfilesender.activities

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.example.maverickfilesender.R
import com.example.maverickfilesender.constants.Constants
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.dialog_download_location.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
var isInternal=true
        val sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
var persistentDirectory=sharedPreferences.getString(Constants.SP_STORAGE_LOCATION,"")
        val isDarkMode = sharedPreferences.getBoolean(Constants.SP_DARK_MODE, true)
var historyDirectory=sharedPreferences.getString(Constants.SP_HISTORY_LOCATION,"")
var isHistoryInternal=true
        switch_darkMode.isChecked = isDarkMode

        var sdDirectory=""


        var files = ContextCompat.getExternalFilesDirs(this, null)

        if(files.size>1){
            sdDirectory =getSDirectory(files[1].path)
        }


        if(persistentDirectory!="/storage/emulated/0"){
            isInternal=false

        }

        if(persistentDirectory!="/storage/emulated/0"&& persistentDirectory!=sdDirectory){
            val editor = sharedPreferences.edit()
            if(sdDirectory.isNotEmpty()) {
                persistentDirectory = sdDirectory


                editor.putString(Constants.SP_STORAGE_LOCATION, sdDirectory)
            }else{

                isInternal=true

                editor.putString(Constants.SP_STORAGE_LOCATION, "/storage/emulated/0")
            }

            editor.apply()

        }

        if(persistentDirectory!="/storage/emulated/0"){
            tv_current_StorageLocation.text="SD Storage"
        }


        if(historyDirectory!="/storage/emulated/0"){
            val editor = sharedPreferences.edit()
            if(historyDirectory!=sdDirectory){
                historyDirectory="/storage/emulated/0"
                editor.putString(Constants.SP_HISTORY_LOCATION,historyDirectory)


            }



        }

        if(historyDirectory=="/storage/emulated/0"){
            isHistoryInternal=true
            tv_current_historyLocation.text="Internal Storage"
        }
        else{
isHistoryInternal=false
            tv_current_historyLocation.text="SD Storage"
        }



        ll_downloadLocation.setOnClickListener {




var dialog=Dialog(this)
            dialog.setContentView(R.layout.dialog_download_location)

            if(!sdDirectory.isNullOrEmpty()) {
                dialog.ll_Dialog_sdStorage.visibility=View.VISIBLE
                dialog.tvDialog_sdDirectory.text = sdDirectory
            }
            if(isInternal){
                dialog.imv_internalStorage_check.visibility=View.VISIBLE
            }
            else{
                dialog.imv_internalStorage_check.visibility=View.GONE
                dialog.imv_sdStorage_check.visibility=View.VISIBLE
            }


            dialog.ll_Dialog_internalStorage.setOnClickListener {

                if(!isInternal){
                    val editor=sharedPreferences.edit()
                    editor.putString(Constants.SP_STORAGE_LOCATION,"/storage/emulated/0")
                    editor.apply()
                    isInternal=true
                    Constants.currentDownloadLocation="/storage/emulated/0"

                    tv_current_StorageLocation.text="Internal Storage"
                    dialog.imv_internalStorage_check.visibility=View.VISIBLE
                    dialog.imv_sdStorage_check.visibility=View.GONE

                }


            }

            dialog.ll_Dialog_sdStorage.setOnClickListener {


                if(isInternal){

                    val editor=sharedPreferences.edit()
                    editor.putString(Constants.SP_STORAGE_LOCATION,sdDirectory)
                    editor.apply()
                    isInternal=false
                    Constants.currentDownloadLocation=sdDirectory
                    tv_current_StorageLocation.text="SD Storage"

                    dialog.imv_internalStorage_check.visibility=View.GONE
                    dialog.imv_sdStorage_check.visibility=View.VISIBLE
                }

            }


            dialog.show()




        }

ll_historyLocation.setOnClickListener {

    var dialog=Dialog(this)
    dialog.setContentView(R.layout.dialog_download_location)
    dialog.tv_dialog_storageLocation.text="History Location"

    if(!sdDirectory.isNullOrEmpty()) {
        dialog.ll_Dialog_sdStorage.visibility=View.VISIBLE
        dialog.tvDialog_sdDirectory.text = sdDirectory
    }


    if(isHistoryInternal){
        dialog.imv_internalStorage_check.visibility=View.VISIBLE
    }
    else{
        dialog.imv_internalStorage_check.visibility=View.GONE
        dialog.imv_sdStorage_check.visibility=View.VISIBLE
    }

    dialog.ll_Dialog_internalStorage.setOnClickListener {

        if(!isHistoryInternal){
            val editor=sharedPreferences.edit()
            editor.putString(Constants.SP_HISTORY_LOCATION,"/storage/emulated/0")
            editor.apply()
            isHistoryInternal=true
            Constants.currentHistoryLocation="/storage/emulated/0"

            tv_current_historyLocation.text="Internal Storage"
            dialog.imv_internalStorage_check.visibility=View.VISIBLE
            dialog.imv_sdStorage_check.visibility=View.GONE

        }


    }


    dialog.ll_Dialog_sdStorage.setOnClickListener {

        if(isHistoryInternal){
            val editor=sharedPreferences.edit()
            editor.putString(Constants.SP_HISTORY_LOCATION,sdDirectory)
            editor.apply()
            isHistoryInternal=false
            Constants.currentHistoryLocation=sdDirectory

            tv_current_historyLocation.text="SD Storage"
            dialog.imv_internalStorage_check.visibility=View.GONE
            dialog.imv_sdStorage_check.visibility=View.VISIBLE

        }


    }


    dialog.show()


}




        switch_darkMode.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            val editor = sharedPreferences.edit()

            override fun onCheckedChanged(p0: CompoundButton?, value: Boolean) {

                if (Constants.clientThread != null || Constants.serverThread != null) {
                    switch_darkMode.isChecked = !value

                  Toast.makeText(this@SettingsActivity,"Can't switch to dark mode when in a connection",Toast.LENGTH_SHORT).show()

                } else {


                    clearSelectedFiles()



                    if (value == false) {

                        editor.putBoolean(Constants.SP_DARK_MODE, false)

                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        Constants.isDarkMode = false
                    } else {
                        editor.putBoolean(Constants.SP_DARK_MODE, true)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        Constants.isDarkMode = true
                    }
                    editor.apply()
                }
            }

        })
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

    fun clearSelectedFiles() {
        Constants.tempSelectedFiles.clear()




        Constants.heirarchyFiles.clear()
        Constants.sendCount = 0




        while (Constants.imagesSelected.isNotEmpty()) {


            Constants.imagesSelected.removeAt(0)
        }



        while (Constants.audioSelected.isNotEmpty()) {

            Constants.audioSelected.removeAt(0)
        }



        while (Constants.appSelected.isNotEmpty()) {

            Constants.appSelected.removeAt(0)


        }

        while (Constants.videosSelected.isNotEmpty()) {


            Constants.videosSelected.removeAt(0)

        }
    }

}