package com.example.firebasept.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.firebasept.Data.BrandData
import com.example.firebasept.Data.PostData
import com.example.firebasept.Data.ProductData
import com.example.firebasept.Data.UserDTO
import com.example.firebasept.Model.BrandProductDataRepository
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Model.UserDataRepository
import com.example.firebasept.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth

internal class HomeViewModel(private val postDataRepository: PostDataRepository, private val userDataRepository: UserDataRepository
                            ,private val brandProductDataRepository: BrandProductDataRepository): ViewModel() {

    val categoryList = listOf<String>("#ALL", "#아우터", "#상의", "#하의", "#원피스", "#신발", "#가방", "#기타")
    var auth = FirebaseAuth.getInstance()

    //var allPostData: LiveData<List<PostDTO>> = foodDataRepository.getAllData()
    var allPostData: LiveData<List<PostData>> = postDataRepository.getAll()   // 파이어 스토어에서 데이터 들고오기
    var allUserData: LiveData<List<UserDTO>> = userDataRepository.getAll()

    private val _postDataForSee = SingleLiveEvent<Pair<PostData, Int>>()
    val postDataForPost: LiveData<Pair<PostData, Int>> get() = _postDataForSee

    fun setPostDataForSee(postData: PostData, i: Int) {
        _postDataForSee.setValue(Pair(postData, i))
    }

    fun getUser(uid: String): UserDTO? {
        return userDataRepository.getUserDTO(uid)
    }

    fun getProduct(productName: String, brandName: String): ProductData? {
        return brandProductDataRepository.getProduct(productName, brandName)
    }

    fun getBrand(brandName: String): BrandData? {
        return brandProductDataRepository.getBrand(brandName)
    }


    var allBrandData: LiveData<List<BrandData>> = brandProductDataRepository.getAllBrand()
    var allProductData: LiveData<List<ProductData>> = brandProductDataRepository.getAllProduct()

    private val _productDataForDetail = SingleLiveEvent<ProductData>()
    val productDataForDetail: LiveData<ProductData> get() = _productDataForDetail

    fun setProductDataForDetail(PRD: ProductData) {
        _productDataForDetail.setValue(PRD)
    }


    private val _brandDataForDetail = SingleLiveEvent<BrandData>()
    val brandDataForDetail: LiveData<BrandData> get() = _brandDataForDetail

    fun setBrandDataForDetail(BD: BrandData) {
        _brandDataForDetail.setValue(BD)
    }

    // 브랜드 정렬 변수
    private val _sortDataBrand = SingleLiveEvent<Int>() // 정렬 관련 라이브 데이터
    val sortDataBrand: LiveData<Int> get() = _sortDataBrand

    fun initSortDataBrand() {
        _sortDataBrand.setValue(1)
    }

    fun setSortDataBrand(i: Int) {
        _sortDataBrand.setValue(i)
    }

    // 상품 정렬 변수
    private val _sortDataProduct = SingleLiveEvent<Int>() // 정렬 관련 라이브 데이터
    val sortDataProduct: LiveData<Int> get() = _sortDataProduct

    fun initSortDataProduct() {
        _sortDataProduct.setValue(1)
    }

    fun setSortDataProduct(i: Int) {
        _sortDataProduct.setValue(i)
    }

    //상품 카테고리 변수
    private val _productTagCategory = SingleLiveEvent<HashMap<String, Boolean>>() // 정렬 관련 라이브 데이터
    val productTagCategory: LiveData<HashMap<String, Boolean>> get() = _productTagCategory

    fun setProductCategory(category: String) {
        Log.d("클릭", "클릭됨!")
        if (_productTagCategory.value == null) {
            _productTagCategory.value = hashMapOf()
        }
        if (_productTagCategory.value!!.containsKey(category)) {
            var h = _productTagCategory.value
            h!!.remove(category)
            _productTagCategory.setValue(h)
        } else {
            var h = _productTagCategory.value
            h!![category] = true
            _productTagCategory.setValue(h)
            Log.d("클릭", "클릭됨2222!")
        }
    }

    fun setProductAllCategory() {
        if (_productTagCategory.value == null) {
            _productTagCategory.value = hashMapOf()
        }
        var allFlag: Boolean = true
        for (tagString in categoryList) {
            if (!(_productTagCategory.value!!.containsKey(tagString))) allFlag = false
        }
        if (!allFlag) {
            var h = _productTagCategory.value
            for (tagString in categoryList) {
                h!![tagString] = true
            }
            _productTagCategory.setValue(h)
        } else {
            var h = _productTagCategory.value
            h!!.clear()
            _productTagCategory.setValue(h)
        }
    }

    fun getBrandData(): List<BrandData> {
        var userDTO = getUser(auth.currentUser.uid)
        var brandListWithTag: List<BrandData>
        var brandList = allBrandData.value
        var sortedList = listOf<BrandData>()

        when (_sortDataBrand.value) {
            1 -> {
                sortedList = brandList!!.sortedByDescending { it.brandName }
            }
            else -> {
                sortedList = brandList!!.sortedWith(
                    compareBy({ it.likeCount },
                        { it.brandName }) // 첫번째 기준으로 정렬후 두번째 기준으로 정렬. 첫번째 기준이 뭔가 주요한 그런 기준!!
                ).reversed()
                //sortedList = postList!!.sortedByDescending { it.likeCount }
            }
        }

        brandListWithTag = sortedList
        /*brandListWithTag = sortedList!!.filter{
            var flag = false
            for(tag in it.tagKeys){
                if(userDTO!!.tags.containsKey(tag)){
                    flag = true
                    break
                }
            }
            flag
        }*/
        if (sortedList != null) {
            if (sortedList.isEmpty()) {
                return sortedList
            }
        }
        return brandListWithTag
    }


    fun getProductData(): List<ProductData> {
        var productListWithTag: List<ProductData>
        var productList = allProductData.value
        var sortedList = listOf<ProductData>()

        when (_sortDataProduct.value) {
            1 -> {
                sortedList = productList!!.sortedByDescending { it.brandName }
            }
            else -> {
                sortedList = productList!!.sortedWith(
                    compareBy({ it.likeCount },
                        { it.brandName }) // 첫번째 기준으로 정렬후 두번째 기준으로 정렬. 첫번째 기준이 뭔가 주요한 그런 기준!!
                ).reversed()
                //sortedList = postList!!.sortedByDescending { it.likeCount }
            }
        }

        productListWithTag = sortedList
        /*productListWithTag = sortedList!!.filter{
            _productTagCategory.value!!.containsKey(it.categoryTag)
        }*/
        if (sortedList != null) {
            if (sortedList.isEmpty()) {
                return sortedList
            }
        }
        return productListWithTag
    }

    fun getMajorProductData(brandName: String): List<ProductData> {
        var filteredProductList: List<ProductData>
        var productList = allProductData.value

        filteredProductList = productList!!.filter {
            it.brandName == brandName
        }.subList(0, 9) // 0~(9-1)

        return filteredProductList
    }

    fun getBestBrandData(): List<BrandData> {
        var brandList = allBrandData.value
        var sortedList = listOf<BrandData>()

        sortedList = brandList!!.sortedWith(
            compareBy({ it.likeCount },
                { it.brandName }) // 첫번째 기준으로 정렬후 두번째 기준으로 정렬. 첫번째 기준이 뭔가 주요한 그런 기준!!
        ).reversed()

        return if (sortedList.size >= 10) {
            sortedList.subList(0, 10)
        } else sortedList.subList(0, sortedList.size)
    }

    fun getBestPostData(): List<PostData> {
        var postList = allPostData.value
        var sortedList = listOf<PostData>()

        sortedList = postList!!.sortedWith(
            compareBy({ it.likeCount },
                { it.timestamp }) // 첫번째 기준으로 정렬후 두번째 기준으로 정렬. 첫번째 기준이 뭔가 주요한 그런 기준!!
        ).reversed()

        return if (sortedList.size >= 10) {
            sortedList.subList(0, 10)
        } else sortedList.subList(0, sortedList.size)
    }

    fun getNewProductData():List<ProductData>{
        var productList = allProductData.value?:return listOf()

        return if(productList!!.size > 13)
        {
            productList!!.subList(0,13)
        }
        else productList!!.subList(0,productList!!.size)
    }

    fun getMoreNewProductData():List<ProductData>{
        var newProductList = getNewProductData()
        var brandSet = mutableSetOf<String>()
        var retList = mutableListOf<ProductData>()
        for(p in newProductList){
            brandSet.add(p.brandName)
        }
        for(brandName in brandSet){
            var productData = ProductData()
            productData.brandName = brandName
            retList.add(productData)
            var addList = newProductList.filter { it.brandName == brandName }
            retList.addAll(addList)
        }
         return retList
    }



    private val _brandSearchText = SingleLiveEvent<String>()
    val brandSearchText: LiveData<String> get() = _brandSearchText

    fun setBrandSearchText(text:String){
        _brandSearchText.setValue(text)
    }

    private val _productSearchText = SingleLiveEvent<String>()
    val productSearchText: LiveData<String> get() = _productSearchText

    fun setProductSearchText(text:String){
        _productSearchText.setValue(text)
    }

    fun getSearchBrandData(): List<BrandData> {
        var brandList = allBrandData.value?:return listOf<BrandData>()
        var searchText = _brandSearchText.value?:return listOf<BrandData>()

        if(searchText == "") return listOf<BrandData>()

        var retList = brandList.filter {
            it.brandName.contains(searchText,true)
        }

        return retList
    }

    fun getSearchProductData(): List<ProductData> {
        var productList = allProductData.value?:return listOf<ProductData>()
        var searchText = _productSearchText.value?:return listOf<ProductData>()
        
        if(searchText == "") return listOf<ProductData>()

        var retList = productList.filter {
            it.productName.contains(searchText,true)
        }

        return retList
    }


    private val _moreNewOrNormalCall = SingleLiveEvent<Pair<Boolean,ProductData>>()
    val moreNewOrNormalCall: LiveData<Pair<Boolean,ProductData>> get() = _moreNewOrNormalCall

    fun setMoreNewOrNormalCall(boolean: Boolean,productData: ProductData){
        _moreNewOrNormalCall.setValue(Pair(boolean,productData))
    }

    fun clickLikeBrand(brandName:String){
        brandProductDataRepository.clickLikeBrandInRepository(brandName)
    }

    fun clickLikeProduct(productName:String,brandName:String){
        brandProductDataRepository.clickLikeProductInRepository(productName,brandName)
    }

    fun clickLikeBrandAdd(brandName:String){
        userDataRepository.clickLikeBrandAddInRepository(brandName)
    }

    fun clickLikeProductAdd(productName:String,brandName:String){
        userDataRepository.clickLikeProductAddInRepository(productName,brandName)
    }


    fun initP(){
        postDataRepository.getP()
    }

    fun initU(){
        userDataRepository.getU()
    }

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

    fun clickTag(tagPosition:Int){
        userDataRepository.clickTagInRepository(tagPosition)
    }

    fun clickAllTag(){
        userDataRepository.clickAllTagInRepository()
    }

    fun clickLike(uid: String,timeStamp: String){
        postDataRepository.clickLikeInRepository(uid,timeStamp)
    }

    fun clickLikePostAdd(uid:String,timeStamp: String){
        userDataRepository.clickLikePostAddInRepository(uid,timeStamp)
    }
}