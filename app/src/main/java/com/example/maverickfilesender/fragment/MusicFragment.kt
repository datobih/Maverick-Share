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
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.adapters.AudioFileRecyclerAdapter
import com.example.maverickfilesender.handlers.MediaHandler
import com.example.maverickfilesender.model.Music
import com.example.maverickfilesender.databinding.FragmentMusicBinding


class MusicFragment : Fragment() {

    private var _binding: FragmentMusicBinding? = null
    private val binding get() = _binding!!

var mContext:Context?=null
    var adapter: AudioFileRecyclerAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

    mContext=context
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentMusicBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (context as MainActivity).musicFragment=this
    var audioList=ArrayList<Music>()


      val musicThread=Thread(
          object :Runnable{

              override fun run() {

                  audioList=MediaHandler(mContext!!).getMediaFromDevice()

                  (mContext as MainActivity).runOnUiThread {
                      adapter = AudioFileRecyclerAdapter(mContext!!, audioList)
                      binding.rvAudio.layoutManager = LinearLayoutManager(mContext)
                      binding.rvAudio.adapter = adapter
                  }
              }





          }


        )
        musicThread.start()






    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}