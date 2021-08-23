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
import com.bumptech.glide.Glide
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Data.PostData
import com.example.firebasept.Data.UserDTO
import com.example.firebasept.Model.BrandProductDataRepository
import com.example.firebasept.Model.UserDataRepository
import com.example.firebasept.PostListner
import com.example.firebasept.R
import com.example.firebasept.itemDeco.ItemDeco
import com.example.firebasept.recyclerView.PostViewAdapter
import com.example.firebasept.viewmodel.UsStyleViewModel
import com.example.firebasept.viewmodel.UsStyleViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_us_style.*

class UsStyleFragment:Fragment(), PostListner {
    lateinit var auth:FirebaseAuth
    lateinit var db:FirebaseFirestore
    lateinit var postList: MutableList<PostData>
    lateinit var repository:PostDataRepository
    lateinit var repository2:UserDataRepository
    lateinit var repository3:BrandProductDataRepository
    lateinit var factory:UsStyleViewModelFactory
    lateinit var viewModel:UsStyleViewModel
    lateinit var itemDeco: ItemDeco
    var sortInitFlag:Int = 0
    var userDTO: UserDTO? = null
    var recyclerViewFlag = false
    lateinit var adapter:PostViewAdapter
    private val tagList = arrayListOf<String>("#비건소재","#사회공헌/기부","#업사이클링","#친환경소재","#동물복지")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_us_style, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        repository = PostDataRepository(db)
        repository2 = UserDataRepository(db)
        repository3 = BrandProductDataRepository(db)
        postList = mutableListOf()
        //db = FirebaseFirestore.getInstance()
        factory = UsStyleViewModelFactory(repository,repository2,repository3)
        viewModel = ViewModelProvider(requireActivity(),factory).get(
            UsStyleViewModel::class.java)
        itemDeco = ItemDeco(requireContext())
        adapter = PostViewAdapter(viewModel) // 이부분이 중요 밑에 갱신까지 이어짐, 아근데 이부분 자세히 공부해야할듯 발표끝나고 보자

        viewModel.initU()
        viewModel.initP()

        buttonBehavior()

        viewModel.allUserData.observe(viewLifecycleOwner, Observer {
            userDTO = viewModel.getUser(auth.currentUser.uid)
            user_nickname_urth_style.text = userDTO!!.nickName
            if(userDTO!!.userImage != ""){
                Glide.with(view).load(userDTO!!.userImage).into(urth_style_user_image)
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

            if(recyclerViewFlag){
                adapter.notifyDataSetChanged()
            }

            urth_style_user_image.setOnClickListener {
                viewModel.setUserPageUserUid(auth.currentUser.uid)
                findNavController().navigate(R.id.userPagePostFragment)
            }
        })
        viewModel.allPostData.observe(viewLifecycleOwner, Observer {

            Log.d("반응","d왔음")
            //콜백 안써도 그냥 라이브데이터 감지로 해도 되네?.. 로직은 비슷하긴 하니까 뭐..
            grid_recyclerview_post.adapter = adapter
            val gridLayoutManager = GridLayoutManager(activity, 2)
            grid_recyclerview_post.layoutManager = gridLayoutManager
            if(itemDeco != null)
                grid_recyclerview_post.removeItemDecoration(itemDeco)
            grid_recyclerview_post.addItemDecoration(itemDeco)
            (grid_recyclerview_post.adapter as PostViewAdapter).setItemClickListener(object:
                PostViewAdapter.OnItemClickListener{
                override fun onClick(v: View, position: Int) {
                    //Log.d("클릭","됐어??")
                    val PD = viewModel.getPostData()!![position]
                    viewModel.postDataForPost.setValue(Pair(PD,position))
                    findNavController().navigate(R.id.postFragment)
                }
            })
            if(!recyclerViewFlag){
                recyclerViewFlag =true
                viewModel.initSortData()
            }
            else{
                Log.d("반응왔냐???????????????","d왔음")
                adapter.notifyDataSetChanged()
            }




        })

        viewModel.sort_data.observe(viewLifecycleOwner, Observer {
            if(recyclerViewFlag){
                when(it){
                    1 -> sort_name_text.text = "최신순"
                    else -> sort_name_text.text = "인기순"
                }

                adapter.notifyDataSetChanged()
            }
        })

        viewModel.getCallBackState().observe(viewLifecycleOwner, Observer {
            //this.loadPage()
            //(grid_recyclerview_post.adapter as PostViewAdapter).notifyDataSetChanged()
        })

        viewModel.notifyCall.observe(viewLifecycleOwner, Observer {
            //(grid_recyclerview_post.adapter as PostViewAdapter).notifyDataSetChanged()
        })


        // 버튼 보였다 사라졌다
        /*var slowAppear:Animation
        var slowDisappear:Animation
        slowDisappear = AnimationUtils.loadAnimation(requireContext(),
            R.anim.fade_out
        );
        slowAppear = AnimationUtils.loadAnimation(requireContext(),
            R.anim.fade_in
        );

        //var animation:Animation =  AlphaAnimation(0, 1)
        //animation.setDuration(1000);



        val onScrollListener = object: RecyclerView.OnScrollListener() {
            var temp: Int = 0
            override fun onScrolled(@NonNull recyclerView:RecyclerView, dx:Int, dy:Int) {
                //if(temp == 1) {
                    super.onScrolled(recyclerView, dx, dy)
                    if(grid_recyclerview_post.computeVerticalScrollOffset() == 0){
                        if(go_to_buy_btn.visibility == View.GONE){
                            go_to_buy_btn.startAnimation(slowAppear)
                        }
                        go_to_buy_btn.visibility = View.VISIBLE
                    }
                    else{
                        if(go_to_buy_btn.visibility == View.VISIBLE){
                            go_to_buy_btn.startAnimation(slowDisappear)
                        }
                        go_to_buy_btn.visibility = View.GONE
                    }

                //}
            }
        }
        grid_recyclerview_post.setOnScrollListener(onScrollListener)*/

        go_to_write_post_btn.setOnClickListener {
            findNavController().navigate(R.id.writePostFragment)
        }



    }

