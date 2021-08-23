package com.example.firebasept.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.firebasept.Model.BrandProductDataRepository
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Model.UserDataRepository
import com.example.firebasept.R
import com.example.firebasept.viewPager.BPviewPagerAdapter
import com.example.firebasept.viewPager.SearchViewPagerAdapter
import com.example.firebasept.viewmodel.HomeViewModel
import com.example.firebasept.viewmodel.HomeViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_brand_product.*
import kotlinx.android.synthetic.main.fragment_brand_product.tabLayout
import kotlinx.android.synthetic.main.fragment_search.*

class SearchBrandProductFragment:Fragment() {
    private val tabTextList = arrayListOf("  브랜드  ", "    상품    ")
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var repository: PostDataRepository
    lateinit var repository2: UserDataRepository
    lateinit var repository3: BrandProductDataRepository
    lateinit var factory: HomeViewModelFactory
    lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view_pager_search_bp.adapter = SearchViewPagerAdapter(childFragmentManager,lifecycle) // 어댑터 불러오기
        view_pager_search_bp.isUserInputEnabled = false // swipe disable
        TabLayoutMediator(tabLayout, view_pager_search_bp) { //탭레이아웃과 뷰페이저 연결
                tab, position ->
            tab.text = tabTextList[position]
            // tab.icon =
        }.attach()
        view_pager_search_bp.offscreenPageLimit = 1 //프래그먼트 깨지는거 방지

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        repository = PostDataRepository(db)
        repository2 = UserDataRepository(db)
        repository3 = BrandProductDataRepository(db)
        factory = HomeViewModelFactory(repository,repository2,repository3)
        viewModel = ViewModelProvider(requireActivity(),factory).get(
            HomeViewModel::class.java)

        viewModel.initPR()
        viewModel.initB()

        search_edit_text_in_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                viewModel.setBrandSearchText(p0.toString())
                viewModel.setProductSearchText(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }
}