package com.example.firebasept.fragment

import SnapToBlock
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SnapHelper
import com.bumptech.glide.Glide
import com.example.firebasept.Data.BrandData
import com.example.firebasept.Data.UserDTO
import com.example.firebasept.Model.BrandProductDataRepository
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Model.UserDataRepository
import com.example.firebasept.R
import com.example.firebasept.itemDeco.CirclePagerIndicatorDecoration
import com.example.firebasept.itemDeco.HorizontalItemDeco
import com.example.firebasept.recyclerView.MajorProductViewAdapter
import com.example.firebasept.viewPager.MajorProductViewPagerAdapter
import com.example.firebasept.viewmodel.BrandProductViewModel
import com.example.firebasept.viewmodel.BrandProductViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_detail_brand.*
import kotlinx.android.synthetic.main.fragment_detail_product.*
import kotlinx.android.synthetic.main.fragment_home.*


class DetailBrandFragment: Fragment() {
    private val tagList = arrayListOf<String>("#비건소재","#사회공헌/기부","#업사이클링","#친환경소재","#동물복지")
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var repository: PostDataRepository
    lateinit var repository2: UserDataRepository
    lateinit var repository3: BrandProductDataRepository
    lateinit var factory: BrandProductViewModelFactory
    lateinit var viewModel: BrandProductViewModel
    var userDTO: UserDTO? = null
    var brandData: BrandData? = null
    var recyclerViewFlag = false
    lateinit var adapter: MajorProductViewAdapter
    lateinit var adapter2: MajorProductViewPagerAdapter
    lateinit var itemDeco: HorizontalItemDeco
    lateinit var itemDeco2: CirclePagerIndicatorDecoration


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_brand, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        repository = PostDataRepository(db)
        repository2 = UserDataRepository(db)
        repository3 = BrandProductDataRepository(db)
        factory = BrandProductViewModelFactory(repository,repository2,repository3)
        viewModel = ViewModelProvider(requireActivity(),factory).get(
            BrandProductViewModel::class.java)

        viewModel.initU()
        viewModel.initB()
        viewModel.initPR()

        itemDeco =
            HorizontalItemDeco(requireContext())
        itemDeco2 =
            CirclePagerIndicatorDecoration()

        viewModel.brandDataForDetail.observe(viewLifecycleOwner, Observer {
            brandData = it
            adapter = MajorProductViewAdapter(viewModel,it.brandName)
            adapter2 = MajorProductViewPagerAdapter(viewModel,it.brandName)
            setView()
        })

        viewModel.allUserData.observe(viewLifecycleOwner, Observer {
            userDTO = viewModel.getUser(auth.currentUser.uid)
        })

        viewModel.allBrandData.observe(viewLifecycleOwner, Observer {
            Log.d("ㅁㄴㅇㅇㅇㅇ",brandData!!.brandName);
            //왜 네비게이트 이후에도 브랜드데이터가 유지가 될까?... 뷰모델 생명주기 때문인ㄴ가?
            brandData = viewModel.getBrand(brandData!!.brandName)
            adapter = MajorProductViewAdapter(viewModel,brandData!!.brandName)

            setView()
            go_to_brand_website_btn.setOnClickListener {
                //Log.d("사이트",productData!!.purchaseLink)
                /*webview_product.visibility = View.VISIBLE
                webview_product.loadUrl(productData!!.purchaseLink)*/
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(brandData!!.brandLink)
                startActivity(i)
            }
        })

        viewModel.allProductData.observe(viewLifecycleOwner, Observer {
            setViewPager()
            setRecyclerView()

            if(!recyclerViewFlag){
                recyclerViewFlag =true
                viewModel.initSortDataProduct()
            }
            else{
                //Log.d("반응왔냐???????????????","d왔음")
                adapter.notifyDataSetChanged()
            }
        })


        detail_brand_heart_button.setOnClickListener {
            viewModel.clickLikeBrand(brandData!!.brandName)
            viewModel.clickLikeBrandAdd(brandData!!.brandName)
        }

