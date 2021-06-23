package com.example.firebasept.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Data.PostData
import com.example.firebasept.PostListner
import com.example.firebasept.R
import com.example.firebasept.recyclerView.CommentViewAdapter
import com.example.firebasept.viewmodel.UsStyleViewModel
import com.example.firebasept.viewmodel.UsStyleViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_post.*
import java.text.SimpleDateFormat
import java.util.*

class SeePostFragment:Fragment(), PostListner {
    var position = 0
    var clickState:Boolean = false
    private lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var postList: MutableList<PostData>
    lateinit var repository: PostDataRepository
    lateinit var factory: UsStyleViewModelFactory
    lateinit var viewModel: UsStyleViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        repository = PostDataRepository(db)
        postList = mutableListOf()
        //db = FirebaseFirestore.getInstance()
        factory = UsStyleViewModelFactory(repository)
        viewModel = ViewModelProvider(requireParentFragment(),factory).get(
            UsStyleViewModel::class.java)

        comment_recyclerview_post.adapter = CommentViewAdapter(viewModel,position)
        comment_recyclerview_post.layoutManager = LinearLayoutManager(activity)

        viewModel.allPostData.observe(viewLifecycleOwner, Observer {

        })
        viewModel.getCallBackState().observe(viewLifecycleOwner, Observer {
            (comment_recyclerview_post.adapter as CommentViewAdapter).notifyDataSetChanged()
        })
        viewModel.getLikeState().observe(viewLifecycleOwner, Observer {
            if(clickState){
                post_heart_button.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                clickState = false
            }
            else{
                post_heart_button.setImageResource(R.drawable.ic_baseline_favorite_24)
                clickState = true
            }
        })
        viewModel.getCommentState().observe(viewLifecycleOwner, Observer {
            (comment_recyclerview_post.adapter as CommentViewAdapter).notifyDataSetChanged()
        })
        viewModel.postDataForPost.observe(viewLifecycleOwner, Observer {
            Log.d("포스트정보",it.first.imageUrl)
            position = it.second
            Glide.with(this).load(it.first.imageUrl).into(post_imageView)
            user_name_text_in_post.text = it.first.userID
            post_title_text.text = it.first.title
            post_body_text.text = it.first.description
            if(it.first.likes.containsKey(auth.uid)){
                clickState = true
                post_heart_button.setImageResource(R.drawable.ic_baseline_favorite_24)
            }
            else{
                clickState = false
                post_heart_button.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
        })

        post_heart_button.setOnClickListener {
            viewModel.clickLike(position)
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
                viewModel.registerComment(position,Pair((auth.currentUser.email + "," + getTime),edit_text_comment.text.toString()))
            }
        }


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
}