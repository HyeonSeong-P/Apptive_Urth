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
import com.example.firebasept.itemDeco.ItemDeco
import com.example.firebasept.recyclerView.SearchProductViewAdapter
import com.example.firebasept.viewmodel.BrandProductViewModel
import com.example.firebasept.viewmodel.BrandProductViewModelFactory
import com.example.firebasept.viewmodel.HomeViewModel
import com.example.firebasept.viewmodel.HomeViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_search_product.*

class SearchProductFragment:Fragment() {
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var repository: PostDataRepository
    lateinit var repository2: UserDataRepository
    lateinit var repository3: BrandProductDataRepository
    lateinit var factory: HomeViewModelFactory
    lateinit var viewModel: HomeViewModel
    lateinit var factory2: BrandProductViewModelFactory
    lateinit var viewModel2: BrandProductViewModel
    lateinit var adapter:SearchProductViewAdapter
    lateinit var itemDeco: ItemDeco
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_product, container, false)
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

        itemDeco = ItemDeco(requireContext())
        adapter = SearchProductViewAdapter(viewModel)


        grid_recyclerview_product_search.adapter = adapter
        val gridLayoutManager = GridLayoutManager(activity,2)
        grid_recyclerview_product_search.layoutManager = gridLayoutManager

        if(itemDeco != null)
            grid_recyclerview_product_search.removeItemDecoration(itemDeco)
        grid_recyclerview_product_search.addItemDecoration(itemDeco)
        (grid_recyclerview_product_search.adapter as SearchProductViewAdapter).setItemClickListener(object:
            SearchProductViewAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                val PRD = viewModel.getSearchProductData()!![position]
                viewModel.setProductDataForDetail(PRD)
                findNavController().navigate(R.id.detailProductFragment)
                //Log.d("클릭","됐어??")
                /*if(position == 8 && viewModel.getNewProductData()!!.size > 9){
                    findNavController().navigate(R.id.moreNewProductFragment)
                }
                else{
                    val PRD = viewModel.getNewProductData()!![position]
                    viewModel.setProductDataForDetail(PRD)
                    findNavController().navigate(R.id.detailProductFragment)
                }*/
            }
        })

        viewModel.allProductData.observe(viewLifecycleOwner, Observer {
            adapter.notifyDataSetChanged()
        })

        viewModel.productSearchText.observe(viewLifecycleOwner, Observer {
            adapter.notifyDataSetChanged()
        })
    }
}