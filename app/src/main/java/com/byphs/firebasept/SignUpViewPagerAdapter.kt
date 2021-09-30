package com.byphs.firebasept

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.byphs.firebasept.fragment.SignUpEmailFragment
import com.byphs.firebasept.fragment.SignUpNicknameFragment
import com.byphs.firebasept.fragment.SignUpPasswordFragment

internal class SignUpViewPagerAdapter (fm: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fm,lifecycle) {
    /*class ViewPagerAdapter(fm: Fragment):
        FragmentStateAdapter(fm) {*/
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {

        return when(position) {
            1 -> SignUpPasswordFragment()
            2 -> SignUpNicknameFragment()
            else -> SignUpEmailFragment()
        }
    }


}
