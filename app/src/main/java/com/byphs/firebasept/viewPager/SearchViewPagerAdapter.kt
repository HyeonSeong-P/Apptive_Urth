package com.byphs.firebasept.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.byphs.firebasept.fragment.SearchBrandFragment
import com.byphs.firebasept.fragment.SearchProductFragment

internal class SearchViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fm,lifecycle) {
    /*class ViewPagerAdapter(fm: Fragment):
        FragmentStateAdapter(fm) {*/
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        return when(position) {
            1 -> SearchProductFragment()
            else -> SearchBrandFragment()
        }
    }


}
