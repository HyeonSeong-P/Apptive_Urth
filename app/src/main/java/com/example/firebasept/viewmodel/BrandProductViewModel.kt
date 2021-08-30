package com.example.firebasept.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.firebasept.Data.*
import com.example.firebasept.Model.BrandProductDataRepository
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Model.UserDataRepository
import com.example.firebasept.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class BrandProductViewModel(private val postDataRepository: PostDataRepository, private val userDataRepository: UserDataRepository
                            ,private val brandProductDataRepository: BrandProductDataRepository): ViewModel() {

    val categoryList = listOf<String>("#ALL", "#아우터", "#상의", "#하의", "#원피스", "#신발", "#가방", "#기타")
    var auth = FirebaseAuth.getInstance()

    //var allPostData: LiveData<List<PostDTO>> = foodDataRepository.getAllData()
    var allPostData: LiveData<List<PostData>> = postDataRepository.getAll()   // 파이어 스토어에서 데이터 들고오기
    var allUserData: LiveData<List<UserDTO>> = userDataRepository.getAll()

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
    var allCampaignData: LiveData<List<CampaignData>> = brandProductDataRepository.getAllCampaign()

    private val _campaignWebSite = SingleLiveEvent<String>()
    val campaignWebSite: LiveData<String> get() = _campaignWebSite

    fun setCampaignWebSite(string: String) {
        _campaignWebSite.setValue(string)
    }

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
    private val _changedProductPosition = SingleLiveEvent<Int>() // 정렬 관련 라이브 데이터
    val changedProductPosition: LiveData<Int> get() = _changedProductPosition

    fun setChangedProductPosition(position: Int) {
        _changedProductPosition.setValue(position)
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
            var h = _productTagCategory.value ?: return

            h!!.remove(category)
            _productTagCategory.setValue(h!!)
        } else {

            var h = _productTagCategory.value ?: return
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
            var h = _productTagCategory.value ?: return
            for (tagString in categoryList) {
                h!![tagString] = true
            }
            _productTagCategory.setValue(h)
        } else {
            var h = _productTagCategory.value ?: return

            h!!.clear()
            _productTagCategory.setValue(h)
        }
    }

    fun getBrandData(): List<BrandData> {
        var userDTO = getUser(auth.currentUser.uid)?: return listOf()
        var brandListWithTag:List<BrandData>
        var brandList = allBrandData.value?: return listOf()
        var sortedList = listOf<BrandData>()
        var userPreferTags = userDTO.tags
        when(_sortDataBrand.value){
            1 ->{
                sortedList = brandList!!.sortedByDescending { it.brandName }
            }
            else -> {
                sortedList = brandList!!.sortedWith(
                    compareBy({it.likeCount},{it.brandName}) // 첫번째 기준으로 정렬후 두번째 기준으로 정렬. 첫번째 기준이 뭔가 주요한 그런 기준!!
                ).reversed()
                //sortedList = postList!!.sortedByDescending { it.likeCount }
            }
        }

        if(userPreferTags.isEmpty()) return listOf()
        brandListWithTag = sortedList!!.filter {
            var flag = false
            for (tag in it.tagKeys) { // 유저가 선택한 태그 중 하나라도 가지고 있는 브랜드라면 필터링해 들고온다.
                if (userPreferTags.containsKey(tag)) {
                    flag = true
                    break
                }
            }
            flag
        }
        if (sortedList != null) {
            if (sortedList.isEmpty()) {
                return sortedList
            }
        }
        return brandListWithTag
    }


    fun getProductData(): List<ProductData> {
        var productListWithTag:List<ProductData>
        var productList = allProductData.value
        var sortedList = listOf<ProductData>()

        when(_sortDataProduct.value){
            3 -> {
                sortedList = productList!!.sortedWith(
                    compareBy({it.price},{it.brandName}) // 첫번째 기준으로 정렬후 두번째 기준으로 정렬. 첫번째 기준이 뭔가 주요한 그런 기준!!
                )
            }
            2 ->{
                sortedList = productList!!.sortedWith(
                    compareBy({it.price},{it.brandName}) // 첫번째 기준으로 정렬후 두번째 기준으로 정렬. 첫번째 기준이 뭔가 주요한 그런 기준!!
                ).reversed()
            }
            1 ->{
                sortedList = productList!!.sortedByDescending { it.productName }
            }
            else -> {
                sortedList = productList!!.sortedWith(
                    compareBy({it.likeCount},{it.productName}) // 첫번째 기준으로 정렬후 두번째 기준으로 정렬. 첫번째 기준이 뭔가 주요한 그런 기준!!
                ).reversed()
                //sortedList = postList!!.sortedByDescending { it.likeCount }
            }
        }

        var userPickProductTagMap: HashMap<String, Boolean> = _productTagCategory.value ?: return sortedList
        if(userPickProductTagMap.isEmpty()) return sortedList

        productListWithTag = sortedList!!.filter{
            userPickProductTagMap.containsKey(it.categoryTag)
        }

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
        var filteredProductList:List<ProductData>
        var productList = allProductData.value

        filteredProductList = productList!!.filter {
            it.brandName == brandName
        }.subList(0,9) // 0~(9-1)

        return filteredProductList
    }

    fun getPostDataWithProductData(productName: String, brandName: String): List<PostData> {
        Log.d("포스트","ㄹ호ㅓㄹ허")
        if(productName == null || productName == "" || brandName == null || brandName == "")
            return listOf()
        var postList = allPostData.value ?:return listOf()

        var retList = postList.filter { postData ->
            postData.putOnProductList.map { Pair(it.productName,it.brandName) }.contains(Pair(productName,brandName))
        }
        Log.d("포스트",retList.size.toString())
        return retList
    }


    private val _productPageNum = SingleLiveEvent<Int>() // 리사이클러뷰 페이징 관련 라이브 데이터
    val productPageNum: LiveData<Int> get() = _productPageNum

    fun setProductPageNum(i:Int){
        _productPageNum.setValue(i)
    }

    fun getSubProductData(pageNum:Int)
    : List<ProductData> {
        //var pageNum = _productPageNum.value?: 0
        var productList = getProductData()
        var retList = mutableListOf<ProductData>()

        if(pageNum*10 + 10 < productList.size){
            retList.addAll(productList.subList(0,pageNum*10 + 10))
            retList.add(ProductData())
        }
        else{
            retList.addAll(productList)
        }
        return retList.toList()
    }


    fun getCampaignData():List<CampaignData>{
        var retList = allCampaignData.value?:return listOf()
        return retList
    }


    private val _majorProductCall = SingleLiveEvent<ProductData>() // 리사이클러뷰 페이징 관련 라이브 데이터
    val majorProductCall: LiveData<ProductData> get() = _majorProductCall

    fun setMajorProductCall(productData:ProductData){
        _majorProductCall.setValue(productData)
    }

    fun clickLike(uid: String,timeStamp: String){
        postDataRepository.clickLikeInRepository(uid,timeStamp)
    }

    fun clickLikePostAdd(uid:String,timeStamp: String){
        userDataRepository.clickLikePostAddInRepository(uid,timeStamp)
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

    fun initC(){
        brandProductDataRepository.getC()
    }

    fun addBrand(brandData:BrandData){
        brandProductDataRepository.addBrand(brandData)
    }

    fun addProduct(productData: ProductData,boolean: Boolean){
        brandProductDataRepository.addProduct(productData, boolean)
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
}