        // 버튼 보였다 사라졌다
        var slowAppear: Animation
        var slowDisappear:Animation
        slowDisappear = AnimationUtils.loadAnimation(requireContext(),
            R.anim.fade_out
        );
        slowAppear = AnimationUtils.loadAnimation(requireContext(),
            R.anim.fade_in
        );

        //var animation:Animation =  AlphaAnimation(0, 1)
        //animation.setDuration(1000);

        detail_brand_scroll.setOnScrollChangeListener { view: View, i: Int, i1: Int, i2: Int, i3: Int ->
            if(detail_brand_scroll.scrollY == 0){
                if(go_to_brand_website_btn.visibility == View.GONE){
                    go_to_brand_website_btn.startAnimation(slowAppear)
                }
                go_to_brand_website_btn.visibility = View.VISIBLE
            }
            else{
                if(go_to_brand_website_btn.visibility == View.VISIBLE){
                    go_to_brand_website_btn.startAnimation(slowDisappear)
                }
                go_to_brand_website_btn.visibility = View.GONE
            }
        }

        back_btn_detail_brand.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.majorProductCall.observe(viewLifecycleOwner, Observer {
            viewModel.setProductDataForDetail(it!!)
            findNavController().navigate(R.id.detailProductFragment)
        })

    }

    fun setRecyclerView(){
        horizontal_recyclerview_detail_brand.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        horizontal_recyclerview_detail_brand.layoutManager = linearLayoutManager
        val snapHelper: SnapHelper = SnapToBlock(3)
        if (horizontal_recyclerview_detail_brand.onFlingListener == null) snapHelper.attachToRecyclerView(
            horizontal_recyclerview_detail_brand
        )
        if(itemDeco != null)
            horizontal_recyclerview_detail_brand.removeItemDecoration(itemDeco)
        horizontal_recyclerview_detail_brand.addItemDecoration(itemDeco)
        horizontal_recyclerview_detail_brand.addItemDecoration(itemDeco2)

        (horizontal_recyclerview_detail_brand.adapter as MajorProductViewAdapter).setItemClickListener(object:
            MajorProductViewAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                //Log.d("클릭","됐어??")
                val PRD = viewModel.getMajorProductData(brandData!!.brandName)!![position]
                viewModel.setProductDataForDetail(PRD)
                findNavController().navigate(R.id.detailProductFragment)
            }
        })
    }

    //"#비건소재","#사회공헌/기부","#업사이클링","#친환경소재","#동물복지"
    fun setView(){
        Glide.with(this).load(brandData!!.logoImage).fitCenter().into(brand_image_detail_brand)
        Glide.with(this).load(brandData!!.backgroundImage).centerCrop().into(brand_background_image)
        brand_name_text_detail_brand.text = brandData!!.brandName
        brand_korean_name_text_detail_brand.text = brandData!!.koreanBrandName
        brand_description_text_detail_brand.text = brandData!!.description
        detail_brand_like_count.text = brandData!!.likeCount.toString()
        if(brandData!!.likes.containsKey(auth.currentUser.uid)){
            detail_brand_heart_button.setImageResource(R.drawable.red_heart)
        }
        else{
            detail_brand_heart_button.setImageResource(R.drawable.empty_heart)
        }

        if(brandData!!.tagKeys.contains(tagList[0])){
            begun_tag_btn_in_detail_brand.visibility = View.VISIBLE
        }
        if(brandData!!.tagKeys.contains(tagList[1])){
            social_tag_btn_in_detail_brand.visibility = View.VISIBLE
        }
        if(brandData!!.tagKeys.contains(tagList[2])){
            up_tag_btn_in_detail_brand.visibility = View.VISIBLE
        }
        if(brandData!!.tagKeys.contains(tagList[3])){
            eco_tag_btn_in_detail_brand.visibility = View.VISIBLE
        }
        if(brandData!!.tagKeys.contains(tagList[4])){
            animal_tag_btn_in_detail_brand.visibility = View.VISIBLE
        }
    }

    fun setViewPager(){
        major_product_view_pager_detail_brand.adapter = adapter2
        TabLayoutMediator(detail_brand_indicator_tab_layout, major_product_view_pager_detail_brand) { tab, position ->
            //Some implementation
        }.attach()
    }

}