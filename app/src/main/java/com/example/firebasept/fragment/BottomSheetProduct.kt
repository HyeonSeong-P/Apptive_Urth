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

internal class BottomSheetProduct(sortName:String) : BottomSheetDialogFragment() {
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
        return inflater.inflate(R.layout.bottom_sheet_sort, container, false)
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
            "낮은가격순"->{
                changeRtnTextColor(3)
            }
            "높은가격순"->{
                changeRtnTextColor(2)
            }
            "최신순"->{
                changeRtnTextColor(1)
            }
            else -> {
                changeRtnTextColor(0)
            }

        }
        popular_btn.setOnClickListener {
            changeRtnTextColor(0)
            viewModel.setSortDataProduct(0)
            dialog?.dismiss()
        }
        latest_btn.setOnClickListener {
            changeRtnTextColor(1)
            viewModel.setSortDataProduct(1)
            dialog?.dismiss()
        }
        high_price_btn.setOnClickListener {
            changeRtnTextColor(2)
            viewModel.setSortDataProduct(2)
            dialog?.dismiss()
        }
        low_price_btn.setOnClickListener {
            changeRtnTextColor(3)
            viewModel.setSortDataProduct(3)
            dialog?.dismiss()
        }
    }

    fun changeRtnTextColor(i:Int){
        when(i){
            1 -> {
                latest_btn.setTextColor(Color.parseColor("#3e3a39"))
                popular_btn.setTextColor(Color.parseColor("#aaaaaa"))
                high_price_btn.setTextColor(Color.parseColor("#aaaaaa"))
                low_price_btn.setTextColor(Color.parseColor("#aaaaaa"))
            }
            2 -> {
                high_price_btn.setTextColor(Color.parseColor("#3e3a39"))
                popular_btn.setTextColor(Color.parseColor("#aaaaaa"))
                latest_btn.setTextColor(Color.parseColor("#aaaaaa"))
                low_price_btn.setTextColor(Color.parseColor("#aaaaaa"))
            }
            3 -> {
                low_price_btn.setTextColor(Color.parseColor("#3e3a39"))
                popular_btn.setTextColor(Color.parseColor("#aaaaaa"))
                high_price_btn.setTextColor(Color.parseColor("#aaaaaa"))
                latest_btn.setTextColor(Color.parseColor("#aaaaaa"))
            }
            else -> {
                popular_btn.setTextColor(Color.parseColor("#3e3a39"))
                latest_btn.setTextColor(Color.parseColor("#aaaaaa"))
                high_price_btn.setTextColor(Color.parseColor("#aaaaaa"))
                low_price_btn.setTextColor(Color.parseColor("#aaaaaa"))
            }
        }
    }
}