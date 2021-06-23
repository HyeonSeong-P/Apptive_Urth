package com.example.firebasept.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Data.PostData
import com.example.firebasept.PostListner
import com.example.firebasept.R
import com.example.firebasept.recyclerView.PostViewAdapter
import com.example.firebasept.viewmodel.UsStyleViewModel
import com.example.firebasept.viewmodel.UsStyleViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_us_style.*

class UsStyleFragment:Fragment(), PostListner {
    lateinit var db:FirebaseFirestore
    lateinit var postList: MutableList<PostData>
    lateinit var repository:PostDataRepository
    lateinit var factory:UsStyleViewModelFactory
    lateinit var viewModel:UsStyleViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_us_style, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        db = FirebaseFirestore.getInstance()
        repository = PostDataRepository(db)
        postList = mutableListOf()
        //db = FirebaseFirestore.getInstance()
        factory = UsStyleViewModelFactory(repository)
        viewModel = ViewModelProvider(requireParentFragment(),factory).get(
            UsStyleViewModel::class.java)

        viewModel.allPostData.observe(viewLifecycleOwner, Observer {
            //콜백 안써도 그냥 라이브데이터 감지로 해도 되네?.. 로직은 비슷하긴 하니까 뭐..
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
            /*if(viewModel.getPostData()!!.isNotEmpty()){
                grid_recyclerview_post.adapter = PostViewAdapter(viewModel)
                val gridLayoutManager = GridLayoutManager(activity, 2)
                grid_recyclerview_post.layoutManager = gridLayoutManager
            }*/
            //(grid_recyclerview_post.adapter as PostViewAdapter).notifyDataSetChanged()
        })

        viewModel.initP()

        viewModel.getCallBackState().observe(viewLifecycleOwner, Observer {
            //this.loadPage()
            //(grid_recyclerview_post.adapter as PostViewAdapter).notifyDataSetChanged()
        })

        viewModel.notifyCall.observe(viewLifecycleOwner, Observer {
            //(grid_recyclerview_post.adapter as PostViewAdapter).notifyDataSetChanged()
        })

        /*db.collection("post") // 파이어 스토어에서 데이터 들고오기
            .get()
            .addOnSuccessListener { result ->
                Log.d("성공","성공했냐?!!!!")
                for (document in result) {
                    val postDTO = document.toObject(PostData::class.java)
                    Log.d("아이디",postDTO.userID)
                    postList.add(postDTO)
                    //(grid_recyclerview_post.adapter as PostViewAdapter)!!.notifyDataSetChanged()
                }
                Log.d("몇개야!!!",postList.size.toString())
                this.loadPage()
            }
            .addOnFailureListener { exception ->
                Log.d("실패","실패했냐?!!!!")
                Log.w(TAG, "에러남!!!!!!!!!!!!!!!", exception)
            }*/







        var slowAppear:Animation
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

        grid_recyclerview_post.setOnScrollListener(onScrollListener)


        go_to_buy_btn.setOnClickListener {
            Log.d("얘는 되냐?",postList.size.toString())
        }
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

        /*postList = vi
        Log.d("얘는 되slsl?",viewModel.getPostData()!!.size.toString())
        for(p in postList){
            Log.d("얘는 되냐?",p.title)
        }*/

    }
}