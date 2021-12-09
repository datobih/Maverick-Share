package com.example.maverickfilesender.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.maverickfilesender.R
import com.example.maverickfilesender.constants.Constants
import java.io.File


class FilesDirectoryFragment : Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_files_directory, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
val bundle=this.arguments
val directory=bundle!!.getString(Constants.BUNDLE_STORAGE_DIRECTORY)




      val file= File(directory)

        val files:Array<File> =file.listFiles()

val i=0
    }

}