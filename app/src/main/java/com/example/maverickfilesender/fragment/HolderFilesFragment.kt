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
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentTransaction
import com.example.maverickfilesender.R
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.constants.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_holder_files.*
import kotlinx.android.synthetic.main.fragment_holder_files.view.*
import java.io.File
import java.text.DecimalFormat
import kotlin.concurrent.fixedRateTimer

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



       this.childFragmentManager?.beginTransaction().apply {
this.setReorderingAllowed(true)


            this.replace(view.holder_files_fragment.id,StorageDirectoryFragment())
            this.commitAllowingStateLoss()

        }



    }

    override fun onDestroy() {




        super.onDestroy()
    }

    fun onFinalStack(context: Context){
        var i=0
        while(true){
            if(Constants.heirarchyFiles.isNotEmpty()) {
                if (i != Constants.heirarchyFiles[Constants.heirarchyFiles.lastIndex].size) {
                    Constants.selectedFiles.remove(Constants.heirarchyFiles[Constants.heirarchyFiles.lastIndex][i])
                    i++
                } else {
                    i = 0
                    Constants.heirarchyFiles.removeAt(Constants.heirarchyFiles.lastIndex)
                }

                if (Constants.heirarchyFiles.isEmpty()) {
                    break
                }
            }

            else{
                break
            }

        }

        while(Constants.parentFiles.isNotEmpty()){


            Constants.selectedFiles.remove(Constants.parentFiles[Constants.parentFiles.lastIndex])
            Constants.parentFiles.removeAt(Constants.parentFiles.lastIndex)
        }


        if(Constants.selectedFiles.isEmpty()){

            Constants.sendCount=0
            if( (context as MainActivity).ll_main_send.visibility==View.VISIBLE) {
                (context as MainActivity).ll_main_send.startAnimation((context as MainActivity).transitionDown)
                (context as MainActivity).ll_main_send.visibility = View.INVISIBLE

            }
        }

    }
}