package com.example.firebasept.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.firebasept.fragment.BrandFragment
import com.example.firebasept.fragment.ProductFragment

internal class BPviewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fm,lifecycle) {
    /*class ViewPagerAdapter(fm: Fragment):
        FragmentStateAdapter(fm) {*/
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        return when(position) {
            1 -> ProductFragment()
            else -> BrandFragment()
        }
    }


}
