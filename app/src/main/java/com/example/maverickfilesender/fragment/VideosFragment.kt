package com.example.maverickfilesender.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.postDelayed
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maverickfilesender.R
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.adapters.ImageFileRecyclerAdapter
import com.example.maverickfilesender.adapters.VideoFileRecyclerAdapter
import com.example.maverickfilesender.handlers.MediaHandler
import com.example.maverickfilesender.model.Image
import com.example.maverickfilesender.model.Video
import kotlinx.android.synthetic.main.fragment_images.*
import kotlinx.android.synthetic.main.fragment_videos.*
import kotlinx.android.synthetic.main.fragment_videos.view.*
import java.util.concurrent.TimeUnit


class VideosFragment : Fragment() {

    var mContext: Context?=null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_videos, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var videos=ArrayList<Video>()
        var handler=Handler(Looper.getMainLooper())
        val videoThread=Thread(object :Runnable{
            override fun run() {


                videos= MediaHandler(requireContext()).fetchVideoFiles()

                val adapter= VideoFileRecyclerAdapter(requireContext(),videos)


                (mContext as MainActivity).runOnUiThread{


                    view.rv_videos.setHasFixedSize(true)
                    view.rv_videos.layoutManager= LinearLayoutManager(requireContext())
                    view.rv_videos.adapter=adapter
                    view.progress_videos.visibility=View.GONE
                }


            }






        }).start()


    }

}