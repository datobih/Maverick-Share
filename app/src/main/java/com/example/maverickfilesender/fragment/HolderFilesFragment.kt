package com.example.maverickfilesender.fragment

import android.content.Context
import android.os.Bundle
import android.os.StatFs
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.maverickfilesender.R
import com.example.maverickfilesender.constants.Constants
import kotlinx.android.synthetic.main.fragment_holder_files.*
import kotlinx.android.synthetic.main.fragment_holder_files.view.*
import java.io.File
import java.text.DecimalFormat

class HolderFilesFragment : Fragment() {


    var mContext: Context?=null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context

    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_holder_files, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        requireActivity().supportFragmentManager.beginTransaction().apply {

            replace(R.id.holder_files_fragment,StorageDirectoryFragment())
commit()

        }



    }


}