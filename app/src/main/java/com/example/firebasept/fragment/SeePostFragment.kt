package com.example.firebasept.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Data.PostData
import com.example.firebasept.Data.ProductData
import com.example.firebasept.Data.UserDTO
import com.example.firebasept.Model.BrandProductDataRepository
import com.example.firebasept.Model.UserDataRepository
import com.example.firebasept.PostListner
import com.example.firebasept.R
import com.example.firebasept.recyclerView.CommentViewAdapter
import com.example.firebasept.itemDeco.HorizontalItemDeco
import com.example.firebasept.recyclerView.PutOnProductInPostViewAdapter
import com.example.firebasept.viewPager.SeePostViewPagerAdapter
import com.example.firebasept.viewmodel.BrandProductViewModel
import com.example.firebasept.viewmodel.BrandProductViewModelFactory
import com.example.firebasept.viewmodel.UsStyleViewModel
import com.example.firebasept.viewmodel.UsStyleViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_detail_brand.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.fragment_us_style.*
import kotlinx.android.synthetic.main.post_delete_dialog.*
import kotlinx.android.synthetic.main.see_post_photo_design.*
import java.text.SimpleDateFormat
import java.util.*

class SeePostFragment:Fragment(), PostListner {
    private val tagList = arrayListOf<String>("#비건소재","#사회공헌/기부","#업사이클링","#친환경소재","#동물복지")
    var position = 0
    var clickState:Boolean = false
    private lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var postList: MutableList<PostData>
    lateinit var repository: PostDataRepository
    lateinit var repository2:UserDataRepository
    lateinit var repository3: BrandProductDataRepository
    lateinit var factory: UsStyleViewModelFactory
    lateinit var viewModel: UsStyleViewModel
    lateinit var factory2: BrandProductViewModelFactory
    lateinit var viewModel2: BrandProductViewModel
    lateinit var adapter:PutOnProductInPostViewAdapter
    lateinit var adapter2:SeePostViewPagerAdapter
    lateinit var itemDeco: HorizontalItemDeco
    var productList = listOf<ProductData>()
    var photoList = listOf<String>()
    var postData: PostData? = null
    var userDTO: UserDTO? = null
    lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        itemDeco =
            HorizontalItemDeco(requireContext())
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        repository = PostDataRepository(db)
        repository2 = UserDataRepository(db)
        repository3 = BrandProductDataRepository(db)
        postList = mutableListOf()
        //db = FirebaseFirestore.getInstance()
        factory = UsStyleViewModelFactory(repository, repository2, repository3)
        viewModel = ViewModelProvider(requireActivity(), factory).get(
            UsStyleViewModel::class.java
        )

        factory2 = BrandProductViewModelFactory(repository, repository2, repository3)
        viewModel2 = ViewModelProvider(requireActivity(), factory2).get(
            BrandProductViewModel::class.java
        )

        adapter2 = SeePostViewPagerAdapter(photoList)


        viewModel.initU()
        viewModel.initPR()
        //viewModel.initP()

        //comment_recyclerview_post.adapter = CommentViewAdapter(viewModel,position)
        //comment_recyclerview_post.layoutManager = LinearLayoutManager(activity)

        viewModel.postDataForPost.observe(viewLifecycleOwner, Observer {
            photoList = it.first.imageUrl
            productList = it.first.putOnProductList
            adapter = PutOnProductInPostViewAdapter(viewModel, productList)
            postData = it.first
            position = it.second
            Log.d("확인",postData!!.likes.containsKey(auth.currentUser.uid).toString())
            setView()
            setRecyclerView()
            if (postData!!.uid == auth.currentUser.uid) {
                post_menu_bar.visibility = View.VISIBLE
            } else {
                post_menu_bar.visibility = View.GONE
            }

            post_user_image.setOnClickListener {
                viewModel.setUserPageUserUid(postData!!.uid)
                findNavController().navigate(R.id.userPagePostFragment)
            }
        })


        adapter = PutOnProductInPostViewAdapter(viewModel, productList)

