package com.byphs.firebasept.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.byphs.firebasept.Data.ProductData
import com.byphs.firebasept.Data.UserDTO
import com.byphs.firebasept.Model.BrandProductDataRepository
import com.byphs.firebasept.Model.PostDataRepository
import com.byphs.firebasept.Model.UserDataRepository
import com.byphs.firebasept.R
import com.byphs.firebasept.itemDeco.CirclePagerIndicatorDecoration2
import com.byphs.firebasept.itemDeco.HorizontalItemDeco
import com.byphs.firebasept.itemDeco.HorizontalItemDeco2
import com.byphs.firebasept.recyclerView.*
import com.byphs.firebasept.viewPager.HomeProductViewPagerAdapter
import com.byphs.firebasept.viewmodel.*
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*

internal class HomeFragment: Fragment() {
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var repository: PostDataRepository
    lateinit var repository2: UserDataRepository
    lateinit var repository3: BrandProductDataRepository
    lateinit var factory: HomeViewModelFactory
    lateinit var factory2: BrandProductViewModelFactory
    lateinit var factory3: UsStyleViewModelFactory
    lateinit var viewModel: HomeViewModel
    lateinit var viewModel2: BrandProductViewModel
    lateinit var viewModel3: UsStyleViewModel
    lateinit var adapter1:HomeBrandViewAdapter
    lateinit var adapter2: HomeProductViewAdapter
    lateinit var adapter3: HomePostViewAdapter
    lateinit var adapter4: HomeProductViewPagerAdapter
    lateinit var itemDeco: HorizontalItemDeco
    lateinit var itemDeco2: CirclePagerIndicatorDecoration2
    lateinit var itemDeco3: HorizontalItemDeco2
    var userDTO: UserDTO? = null
    var productData: ProductData? = null
    var recyclerViewFlag = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            requireActivity().finishAffinity()
        }
        Glide.with(requireContext()).load("https://vegantigerkorea.com/web/upload/NNEditor/20210323/19_shop1_165755.jpg" ).into(home_main_image)// url로 불러올때 이거쓰자! 이게 좋다!! 글라이드 개꿀!!

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        repository = PostDataRepository(db)
        repository2 = UserDataRepository(db)
        repository3 = BrandProductDataRepository(db)
        factory = HomeViewModelFactory(repository,repository2,repository3)
        viewModel = ViewModelProvider(requireActivity(),factory).get(
            HomeViewModel::class.java)
        factory2 = BrandProductViewModelFactory(repository,repository2,repository3)
        viewModel2 = ViewModelProvider(requireActivity(),factory2).get(
            BrandProductViewModel::class.java)
        factory3 = UsStyleViewModelFactory(repository,repository2,repository3)
        viewModel3 = ViewModelProvider(requireActivity(),factory3).get(
            UsStyleViewModel::class.java)

        itemDeco =
            HorizontalItemDeco(requireContext())
        itemDeco2 =
            CirclePagerIndicatorDecoration2()
        itemDeco3 =
            HorizontalItemDeco2(requireContext())

        adapter1 = HomeBrandViewAdapter(viewModel)
        adapter2 = HomeProductViewAdapter(viewModel)
        adapter3 = HomePostViewAdapter(viewModel)
        adapter4 = HomeProductViewPagerAdapter(viewModel)

        viewModel.initU()
        viewModel.initB()
        viewModel.initPR()
        viewModel.initP()



        home_search_btn.setOnClickListener {
            findNavController().navigate(R.id.searchBrandProductFragment)
        }


        viewModel.allBrandData.observe(viewLifecycleOwner, Observer {
            setBrandRecyclerView()
        })

        viewModel.allProductData.observe(viewLifecycleOwner, Observer {

            view_pager_home_product.setOnTouchListener { view, motionEvent ->
                view.parent.requestDisallowInterceptTouchEvent(true)

                /*if(motionEvent.action == MotionEvent.ACTION_UP || motionEvent.action == MotionEvent.ACTION_DOWN || motionEvent.action == MotionEvent.ACTION_MOVE) {
                    scrollView_home.requestDisallowInterceptTouchEvent(true)
                    return@setOnTouchListener false
                }*/
                false
            }


            view_pager_home_product.adapter = adapter4
            view_pager_home_product.isNestedScrollingEnabled = false
            TabLayoutMediator(home_indicator_tab_layout, view_pager_home_product) { tab, position ->
                //Some implementation
            }.attach()

            adapter4.notifyDataSetChanged()

        })

        viewModel.moreNewOrNormalCall.observe(viewLifecycleOwner, Observer {
            if(it!!.first){
                findNavController().navigate(R.id.moreNewProductFragment)
            }
            else{
                viewModel2.setProductDataForDetail(it!!.second)
                findNavController().navigate(R.id.detailProductFragment)
            }
        })

        viewModel.allPostData.observe(viewLifecycleOwner, Observer {
            setPostRecyclerView()
        })
    }

    fun setPostRecyclerView(){
        home_linear_post_recyclerview.adapter = adapter3
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        home_linear_post_recyclerview.layoutManager = linearLayoutManager
        if(itemDeco3 != null)
            home_linear_post_recyclerview.removeItemDecoration(itemDeco3)
        home_linear_post_recyclerview.addItemDecoration(itemDeco3)
        (home_linear_post_recyclerview.adapter as HomePostViewAdapter).setItemClickListener(object:
            HomePostViewAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                //Log.d("클릭","됐어??")
                val PD = viewModel.getBestPostData()!![position]
                viewModel3.setPostDataForSee(PD,position)
                findNavController().navigate(R.id.postFragment)
            }
        })
    }

    fun setBrandRecyclerView(){
        best_brand_recyclerview.adapter = adapter1
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        best_brand_recyclerview.layoutManager = linearLayoutManager
        if(itemDeco3 != null)
            best_brand_recyclerview.removeItemDecoration(itemDeco3)
        best_brand_recyclerview.addItemDecoration(itemDeco3)

        (best_brand_recyclerview.adapter as HomeBrandViewAdapter).setItemClickListener(object:
            HomeBrandViewAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                //Log.d("클릭","됐어??")
                val BD = viewModel.getBestBrandData()!![position]
                viewModel2.setBrandDataForDetail(BD)
                findNavController().navigate(R.id.detailBrandFragment)
            }
        })
        recyclerViewFlag = true
    }
}