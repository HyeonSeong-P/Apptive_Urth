package com.example.firebasept.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.firebasept.Data.PostData
import com.example.firebasept.Data.UserDTO
import com.example.firebasept.Model.BrandProductDataRepository
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Model.UserDataRepository
import com.example.firebasept.R
import com.example.firebasept.itemDeco.ProfilePostItemDeco
import com.example.firebasept.recyclerView.UserPagePostViewAdapter
import com.example.firebasept.viewmodel.UsStyleViewModel
import com.example.firebasept.viewmodel.UsStyleViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_user_page.*

class UserFragment:Fragment() {
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var postList: MutableList<PostData>
    lateinit var repository: PostDataRepository
    lateinit var repository2: UserDataRepository
    lateinit var repository3: BrandProductDataRepository
    lateinit var factory: UsStyleViewModelFactory
    lateinit var viewModel: UsStyleViewModel
    lateinit var itemDeco: ProfilePostItemDeco
    var sortInitFlag:Int = 0
    var userDTO: UserDTO? = null
    var recyclerViewFlag = false
    var uid:String = ""
    lateinit var adapter: UserPagePostViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_page, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        user_introduction_edit_text.isEnabled = false
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
        itemDeco =
            ProfilePostItemDeco(requireContext())
        adapter = UserPagePostViewAdapter(viewModel)

        viewModel.initU()

        viewModel.user_page_user_uid.observe(viewLifecycleOwner, Observer {
            uid = it
            if(uid == auth.currentUser.uid){
                start_edit_introduction_btn.visibility = View.VISIBLE
                go_to_setting_from_user_page_btn.visibility = View.VISIBLE
            }
            else{
                start_edit_introduction_btn.visibility = View.INVISIBLE
                go_to_setting_from_user_page_btn.visibility = View.INVISIBLE
            }
        })

        viewModel.initP()

        viewModel.allUserData.observe(viewLifecycleOwner, Observer {
            if(uid != ""){
                userDTO = viewModel.getUser(uid)
                user_name_text_in_user_page.text = userDTO!!.nickName
                if(userDTO!!.userImage != ""){
                    Glide.with(view).load(userDTO!!.userImage).into(user_page_user_image)
                }
                user_introduction_edit_text.setText(userDTO!!.userIntroduction)

                go_to_setting_from_user_page_btn.setOnClickListener {
                    findNavController().navigate(R.id.settingFragment)
                }

                start_edit_introduction_btn.setOnClickListener {
                    user_introduction_edit_text.isEnabled = true // 이걸로 에딧텍스트 수정가능 불가능 만들 수 있음!
                    complete_edit_introduction_btn.visibility = View.VISIBLE

                    user_introduction_edit_text.requestFocus()
                    //키보드 보이게 하는 부분
                    val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    //imm.hideSoftInputFromWindow(user_introduction_edit_text.windowToken, 0)
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
                    it.visibility = View.GONE
                }
                complete_edit_introduction_btn.setOnClickListener {
                    viewModel.editUserIntroduction(user_introduction_edit_text.text.toString())
                    user_introduction_edit_text.isEnabled = false
                    start_edit_introduction_btn.visibility = View.VISIBLE
                    //키보드 보이게 하는 부분
                    val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(user_introduction_edit_text.windowToken, 0)
                    it.visibility = View.GONE
                }



            }


        })

        viewModel.allPostData.observe(viewLifecycleOwner, Observer {
            grid_recyclerview_user_page_post.adapter = adapter
            val gridLayoutManager = GridLayoutManager(activity, 3)
            grid_recyclerview_user_page_post.layoutManager = gridLayoutManager
            if(itemDeco != null)
                grid_recyclerview_user_page_post.removeItemDecoration(itemDeco)
            grid_recyclerview_user_page_post.addItemDecoration(itemDeco)
            (grid_recyclerview_user_page_post.adapter as UserPagePostViewAdapter).setItemClickListener(object:
                UserPagePostViewAdapter.OnItemClickListener{
                override fun onClick(v: View, position: Int) {
                    val PD = viewModel.getUserPagePostData()!![position]
                    viewModel.postDataForPost.setValue(Pair(PD,position))
                    findNavController().navigate(R.id.postFragment)
                }
            })

        })
    }
}