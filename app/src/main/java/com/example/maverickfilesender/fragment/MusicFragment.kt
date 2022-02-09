package com.example.maverickfilesender.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maverickfilesender.R
import com.example.maverickfilesender.adapters.AudioFileRecyclerAdapter
import com.example.maverickfilesender.handlers.MediaHandler
import com.example.maverickfilesender.model.Music
import kotlinx.android.synthetic.main.fragment_music.*
import kotlinx.android.synthetic.main.fragment_music.view.*
import kotlinx.android.synthetic.main.item_music.*


class MusicFragment : Fragment() {
var mContext:Context?=null


    override fun onAttach(context: Context) {
        super.onAttach(context)

    mContext=context
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_music, container, false)



    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    var audioList=ArrayList<Music>()


//      val musicThread=Thread(
//          object :Runnable{
//
//              override fun run() {
//
//                  audioList=MediaHandler(mContext!!).getMediaFromDevice()
//
//
//
//              }
//
//
//
//
//
//          }
//
//
//        )
//        musicThread.start()
//
//musicThread.join()

        val adapter=AudioFileRecyclerAdapter(mContext!!,audioList)
view.rv_audio.layoutManager=LinearLayoutManager(mContext)
    view.rv_audio.adapter=adapter


    }

}