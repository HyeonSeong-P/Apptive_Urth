package com.example.firebasept.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.firebasept.Data.PostData
import com.example.firebasept.Data.UserDTO
import com.example.firebasept.Model.BrandProductDataRepository
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Model.UserDataRepository
import com.example.firebasept.R
import com.example.firebasept.viewPager.BPviewPagerAdapter
import com.example.firebasept.viewPager.MyPageViewPagerAdapter
import com.example.firebasept.viewmodel.MyPageViewModel
import com.example.firebasept.viewmodel.MyPageViewModelFactory
import com.example.firebasept.viewmodel.UsStyleViewModel
import com.example.firebasept.viewmodel.UsStyleViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_brand_product.*
import kotlinx.android.synthetic.main.fragment_my_page.*
import kotlinx.android.synthetic.main.fragment_user_page.*

class MyPageFragment:Fragment() {
    private val tabTextList = arrayListOf("  브랜드  ", "    상품    ","  게시글  ")
    private val tagList = arrayListOf<String>("#비건소재","#사회공헌/기부","#업사이클링","#친환경소재","#동물복지")
    lateinit var auth:FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var repository: PostDataRepository
    lateinit var repository2: UserDataRepository
    lateinit var repository3: BrandProductDataRepository
    lateinit var factory: MyPageViewModelFactory
    lateinit var viewModel: MyPageViewModel
    var userDTO:UserDTO? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        repository = PostDataRepository(db)
        repository2 = UserDataRepository(db)
        repository3 = BrandProductDataRepository(db)
        factory = MyPageViewModelFactory(repository,repository2,repository3)
        viewModel = ViewModelProvider(requireParentFragment(),factory).get(
            MyPageViewModel::class.java)

        viewModel.initU()

        view_pager_my_page.adapter = MyPageViewPagerAdapter(childFragmentManager,lifecycle) // 어댑터 불러오기
        view_pager_my_page.isUserInputEnabled = false // swipe disable
        TabLayoutMediator(tabLayout_my_page, view_pager_my_page) { //탭레이아웃과 뷰페이저 연결
                tab, position ->
            tab.text = tabTextList[position]
            // tab.icon =
        }.attach()
        view_pager_my_page.offscreenPageLimit = 2 //프래그먼트 깨지는거 방지
        buttonBehavior()

        viewModel.allUserData.observe(viewLifecycleOwner, Observer {
            userDTO = viewModel.getUser(auth.currentUser.uid)
            my_page_user_nickname.text = userDTO!!.nickName
            if(userDTO!!.userImage != ""){
                Glide.with(view).load(userDTO!!.userImage).into(my_page_user_image)
            }
            var clickTagNum = 0
            for((i, tagString) in tagList.withIndex()){
                if(userDTO!!.tags.containsKey(tagString)){
                    setTagButtonDesign(i,true)
                    clickTagNum++
                }
                else{
                    setTagButtonDesign(i,false)
                }
            }
            if(clickTagNum == 5) setAllTagButtonDesign(true)
            else setAllTagButtonDesign(false)
        })

    }

    private fun setAllTagButtonDesign(flag: Boolean){

        if(flag){
            all_tag_btn_in_my_page.setTextColor(Color.parseColor("#faf8f7"))
            all_tag_btn_in_my_page.setBackgroundResource(R.drawable.selected_circle_image)

            begun_tag_btn_in_my_page.setTextColor(Color.parseColor("#3e3a39"))
            begun_tag_btn_in_my_page.setBackgroundResource(R.drawable.circle_image)

            social_tag_btn_in_my_page.setTextColor(Color.parseColor("#3e3a39"))
            social_tag_btn_in_my_page.setBackgroundResource(R.drawable.circle_image)

            up_tag_btn_in_my_page.setTextColor(Color.parseColor("#3e3a39"))
            up_tag_btn_in_my_page.setBackgroundResource(R.drawable.circle_image)

            eco_tag_btn_in_my_page.setTextColor(Color.parseColor("#3e3a39"))
            eco_tag_btn_in_my_page.setBackgroundResource(R.drawable.circle_image)

            animal_tag_btn_in_my_page.setTextColor(Color.parseColor("#3e3a39"))
            animal_tag_btn_in_my_page.setBackgroundResource(R.drawable.circle_image)
        }
        else{
            all_tag_btn_in_my_page.setTextColor(Color.parseColor("#3e3a39"))
            all_tag_btn_in_my_page.setBackgroundResource(R.drawable.circle_image)
        }

    }
    private fun setTagButtonDesign(tagPosition:Int,flag:Boolean){
        when(tagPosition){
            1 -> {
                if(!flag){
                    social_tag_btn_in_my_page.setTextColor(Color.parseColor("#3e3a39"))
                    social_tag_btn_in_my_page.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    social_tag_btn_in_my_page.setTextColor(Color.parseColor("#faf8f7"))
                    social_tag_btn_in_my_page.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }
            2 -> {
                if(!flag){
                    up_tag_btn_in_my_page.setTextColor(Color.parseColor("#3e3a39"))
                    up_tag_btn_in_my_page.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    up_tag_btn_in_my_page.setTextColor(Color.parseColor("#faf8f7"))
                    up_tag_btn_in_my_page.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }
            3 -> {
                if(!flag){
                    eco_tag_btn_in_my_page.setTextColor(Color.parseColor("#3e3a39"))
                    eco_tag_btn_in_my_page.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    eco_tag_btn_in_my_page.setTextColor(Color.parseColor("#faf8f7"))
                    eco_tag_btn_in_my_page.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }
            4 -> {
                if(!flag){
                    animal_tag_btn_in_my_page.setTextColor(Color.parseColor("#3e3a39"))
                    animal_tag_btn_in_my_page.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    animal_tag_btn_in_my_page.setTextColor(Color.parseColor("#faf8f7"))
                    animal_tag_btn_in_my_page.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }
            else -> {
                if(!flag){
                    begun_tag_btn_in_my_page.setTextColor(Color.parseColor("#3e3a39"))
                    begun_tag_btn_in_my_page.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    begun_tag_btn_in_my_page.setTextColor(Color.parseColor("#faf8f7"))
                    begun_tag_btn_in_my_page.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }
        }
    }

    private fun buttonBehavior(){
        logout_btn.setOnClickListener {
            auth.signOut()
        }

        go_to_setting.setOnClickListener {
            findNavController().navigate(R.id.settingFragment)
        }

        // 태그 포지션
        // 0. #비건소재  1. #사회공헌/기부  2. #업사이클링  3. #친환경소재  4. #동물복지
        all_tag_btn_in_my_page.setOnClickListener {
            viewModel.clickAllTag()
        }
        begun_tag_btn_in_my_page.setOnClickListener {
            viewModel.clickTag(0)
        }
        social_tag_btn_in_my_page.setOnClickListener {
            viewModel.clickTag(1)
        }
        up_tag_btn_in_my_page.setOnClickListener {
            viewModel.clickTag(2)
        }
        eco_tag_btn_in_my_page.setOnClickListener {
            viewModel.clickTag(3)
        }
        animal_tag_btn_in_my_page.setOnClickListener {
            viewModel.clickTag(4)
        }
    }
}