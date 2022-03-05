package com.example.maverickfilesender.receivers

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.maverickfilesender.R
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.constants.Constants

class ApplicationReceiver:Application() {

    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.Theme_MaverickFileSender)
        val mSharedPreferences= getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        val isDarkMode=mSharedPreferences!!.getBoolean(Constants.SP_DARK_MODE,true)

        Constants.isDarkMode=isDarkMode




        if(!isDarkMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }



        registerActivityLifecycleCallbacks(object :ActivityLifecycleCallbacks{
        override fun onActivityCreated(p0: Activity, p1: Bundle?) {

        }

        override fun onActivityStarted(p0: Activity) {

        }

        override fun onActivityResumed(p0: Activity) {

        }

        override fun onActivityPaused(p0: Activity) {

        }

        override fun onActivityStopped(p0: Activity) {

        }

        override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {
            Log.d("DESTROYYYY","Destroyed")
if(activity is MainActivity){

}
        }


    })

    }


}