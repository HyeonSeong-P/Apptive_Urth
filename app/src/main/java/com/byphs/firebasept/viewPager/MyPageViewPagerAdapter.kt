package com.byphs.firebasept.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.byphs.firebasept.fragment.*

internal class MyPageViewPagerAdapter (fm: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fm,lifecycle) {
    /*class ViewPagerAdapter(fm: Fragment):
        FragmentStateAdapter(fm) {*/
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {

        return when(position) {
            1 -> MyPageProductFragment()
            2 -> MyPagePostFragment()
            else -> MyPageBrandFragment()
        }
    }


}