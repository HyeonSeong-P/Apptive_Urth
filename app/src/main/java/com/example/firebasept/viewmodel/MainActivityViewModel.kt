package com.example.firebasept.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.firebasept.Data.BrandData
import com.example.firebasept.Data.PostData
import com.example.firebasept.Data.ProductData
import com.example.firebasept.Data.UserDTO
import com.example.firebasept.Model.BrandProductDataRepository
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Model.UserDataRepository
import com.google.firebase.auth.FirebaseAuth

internal class MainActivityViewModel(private val postDataRepository: PostDataRepository, private val userDataRepository: UserDataRepository
,private val brandProductDataRepository: BrandProductDataRepository): ViewModel() {
    var auth = FirebaseAuth.getInstance()
    //var allPostData: LiveData<List<PostDTO>> = foodDataRepository.getAllData()
    var allPostData: LiveData<List<PostData>> = postDataRepository.getAll()   // 파이어 스토어에서 데이터 들고오기
    var allUserData: LiveData<List<UserDTO>> = userDataRepository.getAll()
    var allBrandData: LiveData<List<BrandData>> = brandProductDataRepository.getAllBrand()
    var allProductData: LiveData<List<ProductData>> = brandProductDataRepository.getAllProduct()

    var dialogDismissCall = brandProductDataRepository.getDialogCall()

    fun initB(){
        brandProductDataRepository.getB()
    }

    fun initPR(){
        brandProductDataRepository.getPR()
    }

    fun addBrand(brandData:BrandData){
        brandProductDataRepository.addBrand(brandData)
    }

    fun addProduct(productData: ProductData,boolean: Boolean){
        brandProductDataRepository.addProduct(productData,boolean)
    }

    fun detectProduct(productData: ProductData):Boolean{
        return brandProductDataRepository.detectProduct(productData)
    }

    fun detectBrand(brandData: BrandData):Boolean{
        return brandProductDataRepository.detectBrand(brandData)
    }

}