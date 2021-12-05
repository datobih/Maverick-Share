package com.example.maverickfilesender.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.maverickfilesender.R
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.adapters.ImageFileRecyclerAdapter
import com.example.maverickfilesender.handlers.MediaHandler
import com.example.maverickfilesender.model.Image
import kotlinx.android.synthetic.main.fragment_images.*


class ImageFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_images, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
var images=ArrayList<Image>()
        var handler=(requireContext() as MainActivity).mHandler
val imageThread=Thread(object :Runnable{
    override fun run() {


        images=MediaHandler(requireContext()).fetchImageFiles()

        val adapter=ImageFileRecyclerAdapter(requireContext(),images)


        handler!!.post {
            rv_images.setHasFixedSize(true)
            rv_images.layoutManager=GridLayoutManager(requireContext(),3)
            rv_images.adapter=adapter
progress_images.visibility=View.GONE
        }


    }






}).start()


    }


}