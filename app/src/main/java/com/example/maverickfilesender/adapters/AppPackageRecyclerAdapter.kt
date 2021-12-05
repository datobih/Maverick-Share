package com.example.maverickfilesender.adapters

import android.content.Context
import android.content.pm.ApplicationInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maverickfilesender.R
import kotlinx.android.synthetic.main.item_app.view.*
import java.io.File
import java.text.DecimalFormat

class AppPackageRecyclerAdapter(val context: Context,val appPackageList:MutableList<ApplicationInfo>):RecyclerView.Adapter<AppPackageRecyclerAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_app,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {



            Glide.with(context)
                .load(appPackageList[position].loadIcon(context.packageManager))
                .centerCrop()

                .into(holder.icon)

//
//          var drawable=appPackageList[position].loadIcon(context.packageManager)
//           holder.itemView.imv.setImageDrawable(drawable)


val name=appPackageList[position].loadLabel(context.packageManager).toString()
            holder.appName.text=name
            val sizeKb= (File(appPackageList[position].sourceDir).length())/(1024)
            var sizeMb=sizeKb.toFloat()/1024f
            var sizeGb=sizeMb.toFloat()/1024f

            sizeMb=roundToTwoDecimals(sizeMb)
            sizeGb=roundToTwoDecimals(sizeGb)

            String.format("%.2f",sizeMb)
            if(sizeKb<1000){
                holder.appSize.text=sizeKb.toString()+"KB"
            }

            else if(sizeKb>1000 && sizeMb<1000){
                holder.appSize.text=sizeMb.toString()+"MB"

            }

            else if(sizeMb>1000){
                holder.appSize.text=sizeGb.toString()+"GB"
            }


        }

    override fun getItemCount(): Int {
        return appPackageList.size
    }




    fun roundToTwoDecimals(value:Float):Float{
val df=DecimalFormat("#.##")



return df.format(value).toFloat()
    }






    class MyViewHolder(view:View):RecyclerView.ViewHolder(view){

        val appName=view.tv_appName
        val appSize=view.tv_appSize
        val icon=view.imv


    }

}