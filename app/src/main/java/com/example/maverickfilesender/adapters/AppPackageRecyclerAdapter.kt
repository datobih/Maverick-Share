package com.example.maverickfilesender.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maverickfilesender.R
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.databinding.ItemAppBinding
import com.example.maverickfilesender.model.AppPackage
import com.example.maverickfilesender.model.ParseFile
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.ObjectOutputStream
import java.text.DecimalFormat

class AppPackageRecyclerAdapter(val context: Context,val appPackagePackageList:MutableList<AppPackage>):RecyclerView.Adapter<AppPackageRecyclerAdapter.MyViewHolder>() {

  var onClick=false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemAppBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(appPackagePackageList[position].onSelect){
            Constants.appSelected.add(position)
            if(Constants.clientThread==null) {
                holder.binding.imvAppPackageSelect.visibility = android.view.View.VISIBLE
                (context as MainActivity).showSendPanel()
            }
            onClick=false
        }
        else if(onClick){
            Constants.appSelected.remove(position)
            holder.binding.imvAppPackageSelect.visibility = android.view.View.GONE

            if(Constants.tempSelectedFiles.isEmpty()){
                (context as MainActivity).hideSendPanel()
            }
        }

        holder.binding.llAppItem.setOnClickListener {
onClick=true
appPackagePackageList[position].onSelect=!appPackagePackageList[position].onSelect

if(appPackagePackageList[position].onSelect){

    var data:ByteArray?=null
    if (appPackagePackageList[position].drawable != null) {
        val bitmap = getBitmapFromDrawable(appPackagePackageList[position].drawable)


        val stream=ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
         data=stream.toByteArray()

        val temp=BitmapFactory.decodeByteArray(data,0,data.size)

this.appPackagePackageList[position].data=data

    }

   Constants.tempSelectedFiles.add(ParseFile( File(appPackagePackageList[position].applicationInfo.sourceDir),data,"",appPackagePackageList[position].drawable))
    Constants.sendCount++
}

    else{
  Constants.tempSelectedFiles.remove(ParseFile(File(appPackagePackageList[position].applicationInfo.sourceDir),appPackagePackageList[position].data,"",appPackagePackageList[position].drawable))

    Constants.sendCount--
    }


    notifyItemChanged(position)



}


            Glide.with(context)
                .load(appPackagePackageList[position].drawable)
                .centerCrop()
                .into(holder.binding.imv)

            val name=appPackagePackageList[position].applicationInfo.loadLabel(context.packageManager).toString()
            holder.binding.tvAppName.text=name
            val sizeKb= (File(appPackagePackageList[position].applicationInfo.sourceDir).length())/(1024)
            var sizeMb=sizeKb.toFloat()/1024f
            var sizeGb=sizeMb.toFloat()/1024f

            sizeMb=roundToTwoDecimals(sizeMb)
            sizeGb=roundToTwoDecimals(sizeGb)

            String.format("%.2f",sizeMb)
            if(sizeKb<1000){
                holder.binding.tvAppSize.text=sizeKb.toString()+"KB"
            }
            else if(sizeKb>1000 && sizeMb<1000){
                holder.binding.tvAppSize.text=sizeMb.toString()+"MB"
            }
            else if(sizeMb>1000){
                holder.binding.tvAppSize.text=sizeGb.toString()+"GB"
            }
        }

    override fun getItemCount(): Int {
        return appPackagePackageList.size
    }




    fun roundToTwoDecimals(value:Float):Float{
val df=DecimalFormat("#.##")



return df.format(value).toFloat()
    }


    fun getBitmapFromDrawable(drawable: Drawable):Bitmap{
val bitmap=Bitmap.createBitmap(drawable.intrinsicWidth,drawable.intrinsicHeight,Bitmap.Config.ARGB_8888)
val canvas=Canvas(bitmap)
        drawable.setBounds(0,0,canvas.width,canvas.height)
   drawable.draw(canvas)
        return bitmap

    }






    class MyViewHolder(val binding: ItemAppBinding) : RecyclerView.ViewHolder(binding.root)
}