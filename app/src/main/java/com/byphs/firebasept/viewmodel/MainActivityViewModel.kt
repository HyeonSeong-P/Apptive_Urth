package com.byphs.firebasept.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.byphs.firebasept.Data.BrandData
import com.byphs.firebasept.Data.PostData
import com.byphs.firebasept.Data.ProductData
import com.byphs.firebasept.Data.UserDTO
import com.byphs.firebasept.Model.BrandProductDataRepository
import com.byphs.firebasept.Model.PostDataRepository
import com.byphs.firebasept.Model.UserDataRepository
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