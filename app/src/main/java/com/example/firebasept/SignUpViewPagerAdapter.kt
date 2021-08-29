package com.example.firebasept

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.firebasept.fragment.SignUpEmailFragment
import com.example.firebasept.fragment.SignUpNicknameFragment
import com.example.firebasept.fragment.SignUpPasswordFragment

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
