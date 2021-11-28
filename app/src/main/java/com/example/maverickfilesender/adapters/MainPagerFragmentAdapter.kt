package com.example.maverickfilesender.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.maverickfilesender.AppsFragment
import com.example.maverickfilesender.HolderFilesFragment
import com.example.maverickfilesender.MediaFragment

class MainPagerFragmentAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle):FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
     return 3
    }

    override fun createFragment(position: Int): Fragment {
if(position==0) {
    return AppsFragment()
}
if(position==1) {
    return MediaFragment()

}


        return HolderFilesFragment()
}

}