    override fun loadPage() {
        grid_recyclerview_post.adapter = PostViewAdapter(viewModel)
        val gridLayoutManager = GridLayoutManager(activity, 2)
        grid_recyclerview_post.layoutManager = gridLayoutManager

        (grid_recyclerview_post.adapter as PostViewAdapter).setItemClickListener(object:
            PostViewAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                //Log.d("클릭","됐어??")
                val PD = viewModel.getPostData()!![position]
                viewModel.postDataForPost.setValue(Pair(PD,position))
                findNavController().navigate(R.id.postFragment)
            }
        })
    }

    private fun setAllTagButtonDesign(flag: Boolean){

        if(flag){
            all_tag_btn_in_urth_style.setTextColor(Color.parseColor("#faf8f7"))
            all_tag_btn_in_urth_style.setBackgroundResource(R.drawable.selected_circle_image)

            begun_tag_btn_in_urth_style.setTextColor(Color.parseColor("#3e3a39"))
            begun_tag_btn_in_urth_style.setBackgroundResource(R.drawable.circle_image)

            social_tag_btn_in_urth_style.setTextColor(Color.parseColor("#3e3a39"))
            social_tag_btn_in_urth_style.setBackgroundResource(R.drawable.circle_image)

            up_tag_btn_in_urth_style.setTextColor(Color.parseColor("#3e3a39"))
            up_tag_btn_in_urth_style.setBackgroundResource(R.drawable.circle_image)

            eco_tag_btn_in_urth_style.setTextColor(Color.parseColor("#3e3a39"))
            eco_tag_btn_in_urth_style.setBackgroundResource(R.drawable.circle_image)

            animal_tag_btn_in_urth_style.setTextColor(Color.parseColor("#3e3a39"))
            animal_tag_btn_in_urth_style.setBackgroundResource(R.drawable.circle_image)
        }
        else{
            all_tag_btn_in_urth_style.setTextColor(Color.parseColor("#3e3a39"))
            all_tag_btn_in_urth_style.setBackgroundResource(R.drawable.circle_image)
        }

    }
    private fun setTagButtonDesign(tagPosition:Int,flag:Boolean){
        when(tagPosition){
            1 -> {
                if(!flag){
                    social_tag_btn_in_urth_style.setTextColor(Color.parseColor("#3e3a39"))
                    social_tag_btn_in_urth_style.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    social_tag_btn_in_urth_style.setTextColor(Color.parseColor("#faf8f7"))
                    social_tag_btn_in_urth_style.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }
            2 -> {
                if(!flag){
                    up_tag_btn_in_urth_style.setTextColor(Color.parseColor("#3e3a39"))
                    up_tag_btn_in_urth_style.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    up_tag_btn_in_urth_style.setTextColor(Color.parseColor("#faf8f7"))
                    up_tag_btn_in_urth_style.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }
            3 -> {
                if(!flag){
                    eco_tag_btn_in_urth_style.setTextColor(Color.parseColor("#3e3a39"))
                    eco_tag_btn_in_urth_style.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    eco_tag_btn_in_urth_style.setTextColor(Color.parseColor("#faf8f7"))
                    eco_tag_btn_in_urth_style.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }
            4 -> {
                if(!flag){
                    animal_tag_btn_in_urth_style.setTextColor(Color.parseColor("#3e3a39"))
                    animal_tag_btn_in_urth_style.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    animal_tag_btn_in_urth_style.setTextColor(Color.parseColor("#faf8f7"))
                    animal_tag_btn_in_urth_style.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }
            else -> {
                if(!flag){
                    begun_tag_btn_in_urth_style.setTextColor(Color.parseColor("#3e3a39"))
                    begun_tag_btn_in_urth_style.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    begun_tag_btn_in_urth_style.setTextColor(Color.parseColor("#faf8f7"))
                    begun_tag_btn_in_urth_style.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }
        }
    }

    private fun buttonBehavior(){

        sort_btn_urth_style.setOnClickListener{
            val bottomSheet = BottomSheet(sort_name_text.text.toString())
            Log.d("s",sort_name_text.text.toString())
            bottomSheet.show(childFragmentManager,bottomSheet.tag)

        }

        // 태그 포지션
        // 0. #비건소재  1. #사회공헌/기부  2. #업사이클링  3. #친환경소재  4. #동물복지
        all_tag_btn_in_urth_style.setOnClickListener {
            viewModel.clickAllTag()
        }
        begun_tag_btn_in_urth_style.setOnClickListener {
            viewModel.clickTag(0)
        }
        social_tag_btn_in_urth_style.setOnClickListener {
            viewModel.clickTag(1)
        }
        up_tag_btn_in_urth_style.setOnClickListener {
            viewModel.clickTag(2)
        }
        eco_tag_btn_in_urth_style.setOnClickListener {
            viewModel.clickTag(3)
        }
        animal_tag_btn_in_urth_style.setOnClickListener {
            viewModel.clickTag(4)
        }
    }
}