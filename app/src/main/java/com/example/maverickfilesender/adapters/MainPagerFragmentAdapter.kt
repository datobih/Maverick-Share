package com.example.maverickfilesender.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.maverickfilesender.fragment.*

class MainPagerFragmentAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle):FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
     return 6
    }

    override fun createFragment(position: Int): Fragment {
if(position==0) {
    return HistoryFragment()
}
if(position==1) {
    return AppsFragment()

}



        if(position==2){
            return ImageFragment()
        }

        if(position==3){
            return VideosFragment()
        }

        if(position==4){
            return MusicFragment()
        }


        return HolderFilesFragment()
}

}