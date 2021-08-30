package com.example.firebasept.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasept.Model.BrandProductDataRepository
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Model.UserDataRepository
import com.example.firebasept.R
import com.example.firebasept.itemDeco.SearchBrandItemDeco
import com.example.firebasept.recyclerView.*
import com.example.firebasept.viewmodel.BrandProductViewModel
import com.example.firebasept.viewmodel.BrandProductViewModelFactory
import com.example.firebasept.viewmodel.HomeViewModel
import com.example.firebasept.viewmodel.HomeViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_search_brand.*

internal class SearchBrandFragment: Fragment() {
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var repository: PostDataRepository
    lateinit var repository2: UserDataRepository
    lateinit var repository3: BrandProductDataRepository
    lateinit var factory: HomeViewModelFactory
    lateinit var viewModel: HomeViewModel
    lateinit var factory2: BrandProductViewModelFactory
    lateinit var viewModel2: BrandProductViewModel
    lateinit var adapter: SearchBrandViewAdapter
    lateinit var itemDeco: SearchBrandItemDeco

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_brand, container, false)
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

        adapter = SearchBrandViewAdapter(viewModel)
        itemDeco =
            SearchBrandItemDeco(requireContext())

        recyclerview_brand_search.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerview_brand_search.layoutManager = linearLayoutManager
        if(itemDeco != null)
            recyclerview_brand_search.removeItemDecoration(itemDeco)
        recyclerview_brand_search.addItemDecoration(itemDeco)
        (recyclerview_brand_search.adapter as SearchBrandViewAdapter).setItemClickListener(object:
            SearchBrandViewAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                //Log.d("클릭","됐어??")
                val BD = viewModel.getSearchBrandData()!![position]
                viewModel2.setBrandDataForDetail(BD)
                findNavController().navigate(R.id.detailBrandFragment)
            }
        })
        viewModel.allBrandData.observe(viewLifecycleOwner, Observer {
            adapter.notifyDataSetChanged()
        })
        viewModel.brandSearchText.observe(viewLifecycleOwner, Observer {
            adapter.notifyDataSetChanged()
        })
    }
}