package com.example.firebasept.fragment

import com.example.firebasept.Model.BrandProductDataRepository
import com.example.firebasept.viewmodel.BrandProductViewModel
import com.example.firebasept.viewmodel.BrandProductViewModelFactory
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.firebasept.Data.PostData
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Model.UserDataRepository
import com.example.firebasept.R
import com.example.firebasept.viewmodel.UsStyleViewModel
import com.example.firebasept.viewmodel.UsStyleViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.bottom_sheet_sort.*
import kotlinx.android.synthetic.main.bottom_sheet_sort_brand.*

class BottomSheetBrand(sortName:String) : BottomSheetDialogFragment() {
    private val s = sortName
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var postList: MutableList<PostData>
    lateinit var repository: PostDataRepository
    lateinit var repository2: UserDataRepository
    lateinit var repository3: BrandProductDataRepository
    lateinit var factory: BrandProductViewModelFactory
    lateinit var viewModel: BrandProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_sort_brand, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        repository = PostDataRepository(db)
        repository2 = UserDataRepository(db)
        repository3 = BrandProductDataRepository(db)
        postList = mutableListOf()
        //db = FirebaseFirestore.getInstance()
        factory = BrandProductViewModelFactory(repository,repository2,repository3)
        viewModel = ViewModelProvider(requireActivity(),factory).get(
            BrandProductViewModel::class.java)

        when(s){

            "최신순"->{
                changeRtnTextColor(1)
            }
            else -> {
                changeRtnTextColor(0)
            }

        }
        popular_btn_brand.setOnClickListener {
            changeRtnTextColor(0)
            viewModel.setSortDataBrand(0)
            dialog?.dismiss()
        }
        latest_btn_brand.setOnClickListener {
            changeRtnTextColor(1)
            viewModel.setSortDataBrand(1)
            dialog?.dismiss()
        }

    }

    private fun changeRtnTextColor(i:Int){
        when(i){
            1 -> {
                latest_btn_brand.setTextColor(Color.parseColor("#3e3a39"))
                popular_btn_brand.setTextColor(Color.parseColor("#aaaaaa"))

            }
            else -> {
                popular_btn_brand.setTextColor(Color.parseColor("#3e3a39"))
                latest_btn_brand.setTextColor(Color.parseColor("#aaaaaa"))
            }
        }
    }
}