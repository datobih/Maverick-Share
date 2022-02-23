package com.example.maverickfilesender.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import com.example.maverickfilesender.R
import com.example.maverickfilesender.constants.Constants
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.content_main.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        val isDarkMode = sharedPreferences.getBoolean(Constants.SP_DARK_MODE, true)


        switch_darkMode.isChecked = isDarkMode



        switch_darkMode.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            val editor = sharedPreferences.edit()

            override fun onCheckedChanged(p0: CompoundButton?, value: Boolean) {


                clearSelectedFiles()



                if (value == false) {

                    editor.putBoolean(Constants.SP_DARK_MODE, false)

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                } else {
                    editor.putBoolean(Constants.SP_DARK_MODE, true)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                editor.apply()
            }


        })
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