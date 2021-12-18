package com.example.maverickfilesender.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maverickfilesender.R
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.model.AppPackage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_app.view.*
import java.io.File
import java.text.DecimalFormat

class AppPackageRecyclerAdapter(val context: Context,val appPackagePackageList:MutableList<AppPackage>):RecyclerView.Adapter<AppPackageRecyclerAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_app,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {



        if(appPackagePackageList[position].onSelect){
Constants.appSelected.add(position)
            holder.itemView.imv_appPackageSelect.visibility=View.VISIBLE
            if((context as MainActivity).ll_main_send.visibility!=View.VISIBLE) {
                (context as MainActivity).ll_main_send.visibility = View.VISIBLE
                (context as MainActivity).ll_main_send.startAnimation((context as MainActivity).animationMoveUp)
            }
            }
        else{
            Constants.appSelected.remove(position)
            holder.itemView.imv_appPackageSelect.visibility=View.GONE

            if(Constants.sendCount==0){
                (context as MainActivity).ll_main_send.startAnimation((context as MainActivity).transitionDown)
                (context as MainActivity).ll_main_send.visibility=View.INVISIBLE
            }
        }


holder.itemView.ll_appItem.setOnClickListener {

appPackagePackageList[position].onSelect=!appPackagePackageList[position].onSelect

if(appPackagePackageList[position].onSelect){
   Constants.selectedFiles.add(File(appPackagePackageList[position].applicationInfo.sourceDir))
    Constants.sendCount++
}

    else{
  Constants.selectedFiles.remove(File(appPackagePackageList[position].applicationInfo.sourceDir))

    Constants.sendCount--
    }


    notifyItemChanged(position)



}


            Glide.with(context)
                .load(appPackagePackageList[position].drawable)
                .centerCrop()

                .into(holder.icon)

//
//          var drawable=appPackageList[position].loadIcon(context.packageManager)
//           holder.itemView.imv.setImageDrawable(drawable)


val name=appPackagePackageList[position].applicationInfo.loadLabel(context.packageManager).toString()
            holder.appName.text=name
            val sizeKb= (File(appPackagePackageList[position].applicationInfo.sourceDir).length())/(1024)
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
        return appPackagePackageList.size
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