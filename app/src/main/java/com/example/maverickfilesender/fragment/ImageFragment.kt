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
import com.example.maverickfilesender.R
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.adapters.ImageFileRecyclerAdapter
import com.example.maverickfilesender.handlers.MediaHandler
import com.example.maverickfilesender.model.Image
import kotlinx.android.synthetic.main.fragment_images.*
import kotlinx.android.synthetic.main.fragment_images.view.*
import java.util.concurrent.TimeUnit


class ImageFragment : Fragment() {

var mContext:Context?=null
    override fun onAttach(context: Context) {
        super.onAttach(context)
    mContext=context

    }

var adapter:ImageFileRecyclerAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_images, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (mContext as MainActivity).imageFragment=this
var images=ArrayList<Image>()
        var handler=(requireContext() as MainActivity).mHandler
val imageThread=Thread(object :Runnable{
    override fun run() {


        images=MediaHandler(mContext!!).fetchImageFiles()

         adapter=ImageFileRecyclerAdapter(mContext!!,images)


        (mContext as MainActivity).runOnUiThread {

            view.rv_images.layoutManager=GridLayoutManager(mContext,3)
            view.rv_images.adapter=adapter
            view.rv_images.setHasFixedSize(true)
            view.progress_images.visibility=View.GONE
        }


    }






}).start()


    }


}