        // 다이얼로그 팝업
        dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.post_delete_dialog)
        // 이걸 추가해야 뒤에 흰배경 없어짐!
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        viewModel.allProductData.observe(viewLifecycleOwner, Observer {
            setRecyclerView()
            //setView()
            adapter.notifyDataSetChanged()

        })

        viewModel.allUserData.observe(viewLifecycleOwner, Observer {
            userDTO = viewModel.getUser(postData!!.uid)
            comment_recyclerview_post.adapter = CommentViewAdapter(viewModel,postData!!.uid,postData!!.timestamp)
            comment_recyclerview_post.layoutManager = LinearLayoutManager(activity)
            Glide.with(this).load(userDTO!!.userImage).centerCrop().into(post_user_image)
            adapter.notifyDataSetChanged()

        })
        viewModel.allPostData.observe(viewLifecycleOwner, Observer {
            if(postData!=null){
                postData = viewModel.searchPostData(postData!!.uid,postData!!.timestamp)!!
                like_count.text = postData!!.likeCount.toString()
                comment_count.text = postData!!.commentCount.toString()
                setView()
                adapter2.notifyDataSetChanged()
            }


        })
        viewModel.getCallBackState().observe(viewLifecycleOwner, Observer {
            (comment_recyclerview_post.adapter as CommentViewAdapter).notifyDataSetChanged()
        })

        viewModel.getCommentState().observe(viewLifecycleOwner, Observer {
            //postData = viewModel.searchPostData(postData!!.uid,postData!!.timestamp)!!
            //comment_count.text = postData.commentCount.toString()
            (comment_recyclerview_post.adapter as CommentViewAdapter).notifyDataSetChanged()
        })


        post_menu_bar.setOnClickListener{
            val bottomSheet = BottomSheetPost()
            bottomSheet.show(childFragmentManager,bottomSheet.tag)
        }
        /*post_menu_bar.setOnClickListener{
            showDialog()
        }*/
        post_heart_button_see_post.setOnClickListener {
            viewModel.clickLike(postData!!.uid,postData!!.timestamp)
            viewModel.clickLikePostAdd(postData!!.uid,postData!!.timestamp)
        }

        comment_register_button.setOnClickListener {
            if (edit_text_comment.text.toString().replace(" ", "").equals("")) { //  공백체크
                Toast.makeText(requireContext(), "댓글 내용을 입력해주세요.",
                    Toast.LENGTH_SHORT).show()
            }
            else{
                val now: Long = System.currentTimeMillis()
                val mDate = Date(now)
                val simpleDate = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                val getTime: String = simpleDate.format(mDate)
                viewModel.registerComment(postData!!.uid,postData!!.timestamp,Pair((auth.currentUser.uid + "," + getTime),edit_text_comment.text.toString()))
                edit_text_comment.text = null
            }
        }

        viewModel.postDeleteCall.observe(viewLifecycleOwner, Observer {
            showDialog()
        })

        viewModel.postEditCall.observe(viewLifecycleOwner, Observer {
            if(postData != null){
                viewModel.setEditPostCallToWrite(postData!!)
                findNavController().navigate(R.id.action_postFragment_to_writePostFragment)
            }
        })
    }

    override fun loadPage() {

        (comment_recyclerview_post.adapter as CommentViewAdapter).setItemClickListener(object:
            CommentViewAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
            }
        })

        /*postList = vi
        Log.d("얘는 되slsl?",viewModel.getPostData()!!.size.toString())
        for(p in postList){
            Log.d("얘는 되냐?",p.title)
        }*/

    }


    //"#비건소재","#사회공헌/기부","#업사이클링","#친환경소재","#동물복지"
    fun floatPostTag(list:List<String>){
        if(list.contains(tagList[0])){
            begun_tag_btn_in_detail_post.visibility = View.VISIBLE
        }
        else{
            begun_tag_btn_in_detail_post.visibility = View.GONE
        }

        if(list.contains(tagList[1])){
            social_tag_btn_in_detail_post.visibility = View.VISIBLE
        }
        else{
            social_tag_btn_in_detail_post.visibility = View.GONE
        }

        if(list.contains(tagList[2])){
            up_tag_btn_in_detail_post.visibility = View.VISIBLE
        }
        else{
            up_tag_btn_in_detail_post.visibility = View.GONE
        }

        if(list.contains(tagList[3])){
            eco_tag_btn_in_detail_post.visibility = View.VISIBLE
        }
        else{
            eco_tag_btn_in_detail_post.visibility = View.GONE
        }

        if(list.contains(tagList[4])){
            animal_tag_btn_in_detail_post.visibility = View.VISIBLE
        }
        else{
            animal_tag_btn_in_detail_post.visibility = View.GONE
        }
    }

    fun setView(){
        adapter2 = SeePostViewPagerAdapter(photoList)
        see_post_photo_view_pager.adapter = adapter2
        TabLayoutMediator(photo_indicator_tab_layout, see_post_photo_view_pager) { tab, position ->
            //Some implementation
        }.attach()


        Log.d("이미지 링크",postData!!.imageUrl[0])
        user_name_text_in_post.text = postData!!.nickname
        post_title_text.text = postData!!.title
        post_body_text.text = postData!!.description
        floatPostTag(postData!!.tagKeys)
        like_count.text = postData!!.likeCount.toString()
        comment_count.text = postData!!.commentCount.toString()

        Log.d("확인2",postData!!.likes.containsKey(auth.currentUser.uid).toString())
        if(postData!!.likes.containsKey(auth.currentUser.uid)){
            clickState = true
            post_heart_button_see_post.setImageResource(R.drawable.red_heart)
        }
        else{
            clickState = false
            post_heart_button_see_post.setImageResource(R.drawable.empty_heart)
        }
    }

    fun setRecyclerView(){
        horizontal_recyclerview_put_on_in_post.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        horizontal_recyclerview_put_on_in_post.layoutManager = linearLayoutManager
        if(itemDeco != null)
            horizontal_recyclerview_put_on_in_post.removeItemDecoration(itemDeco)
        horizontal_recyclerview_put_on_in_post.addItemDecoration(itemDeco)
        (horizontal_recyclerview_put_on_in_post.adapter as PutOnProductInPostViewAdapter).setItemClickListener(object:
            PutOnProductInPostViewAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                var PRD = viewModel.getPutOnProductData(postData!!.putOnProductList)[position]
                viewModel2.setProductDataForDetail(PRD)
                findNavController().navigate(R.id.detailProductFragment)
            }
        })
    }

    private fun showDialog(){
        dialog.show()

        dialog.post_delete_btn.setOnClickListener {
            viewModel.deletePost(postData!!.uid,postData!!.timestamp)
            dialog.dismiss()
            findNavController().navigateUp()
        }
        dialog.cancel_post_delete_btn.setOnClickListener {
            dialog.dismiss()
        }
    }
}