package com.example.firebasept.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.firebasept.Data.BrandData
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Data.PostData
import com.example.firebasept.Data.ProductData
import com.example.firebasept.Data.UserDTO
import com.example.firebasept.Model.BrandProductDataRepository
import com.example.firebasept.Model.UserDataRepository
import com.example.firebasept.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth

internal class UsStyleViewModel(private val postDataRepository: PostDataRepository,private val userDataRepository: UserDataRepository
                       , private val brandProductDataRepository: BrandProductDataRepository
): ViewModel(){

    var auth = FirebaseAuth.getInstance()

    //var allPostData: LiveData<List<PostDTO>> = foodDataRepository.getAllData()
    var allPostData: LiveData<List<PostData>> = postDataRepository.getAll()   // 파이어 스토어에서 데이터 들고오기
    var allUserData: LiveData<List<UserDTO>> = userDataRepository.getAll()
    var allProductData: LiveData<List<ProductData>> = brandProductDataRepository.getAllProduct()
    var allBrandData: LiveData<List<BrandData>> = brandProductDataRepository.getAllBrand()

    private val _postDataForSee = SingleLiveEvent<Pair<PostData,Int>>()
    val postDataForPost get() = _postDataForSee

    fun setPostDataForSee(postData: PostData,i:Int){
        _postDataForSee.setValue(Pair(postData,i))
    }
    private val _notifyCall = SingleLiveEvent<Boolean>()
    val notifyCall get() = _notifyCall


    private val _sort_data = SingleLiveEvent<Int>() // 정렬 관련 라이브 데이터
    val sort_data: LiveData<Int> get() = _sort_data

    fun initSortData(){
        _sort_data.setValue(1)
    }

    fun setSortData(i:Int){
        _sort_data.setValue(i)
    }
    fun callNotify(){
        _notifyCall.call()
    }


    fun getPostData(): List<PostData>? {
        var userDTO = getUser(auth.currentUser.uid)
        if(userDTO == null) return listOf<PostData>()
        var postListWithTag:List<PostData>
        var postList = allPostData.value
        var sortedList = listOf<PostData>()

        when(_sort_data.value){
            3->{
                sortedList = postList!!.sortedWith(
                    compareBy({it.putOnProductList.sumBy { productData ->  productData.price }},{it.timestamp}) // 첫번째 기준으로 정렬후 두번째 기준으로 정렬. 첫번째 기준이 뭔가 주요한 그런 기준!!
                )
            }
            2->{
                sortedList = postList!!.sortedWith(
                    compareBy({it.putOnProductList.sumBy { productData ->  productData.price }},{it.timestamp}) // 첫번째 기준으로 정렬후 두번째 기준으로 정렬. 첫번째 기준이 뭔가 주요한 그런 기준!!
                ).reversed()
            }
            1 ->{

                sortedList = postList!!.sortedByDescending { it.timestamp }
            }
            else -> {
                sortedList = postList!!.sortedWith(
                    compareBy({it.likeCount},{it.timestamp}) // 첫번째 기준으로 정렬후 두번째 기준으로 정렬. 첫번째 기준이 뭔가 주요한 그런 기준!!
                ).reversed()
                //sortedList = postList!!.sortedByDescending { it.likeCount }
            }
        }


        postListWithTag = sortedList!!.filter{
            var flag = false
            for(tag in it.tagKeys){
                if(userDTO!!.tags.containsKey(tag)){
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
        /*if (postList != null) {
            if(postList.isEmpty()){
                return postList
            }
            else{

            }
        }
        else return listOf<PostData>()*/
        return postListWithTag
    }

    private val _search_put_on_product_data = SingleLiveEvent<String>() // 검색 관련 라이브 데이터
    val search_put_on_product_data: LiveData<String> get() = _search_put_on_product_data

    fun setSearchPutOnProductData(text:String){
        _search_put_on_product_data.setValue(text)
    }

    fun getSearchPutOnProductData(): List<ProductData> {
        var searchText:String = if(_search_put_on_product_data.value == null) ""
            else _search_put_on_product_data.value!!

        var productList = allProductData.value
        var returnList = listOf<ProductData>()

        Log.d("검색갯수",allProductData.value!!.size.toString())
        if(productList == null){
            return returnList
        }
        else{
            if(searchText == ""){
                return productList
            }
            returnList = productList.filter { it.productName.contains(searchText,true) || it.brandName.contains(searchText,true) }
            return returnList
        }
    }

    fun getPutOnProductData(list: List<ProductData>): List<ProductData> {
        var productList: List<ProductData>? = allProductData.value ?: return mutableListOf<ProductData>()
        var retList = productList!!.filter {
            var flag = false
            for (p in list) {
                if(p.productName == it.productName && p.brandName == it.brandName){
                    flag = true
                    break
                }
            }
            flag
        }
        return retList
    }

    private val _put_on_product_data_list = SingleLiveEvent<List<Pair<String, String>>>() // 게시글 작성 착용상품 관련 라이브 데이터
    val put_on_product_data_list: LiveData<List<Pair<String, String>>> get() = _put_on_product_data_list

    fun setPutOnProductList(list: List<Pair<String, String>>){
        _put_on_product_data_list.setValue(list)
    }

    private val _user_page_user_uid = SingleLiveEvent<String>() // 유저페이지 관련 라이브 데이터
    val user_page_user_uid: LiveData<String> get() = _user_page_user_uid

    fun setUserPageUserUid(uid:String){
        _user_page_user_uid.setValue(uid)
    }
    fun getUserPagePostData(): List<PostData> {
        var postList: List<PostData>? = allPostData.value ?: return listOf<PostData>()
        var uid = _user_page_user_uid.value ?: return listOf()

        var retList = postList!!
            .filter { it.uid == uid }
            .sortedByDescending { it.timestamp }

        return retList
    }


    private val _postDeleteCall = SingleLiveEvent<Boolean>() // 포스트 삭제 콜
    val postDeleteCall: LiveData<Boolean> get() = _postDeleteCall

    fun setDeletePostCall(boolean: Boolean){
        _postDeleteCall.value = true
    }

    private val _postEditCall = SingleLiveEvent<Boolean>() // 포스트 수정 콜
    val postEditCall: LiveData<Boolean> get() = _postEditCall

    fun setEditPostCall(boolean: Boolean){
        _postEditCall.value = true
    }

    private val _postEditCallToWrite = SingleLiveEvent<PostData>() // 포스트 수정 콜
    val postEditCallToWrite: LiveData<PostData> get() = _postEditCallToWrite

    fun setEditPostCallToWrite(postData: PostData){
        _postEditCallToWrite.value = postData
    }

    fun initP(){
        postDataRepository.getP()
    }
    fun initU(){
        userDataRepository.getU()
    }

    fun initPR(){
        brandProductDataRepository.getPR()
    }

    fun clickTag(tagPosition:Int){
        userDataRepository.clickTagInRepository(tagPosition)
    }

    fun clickAllTag(){
        userDataRepository.clickAllTagInRepository()
    }

    fun getProduct(productName:String,brandName: String): ProductData? {
        return brandProductDataRepository.getProduct(productName,brandName)
    }

    fun getUser(uid:String): UserDTO? {
        return userDataRepository.getUserDTO(uid)
    }

    fun searchPostData(uid:String,timeStamp:String): PostData? {
        return postDataRepository.getSearchPostData(uid,timeStamp)
    }

    fun getCallBackState(): SingleLiveEvent<Boolean> {
        return postDataRepository.callBackState
    }

    fun getLikeState(): SingleLiveEvent<Boolean> {
        return postDataRepository.likeState
    }
    fun getCommentState(): SingleLiveEvent<Boolean> {
        return postDataRepository.commentState
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

    fun registerComment(postUid:String, postTimestamp:String,commentInformation:Pair<String,String>){
        postDataRepository.registerCommentInRepository(postUid, postTimestamp, commentInformation)
    }

    fun deleteComment(postUid:String, postTimestamp:String, key: String){
        postDataRepository.deleteCommentInRepository(postUid, postTimestamp, key)
    }

    fun editUserIntroduction(introduction:String){
        userDataRepository.editUserIntroductionInRepository(introduction)
    }
    fun deletePost(uid: String, timeStamp: String){
        postDataRepository.deletePost(uid, timeStamp)
    }
}