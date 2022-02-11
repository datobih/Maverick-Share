package com.example.maverickfilesender.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.maverickfilesender.R
import com.example.maverickfilesender.constants.Constants
import kotlinx.android.synthetic.main.activity_permissions.*

class PermissionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MaverickFileSender)
        setContentView(R.layout.activity_permissions)



val buttonAnimation= AnimationUtils.loadAnimation(this,R.anim.spawn_enable)
        btn_enable_permission.startAnimation(buttonAnimation)



        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {

                if (!Environment.isExternalStorageManager()) {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    val uri = Uri.fromParts("package", this.packageName, null)
                    intent.setData(uri)
                    startActivity(intent)

                }

            }
setResult(Activity.RESULT_OK)
            overridePendingTransition(0,0)
            finish()



        }

        btn_enable_permission.setOnClickListener {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){

                var intent= Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                var uri= Uri.fromParts("package",this.packageName,null)
                intent.setData(uri)
                startActivity(intent)



            }


            else{
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.RQ_READ_WRITE_PERMISSION)



            }



        }












    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==Constants.RQ_READ_WRITE_PERMISSION){


            if(grantResults[0] == PackageManager.PERMISSION_GRANTED ){


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    if (!Environment.isExternalStorageManager()) {
                        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                        val uri = Uri.fromParts("package", this.packageName, null)
                        intent.setData(uri)
                        startActivityForResult(intent,0)

                    }else{
                        setResult(Activity.RESULT_OK)
                        finish()


                    }



                }else{
                    setResult(Activity.RESULT_OK)
                    finish()

                }









            }

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    if(requestCode==0){

        setResult(Activity.RESULT_OK)
        finish()

    }


    }




}