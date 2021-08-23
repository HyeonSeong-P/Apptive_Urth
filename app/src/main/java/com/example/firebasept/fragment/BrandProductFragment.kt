package com.example.firebasept.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.firebasept.R
import com.example.firebasept.viewPager.BPviewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_brand_product.*
import kotlinx.android.synthetic.main.fragment_home.*

class BrandProductFragment:Fragment(){
    private val tabTextList = arrayListOf("  브랜드  ", "    상품    ")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_brand_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view_pager_bp.adapter = BPviewPagerAdapter(childFragmentManager,lifecycle) // 어댑터 불러오기
        view_pager_bp.isUserInputEnabled = false // swipe disable
        TabLayoutMediator(tabLayout, view_pager_bp) { //탭레이아웃과 뷰페이저 연결
                tab, position ->
            tab.text = tabTextList[position]
            // tab.icon =
        }.attach()
        view_pager_bp.offscreenPageLimit = 1 //프래그먼트 깨지는거 방지

        bp_search_btn.setOnClickListener {
            findNavController().navigate(R.id.searchBrandProductFragment)
        }
    }
}