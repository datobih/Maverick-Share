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
import com.example.maverickfilesender.databinding.ActivitySettingsBinding
import com.example.maverickfilesender.databinding.DialogDownloadLocationBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSettingsBack.setOnClickListener {

            onBackPressed()

        }

var isInternal=true
        val sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
var persistentDirectory=sharedPreferences.getString(Constants.SP_STORAGE_LOCATION,"")
        val isDarkMode = sharedPreferences.getBoolean(Constants.SP_DARK_MODE, true)
var historyDirectory=sharedPreferences.getString(Constants.SP_HISTORY_LOCATION,"")
var isHistoryInternal=true
        binding.switchDarkMode.isChecked = isDarkMode

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
            binding.tvCurrentStorageLocation.text="SD Storage"
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
            binding.tvCurrentHistoryLocation.text="Internal Storage"
        }
        else{
isHistoryInternal=false
            binding.tvCurrentHistoryLocation.text="SD Storage"
        }



        binding.llDownloadLocation.setOnClickListener {




var dialog=Dialog(this)
            val dialogBinding = DialogDownloadLocationBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)

            if(!sdDirectory.isNullOrEmpty()) {
                dialogBinding.llDialogSdStorage.visibility=View.VISIBLE
                dialogBinding.tvDialogSdDirectory.text = sdDirectory
            }
            if(isInternal){
                dialogBinding.imvInternalStorageCheck.visibility=View.VISIBLE
            }
            else{
                dialogBinding.imvInternalStorageCheck.visibility=View.GONE
                dialogBinding.imvSdStorageCheck.visibility=View.VISIBLE
            }


            dialogBinding.llDialogInternalStorage.setOnClickListener {

                if(!isInternal){
                    val editor=sharedPreferences.edit()
                    editor.putString(Constants.SP_STORAGE_LOCATION,"/storage/emulated/0")
                    editor.apply()
                    isInternal=true
                    Constants.currentDownloadLocation="/storage/emulated/0"

                    binding.tvCurrentStorageLocation.text="Internal Storage"
                    dialogBinding.imvInternalStorageCheck.visibility=View.VISIBLE
                    dialogBinding.imvSdStorageCheck.visibility=View.GONE

                }


            }

            dialogBinding.llDialogSdStorage.setOnClickListener {


                if(isInternal){

                    val editor=sharedPreferences.edit()
                    editor.putString(Constants.SP_STORAGE_LOCATION,sdDirectory)
                    editor.apply()
                    isInternal=false
                    Constants.currentDownloadLocation=sdDirectory
                    binding.tvCurrentStorageLocation.text="SD Storage"

                    dialogBinding.imvInternalStorageCheck.visibility=View.GONE
                    dialogBinding.imvSdStorageCheck.visibility=View.VISIBLE
                }

            }


            dialog.show()




        }

binding.llHistoryLocation.setOnClickListener {

    var dialog=Dialog(this)
    val dialogBinding = DialogDownloadLocationBinding.inflate(layoutInflater)
    dialog.setContentView(dialogBinding.root)
    dialogBinding.tvDialogStorageLocation.text="History Location"

    if(!sdDirectory.isNullOrEmpty()) {
        dialogBinding.llDialogSdStorage.visibility=View.VISIBLE
        dialogBinding.tvDialogSdDirectory.text = sdDirectory
    }


    if(isHistoryInternal){
        dialogBinding.imvInternalStorageCheck.visibility=View.VISIBLE
    }
    else{
        dialogBinding.imvInternalStorageCheck.visibility=View.GONE
        dialogBinding.imvSdStorageCheck.visibility=View.VISIBLE
    }

    dialogBinding.llDialogInternalStorage.setOnClickListener {

        if(!isHistoryInternal){
            val editor=sharedPreferences.edit()
            editor.putString(Constants.SP_HISTORY_LOCATION,"/storage/emulated/0")
            editor.apply()
            isHistoryInternal=true
            Constants.currentHistoryLocation="/storage/emulated/0"

            binding.tvCurrentHistoryLocation.text="Internal Storage"
            dialogBinding.imvInternalStorageCheck.visibility=View.VISIBLE
            dialogBinding.imvSdStorageCheck.visibility=View.GONE

        }


    }


    dialogBinding.llDialogSdStorage.setOnClickListener {

        if(isHistoryInternal){
            val editor=sharedPreferences.edit()
            editor.putString(Constants.SP_HISTORY_LOCATION,sdDirectory)
            editor.apply()
            isHistoryInternal=false
            Constants.currentHistoryLocation=sdDirectory

            binding.tvCurrentHistoryLocation.text="SD Storage"
            dialogBinding.imvInternalStorageCheck.visibility=View.GONE
            dialogBinding.imvSdStorageCheck.visibility=View.VISIBLE

        }


    }


    dialog.show()


}




        binding.switchDarkMode.setOnCheckedChangeListener { p0: CompoundButton?, value: Boolean ->
            val editor = sharedPreferences.edit()

                if (Constants.clientThread != null || Constants.isServerActive == true) {
                    binding.switchDarkMode.isChecked = !value

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