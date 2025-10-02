package com.example.maverickfilesender.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import com.example.maverickfilesender.R
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.databinding.ActivityProfileBinding
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    var mSharedPreferences :SharedPreferences?=null

    var mProfilePicEncoded:String?=""
    var mProfileName:String?=""
    var mData:ByteArray?=null
var profileBitmap:Bitmap?=null
    var bitmap:Bitmap?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mSharedPreferences= getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        mProfilePicEncoded=mSharedPreferences!!.getString(Constants.SP_PROFILE_PIC_DATA,"")
mProfileName=mSharedPreferences!!.getString(Constants.SP_PROFILE_USERNAME,"")


        binding.btnProfileBack.setOnClickListener {

            onBackPressed()

        }


if(!mProfilePicEncoded.isNullOrEmpty()&&mProfilePicEncoded!="pic"){
val userPictureByteArray=Base64.decode(mProfilePicEncoded,Base64.DEFAULT)
profileBitmap=BitmapFactory.decodeByteArray(userPictureByteArray,0,userPictureByteArray.size)

    binding.civUserProfile.setImageBitmap(profileBitmap)

}
        if(!mProfileName.isNullOrEmpty()){

            binding.etProfileUsername.setText(mProfileName)

        }



binding.civUserProfile.setOnClickListener {

val intent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    startActivityForResult(intent,Constants.RQ_PROFILE_PIC)


}


        binding.btnUpdateUserProfile.setOnClickListener {
            val editor=mSharedPreferences!!.edit()
val dataArrayList=ArrayList<String>()

            var verify=false
            if(mData!=null){
                binding.llProfileLoading.visibility=View.VISIBLE
                verify=true
                val encodedData=Base64.encodeToString(mData,Base64.DEFAULT)


                editor.putString(Constants.SP_PROFILE_PIC_DATA,encodedData)
editor.apply()
                Constants.userPictureOnChanged=bitmap
                binding.llProfileLoading.visibility=View.GONE
            }
            if(binding.etProfileUsername.text.toString()!=mProfileName){
                binding.llProfileLoading.visibility=View.VISIBLE
verify=true
                editor.putString(Constants.SP_PROFILE_USERNAME,binding.etProfileUsername.text.toString())
                editor.apply()
                Constants.userNameOnChanged=binding.etProfileUsername.text.toString()
                binding.llProfileLoading.visibility=View.GONE
            }


if(verify){
  setResult(Activity.RESULT_OK)
finish()
}
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


    if(resultCode==Activity.RESULT_OK){

        if(requestCode==Constants.RQ_PROFILE_PIC){

           if(data!!.data!=null){

               try {
                   val inputStream = contentResolver.openInputStream(data!!.data!!)


                   val mDrawable=Drawable.createFromStream(inputStream, data!!.data!!.toString())
bitmap=mDrawable?.let { getBitmapFromDrawable(it) }
binding.civUserProfile.setImageBitmap(bitmap)


                   val stream= ByteArrayOutputStream()

                   bitmap!!.compress(Bitmap.CompressFormat.JPEG,100,stream)
                    mData=stream.toByteArray()





               }
               catch (e:FileNotFoundException){
                   Log.d("Error",e.message!!)
               }


           }


        }

    }

    }

    fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
        val bitmap= Bitmap.createBitmap(drawable.intrinsicWidth,drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas= Canvas(bitmap)
        drawable.setBounds(0,0,canvas.width,canvas.height)
        drawable.draw(canvas)
        return bitmap

    }
}