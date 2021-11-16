package com.example.maverickfilesender.adapters

import android.content.Context
import android.content.pm.ApplicationInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.maverickfilesender.R
import kotlinx.android.synthetic.main.item_app.view.*
import java.io.File
import java.text.DecimalFormat

class AppPackageRecyclerAdapter(val context: Context,val appPackageList:MutableList<ApplicationInfo>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_app,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is MyViewHolder){
            var drawable=appPackageList[position].loadIcon(context.packageManager)
            holder.itemView.imv.setImageDrawable(drawable)

val name=appPackageList[position].loadLabel(context.packageManager).toString()
            holder.itemView.tv_appName.text=name
            val sizeKb= (File(appPackageList[position].sourceDir).length())/(1024)
            var sizeMb=sizeKb.toFloat()/1024f
            var sizeGb=sizeMb.toFloat()/1024f

            sizeMb=roundToTwoDecimals(sizeMb)
            sizeGb=roundToTwoDecimals(sizeGb)

            String.format("%.2f",sizeMb)
            if(sizeKb<1000){
                holder.itemView.tv_appSize.text=sizeKb.toString()+"KB"
            }

            else if(sizeKb>1000 && sizeMb<1000){
                holder.itemView.tv_appSize.text=sizeMb.toString()+"MB"

            }

            else if(sizeMb>1000){
                holder.itemView.tv_appSize.text=sizeGb.toString()+"GB"
            }


        }



    }

    fun roundToTwoDecimals(value:Float):Float{
val df=DecimalFormat("#.##")



return df.format(value).toFloat()
    }

    override fun getItemCount(): Int {

        return appPackageList.size
    }




    class MyViewHolder(view:View):RecyclerView.ViewHolder(view)

}