package com.byphs.firebasept.fragment

import com.byphs.firebasept.Model.BrandProductDataRepository
import com.byphs.firebasept.viewmodel.BrandProductViewModel
import com.byphs.firebasept.viewmodel.BrandProductViewModelFactory
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.byphs.firebasept.Data.PostData
import com.byphs.firebasept.Model.PostDataRepository
import com.byphs.firebasept.Model.UserDataRepository
import com.byphs.firebasept.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.bottom_sheet_sort_brand.*

internal class BottomSheetBrand(sortName:String) : BottomSheetDialogFragment() {
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