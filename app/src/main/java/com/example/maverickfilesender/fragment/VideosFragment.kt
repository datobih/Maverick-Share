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
import com.example.maverickfilesender.databinding.FragmentVideosBinding
import java.util.concurrent.TimeUnit


class VideosFragment : Fragment() {

    private var _binding: FragmentVideosBinding? = null
    private val binding get() = _binding!!

    var mContext: Context?=null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentVideosBinding.inflate(inflater, container, false)
        return binding.root
    }

    var adapter: VideoFileRecyclerAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (mContext as MainActivity).videosFragment=this
        var videos=ArrayList<Video>()
        var handler=Handler(Looper.getMainLooper())
        val videoThread = Thread {
            videos = MediaHandler(requireContext()).fetchVideoFiles()
            adapter = VideoFileRecyclerAdapter(requireContext(), videos)

            (mContext as MainActivity).runOnUiThread {
                binding.rvVideos.setHasFixedSize(true)
                binding.rvVideos.layoutManager = LinearLayoutManager(mContext)
                binding.rvVideos.adapter = adapter
                binding.progressVideos.visibility = View.GONE
            }
        }
        videoThread.start()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}