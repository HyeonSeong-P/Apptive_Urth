package com.example.firebasept.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.firebasept.Model.BrandProductDataRepository
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Model.UserDataRepository
import com.example.firebasept.R
import com.example.firebasept.recyclerView.*
import com.example.firebasept.viewmodel.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_more_new_product.*

class MoreNewProductFragment:Fragment() {
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var repository: PostDataRepository
    lateinit var repository2: UserDataRepository
    lateinit var repository3: BrandProductDataRepository
    lateinit var factory: HomeViewModelFactory
    lateinit var viewModel: HomeViewModel
    lateinit var factory2: BrandProductViewModelFactory
    lateinit var viewModel2: BrandProductViewModel
    lateinit var adapter: MoreNewProductViewAdapter
    lateinit var itemDeco: MoreNewProductItemDeco
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_more_new_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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

        adapter = MoreNewProductViewAdapter(viewModel)
        itemDeco = MoreNewProductItemDeco(requireContext())

        viewModel.initPR()

        viewModel.allProductData.observe(viewLifecycleOwner, Observer {
            grid_recyclerview_more_new_product.adapter = adapter
            val gridLayoutManager = GridLayoutManager(activity,3)
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    // 카페고리 header 나 footer 영역은 grid 4칸을 전부 차지하여 표시
                    if (viewModel.getMoreNewProductData()!![position].productName == ""
                    ) {
                        return gridLayoutManager.spanCount
                    }
                    return 1 // 나머지는 1칸만 사용
                }
            }
            grid_recyclerview_more_new_product.layoutManager = gridLayoutManager

            if(itemDeco != null)
                grid_recyclerview_more_new_product.removeItemDecoration(itemDeco)
            grid_recyclerview_more_new_product.addItemDecoration(itemDeco)
            (grid_recyclerview_more_new_product.adapter as MoreNewProductViewAdapter).setItemClickListener(object:
                MoreNewProductViewAdapter.OnItemClickListener{
                override fun onClick(v: View, position: Int) {
                    //Log.d("클릭","됐어??")
                    val PRD = viewModel.getMoreNewProductData()!![position]
                    if(PRD.productName != ""){
                        viewModel2.setProductDataForDetail(PRD)
                        findNavController().navigate(R.id.detailProductFragment)
                    }
                }
            })
        })
    }
}