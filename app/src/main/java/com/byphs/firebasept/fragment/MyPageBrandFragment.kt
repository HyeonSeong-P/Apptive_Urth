package com.byphs.firebasept.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.byphs.firebasept.Data.PostData
import com.byphs.firebasept.Data.UserDTO
import com.byphs.firebasept.Model.BrandProductDataRepository
import com.byphs.firebasept.Model.PostDataRepository
import com.byphs.firebasept.Model.UserDataRepository
import com.byphs.firebasept.R
import com.byphs.firebasept.itemDeco.MyPageBrandItemDeco
import com.byphs.firebasept.recyclerView.MyPageBrandViewAdapter
import com.byphs.firebasept.viewmodel.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_my_page_brand.*

internal class MyPageBrandFragment: Fragment() {
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var postList: MutableList<PostData>
    lateinit var repository: PostDataRepository
    lateinit var repository2: UserDataRepository
    lateinit var repository3: BrandProductDataRepository
    lateinit var factory: MyPageViewModelFactory
    lateinit var viewModel: MyPageViewModel
    lateinit var factory2: BrandProductViewModelFactory
    lateinit var viewModel2: BrandProductViewModel
    lateinit var itemDeco: MyPageBrandItemDeco
    var userDTO: UserDTO? = null
    var recyclerViewFlag = false
    lateinit var adapter: MyPageBrandViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_page_brand, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        repository = PostDataRepository(db)
        repository2 = UserDataRepository(db)
        repository3 = BrandProductDataRepository(db)
        postList = mutableListOf()
        //db = FirebaseFirestore.getInstance()
        factory = MyPageViewModelFactory(repository,repository2,repository3)
        viewModel = ViewModelProvider(requireParentFragment(),factory).get(
            MyPageViewModel::class.java)
        factory2 = BrandProductViewModelFactory(repository,repository2,repository3)
        viewModel2 = ViewModelProvider(requireActivity(),factory2).get(
            BrandProductViewModel::class.java)

        itemDeco =
            MyPageBrandItemDeco(requireContext())
        adapter = MyPageBrandViewAdapter(viewModel) // 이부분이 중요 밑에 갱신까지 이어짐, 아근데 이부분 자세히 공부해야할듯 발표끝나고 보자


        viewModel.initB()
        viewModel.initU()

        viewModel.allUserData.observe(viewLifecycleOwner, Observer {
            userDTO = viewModel.getUser(auth.currentUser!!.uid)
            if(recyclerViewFlag){
                adapter.notifyDataSetChanged()
            }
        })
        viewModel.allBrandData.observe(viewLifecycleOwner, Observer {
            //콜백 안써도 그냥 라이브데이터 감지로 해도 되네?.. 로직은 비슷하긴 하니까 뭐..
            setRecyclerview()

        })
    }

    fun setRecyclerview(){
        grid_recyclerview_brand_my_page.adapter = adapter
        val gridLayoutManager = GridLayoutManager(activity, 4)
        grid_recyclerview_brand_my_page.layoutManager = gridLayoutManager
        if(itemDeco != null)
            grid_recyclerview_brand_my_page.removeItemDecoration(itemDeco)
        grid_recyclerview_brand_my_page.addItemDecoration(itemDeco)
        (grid_recyclerview_brand_my_page.adapter as MyPageBrandViewAdapter).setItemClickListener(object:
            MyPageBrandViewAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                //Log.d("클릭","됐어??")
                val BD = viewModel.getBrandDataInMyPage()!![position]
                viewModel2.setBrandDataForDetail(BD)
                findNavController().navigate(R.id.detailBrandFragment)
            }
        })
        recyclerViewFlag =true
    }
}