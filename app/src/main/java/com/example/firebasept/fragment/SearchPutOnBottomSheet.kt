package com.example.firebasept.fragment

import android.content.DialogInterface
import android.graphics.Point
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.firebasept.Data.PostData
import com.example.firebasept.Data.ProductData
import com.example.firebasept.Model.BrandProductDataRepository
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Model.UserDataRepository
import com.example.firebasept.R
import com.example.firebasept.itemDeco.ItemDeco
import com.example.firebasept.itemDeco.SearchPutOnItemDeco
import com.example.firebasept.recyclerView.SearchPutOnProductViewAdapter
import com.example.firebasept.viewmodel.UsStyleViewModel
import com.example.firebasept.viewmodel.UsStyleViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.bottom_sheet_search_put_on.*


internal class SearchPutOnBottomSheet() : BottomSheetDialogFragment() {
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var postList: MutableList<PostData>
    lateinit var repository: PostDataRepository
    lateinit var repository2: UserDataRepository
    lateinit var repository3: BrandProductDataRepository
    lateinit var factory: UsStyleViewModelFactory
    lateinit var viewModel: UsStyleViewModel
    lateinit var adapter: SearchPutOnProductViewAdapter
    lateinit var itemDeco: SearchPutOnItemDeco
    var searchProductList = mutableListOf<ProductData>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_search_put_on, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val display = requireActivity().windowManager.defaultDisplay // in case of Fragment
        val size = Point()
        display.getRealSize(size) // or getSize(size)
        val width = size.x
        val height = size.y
        val offsetFromTop = 60
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            isFitToContents = false
            setExpandedOffset(offsetFromTop)
            //peekHeight = height
            state = BottomSheetBehavior.STATE_EXPANDED
        }

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
        viewModel.initPR()
        adapter = SearchPutOnProductViewAdapter(viewModel)
        itemDeco = SearchPutOnItemDeco(requireContext())

        viewModel.setSearchPutOnProductData("")

        viewModel.allProductData.observe(viewLifecycleOwner, Observer {
            Log.d("착용제품","ㅇㅀㅇㅀㅇㅀㅇㅀ")
            searchProductList = viewModel.getSearchPutOnProductData().toMutableList()
            grid_recyclerview_search_put_on_product.adapter = adapter
            val gridLayoutManager = GridLayoutManager(activity, 3)
            grid_recyclerview_search_put_on_product.layoutManager = gridLayoutManager
            if(itemDeco != null)
                grid_recyclerview_search_put_on_product.removeItemDecoration(itemDeco)
            grid_recyclerview_search_put_on_product.addItemDecoration(itemDeco)
            (grid_recyclerview_search_put_on_product.adapter as SearchPutOnProductViewAdapter).setItemClickListener(object:
                SearchPutOnProductViewAdapter.OnItemClickListener{
                override fun onClick(v: View, position: Int) {
                    adapter.notifyDataSetChanged()
                }
            })

            put_on_product_select_complete.setOnClickListener {
                val selectedList = adapter.getCheckMapToList()
                viewModel.setPutOnProductList(selectedList)
                dialog?.dismiss()
            }

        })

        put_on_search_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                viewModel.setSearchPutOnProductData(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        viewModel.search_put_on_product_data.observe(viewLifecycleOwner, Observer {
            // 밑에 둘 다 가능
            //(grid_recyclerview_search_put_on_product.adapter as SearchPutOnProductViewAdapter).notifyDataSetChanged()
            adapter.notifyDataSetChanged()
        })

    }


    override fun onDismiss(dialog: DialogInterface) {
        adapter.clearCheckMap()
        super.onDismiss(dialog)
    }


}