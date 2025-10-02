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
import com.example.maverickfilesender.databinding.FragmentImagesBinding
import java.util.concurrent.TimeUnit


class ImageFragment : Fragment() {

    private var _binding: FragmentImagesBinding? = null
    private val binding get() = _binding!!

var mContext:Context?=null
    override fun onAttach(context: Context) {
        super.onAttach(context)
    mContext=context

    }

    var adapter: ImageFileRecyclerAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (mContext as MainActivity).imageFragment=this
var images=ArrayList<Image>()
        var handler=(requireContext() as MainActivity).mHandler
        val imageThread = Thread {
            images = MediaHandler(mContext!!).fetchImageFiles()
            adapter = ImageFileRecyclerAdapter(mContext!!, images)

            (mContext as MainActivity).runOnUiThread {
                binding.rvImages.layoutManager = GridLayoutManager(mContext, 3)
                binding.rvImages.adapter = adapter
                binding.rvImages.setHasFixedSize(true)
                binding.progressImages.visibility = View.GONE
            }
        }
        imageThread.start()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}