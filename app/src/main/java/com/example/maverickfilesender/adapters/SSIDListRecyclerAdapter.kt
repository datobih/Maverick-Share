package com.example.maverickfilesender.adapters

import android.content.Context
import android.net.wifi.ScanResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.maverickfilesender.R
import kotlinx.android.synthetic.main.item_ssid.view.*

class SSIDListRecyclerAdapter(val context: Context,val scanResults:ArrayList<ScanResult>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SSIDViewHolder(LayoutInflater.from(context).inflate(R.layout.item_ssid,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

if(holder is SSIDViewHolder){

    holder.itemView.tv_item_ssid.text=scanResults[position].SSID
holder.itemView.tv_item_ssid.setOnClickListener {





}

}


    }

    override fun getItemCount(): Int {
        return scanResults.size
    }


class SSIDViewHolder(view: View):RecyclerView.ViewHolder(view)

}