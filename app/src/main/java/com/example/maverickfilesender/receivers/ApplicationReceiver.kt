package com.example.maverickfilesender.receivers

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.example.maverickfilesender.activities.MainActivity

class ApplicationReceiver:Application() {

    override fun onCreate() {
        super.onCreate()


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