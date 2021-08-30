package com.example.firebasept.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.firebasept.Data.UserDTO
import com.example.firebasept.Model.BrandProductDataRepository
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Model.UserDataRepository
import com.example.firebasept.R
import com.example.firebasept.recyclerView.BrandViewAdapter
import com.example.firebasept.itemDeco.ItemDeco
import com.example.firebasept.viewmodel.BrandProductViewModel
import com.example.firebasept.viewmodel.BrandProductViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_brand.*

internal class BrandFragment:Fragment() {
    private val tagList = arrayListOf<String>("#비건소재","#사회공헌/기부","#업사이클링","#친환경소재","#동물복지")
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var repository: PostDataRepository
    lateinit var repository2: UserDataRepository
    lateinit var repository3: BrandProductDataRepository
    lateinit var factory: BrandProductViewModelFactory
    lateinit var viewModel: BrandProductViewModel
    var userDTO: UserDTO? = null
    var recyclerViewFlag = false
    lateinit var adapter: BrandViewAdapter
    lateinit var itemDeco: ItemDeco
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_brand, container, false)
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
        itemDeco = ItemDeco(requireContext())
        adapter = BrandViewAdapter(viewModel) // 이부분이 중요 밑에 갱신까지 이어짐, 아근데 이부분 자세히 공부해야할듯 발표끝나고 보자

        viewModel.initU()
        viewModel.initB()

        buttonBehavior()

        viewModel.allBrandData.observe(viewLifecycleOwner, Observer {
            //콜백 안써도 그냥 라이브데이터 감지로 해도 되네?.. 로직은 비슷하긴 하니까 뭐..
            grid_recyclerview_brand.adapter = adapter
            val gridLayoutManager = GridLayoutManager(activity, 2)
            grid_recyclerview_brand.layoutManager = gridLayoutManager
            if(itemDeco != null)
                grid_recyclerview_brand.removeItemDecoration(itemDeco)
            grid_recyclerview_brand.addItemDecoration(itemDeco)
            (grid_recyclerview_brand.adapter as BrandViewAdapter).setItemClickListener(object:
                BrandViewAdapter.OnItemClickListener{
                override fun onClick(v: View, position: Int) {
                    //Log.d("클릭","됐어??")
                    val BD = viewModel.getBrandData()!![position]
                    viewModel.setBrandDataForDetail(BD)
                    findNavController().navigate(R.id.detailBrandFragment)
                }
            })

            if(!recyclerViewFlag){
                recyclerViewFlag =true
                viewModel.initSortDataBrand()
            }
            else{
                Log.d("반응왔냐???????????????","d왔음")
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.allUserData.observe(viewLifecycleOwner, Observer {
            userDTO = viewModel.getUser(auth.currentUser.uid)
            //my_page_user_nickname.text = userDTO!!.nickName
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

            if(recyclerViewFlag){
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.sortDataBrand.observe(viewLifecycleOwner, Observer {
            if(recyclerViewFlag){
                when(it){
                    1 -> sort_name_text_brand.text = "최신순"
                    else -> sort_name_text_brand.text = "인기순"
                }
                adapter.notifyDataSetChanged()
            }
        })
    }


    private fun setAllTagButtonDesign(flag: Boolean){

        if(flag){
            all_tag_btn_in_brand.setTextColor(Color.parseColor("#faf8f7"))
            all_tag_btn_in_brand.setBackgroundResource(R.drawable.selected_circle_image)

            begun_tag_btn_in_brand.setTextColor(Color.parseColor("#3e3a39"))
            begun_tag_btn_in_brand.setBackgroundResource(R.drawable.circle_image)

            social_tag_btn_in_brand.setTextColor(Color.parseColor("#3e3a39"))
            social_tag_btn_in_brand.setBackgroundResource(R.drawable.circle_image)

            up_tag_btn_in_brand.setTextColor(Color.parseColor("#3e3a39"))
            up_tag_btn_in_brand.setBackgroundResource(R.drawable.circle_image)

            eco_tag_btn_in_brand.setTextColor(Color.parseColor("#3e3a39"))
            eco_tag_btn_in_brand.setBackgroundResource(R.drawable.circle_image)

            animal_tag_btn_in_brand.setTextColor(Color.parseColor("#3e3a39"))
            animal_tag_btn_in_brand.setBackgroundResource(R.drawable.circle_image)
        }
        else{
            all_tag_btn_in_brand.setTextColor(Color.parseColor("#3e3a39"))
            all_tag_btn_in_brand.setBackgroundResource(R.drawable.circle_image)
        }

    }
    private fun setTagButtonDesign(tagPosition:Int,flag:Boolean){
        when(tagPosition){
            1 -> {
                if(!flag){
                    social_tag_btn_in_brand.setTextColor(Color.parseColor("#3e3a39"))
                    social_tag_btn_in_brand.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    social_tag_btn_in_brand.setTextColor(Color.parseColor("#faf8f7"))
                    social_tag_btn_in_brand.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }
            2 -> {
                if(!flag){
                    up_tag_btn_in_brand.setTextColor(Color.parseColor("#3e3a39"))
                    up_tag_btn_in_brand.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    up_tag_btn_in_brand.setTextColor(Color.parseColor("#faf8f7"))
                    up_tag_btn_in_brand.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }
            3 -> {
                if(!flag){
                    eco_tag_btn_in_brand.setTextColor(Color.parseColor("#3e3a39"))
                    eco_tag_btn_in_brand.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    eco_tag_btn_in_brand.setTextColor(Color.parseColor("#faf8f7"))
                    eco_tag_btn_in_brand.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }
            4 -> {
                if(!flag){
                    animal_tag_btn_in_brand.setTextColor(Color.parseColor("#3e3a39"))
                    animal_tag_btn_in_brand.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    animal_tag_btn_in_brand.setTextColor(Color.parseColor("#faf8f7"))
                    animal_tag_btn_in_brand.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }
            else -> {
                if(!flag){
                    begun_tag_btn_in_brand.setTextColor(Color.parseColor("#3e3a39"))
                    begun_tag_btn_in_brand.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    begun_tag_btn_in_brand.setTextColor(Color.parseColor("#faf8f7"))
                    begun_tag_btn_in_brand.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }
        }
    }

    private fun buttonBehavior(){

        sort_btn_brand.setOnClickListener{
            val bottomSheet = BottomSheetBrand(sort_name_text_brand.text.toString())
            Log.d("s",sort_name_text_brand.text.toString())
            bottomSheet.show(childFragmentManager,bottomSheet.tag)

        }

        // 태그 포지션
        // 0. #비건소재  1. #사회공헌/기부  2. #업사이클링  3. #친환경소재  4. #동물복지
        all_tag_btn_in_brand.setOnClickListener {
            viewModel.clickAllTag()
        }
        begun_tag_btn_in_brand.setOnClickListener {
            viewModel.clickTag(0)
        }
        social_tag_btn_in_brand.setOnClickListener {
            viewModel.clickTag(1)
        }
        up_tag_btn_in_brand.setOnClickListener {
            viewModel.clickTag(2)
        }
        eco_tag_btn_in_brand.setOnClickListener {
            viewModel.clickTag(3)
        }
        animal_tag_btn_in_brand.setOnClickListener {
            viewModel.clickTag(4)
        }
    }
}