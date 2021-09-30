package com.byphs.firebasept.viewmodel


import androidx.lifecycle.*
import com.byphs.firebasept.Data.BrandData
import com.byphs.firebasept.Model.PostDataRepository
import com.byphs.firebasept.Data.PostData
import com.byphs.firebasept.Data.ProductData
import com.byphs.firebasept.Data.UserDTO
import com.byphs.firebasept.Model.BrandProductDataRepository
import com.byphs.firebasept.Model.UserDataRepository
import com.byphs.firebasept.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth

internal class MyPageViewModel(private val postDataRepository: PostDataRepository,private val userDataRepository: UserDataRepository
                      , private val brandProductDataRepository: BrandProductDataRepository
): ViewModel(){

    var auth = FirebaseAuth.getInstance()
    //var allPostData: LiveData<List<PostDTO>> = foodDataRepository.getAllData()
    var allPostData: LiveData<List<PostData>> = postDataRepository.getAll()   // 파이어 스토어에서 데이터 들고오기
    var allUserData: LiveData<List<UserDTO>> = userDataRepository.getAll()
    var allBrandData: LiveData<List<BrandData>> = brandProductDataRepository.getAllBrand()
    var allProductData: LiveData<List<ProductData>> = brandProductDataRepository.getAllProduct()

    private val _postDataForSee = SingleLiveEvent<Pair<PostData,Int>>()
    val postDataForPost get() = _postDataForSee

    private val _notifyCall = SingleLiveEvent<Boolean>()
    val notifyCall get() = _notifyCall

    fun callNotify(){
        _notifyCall.call()
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
    fun initB(){
        brandProductDataRepository.getB()
    }

    fun clickTag(tagPosition:Int){
        userDataRepository.clickTagInRepository(tagPosition)
    }

    fun clickAllTag(){
        userDataRepository.clickAllTagInRepository()
    }

    fun editUserNickname(nickname:String){
        userDataRepository.editUserNicknameInRepository(nickname)
    }

    fun editUserPassword(password:String){
        userDataRepository.editUserPasswordInRepository(password)
    }

    fun getUser(uid:String): UserDTO? {
        return userDataRepository.getUserDTO(uid)
    }

    fun getBrandDataInMyPage(): List<BrandData> {
        var userDTO = getUser(auth.currentUser!!.uid)?:return listOf()
        var brandList = allBrandData.value?:return listOf()

        var retList:List<BrandData>

        retList = brandList!!.filter{
            userDTO!!.brandLikes.containsKey(it.brandName)
        }

        return retList
    }

    fun getPostDataInMyPage(): List<PostData> {
        var userDTO = getUser(auth.currentUser!!.uid)?:return listOf()
        var postList = allPostData.value?:return listOf()

        var retList:List<PostData>

        retList = postList!!.filter{
            userDTO!!.postLikes.containsKey(it.uid + "," + it.timestamp)
        }

        return retList
    }


    fun getProductDataInMyPage(): List<ProductData> {
        var userDTO = getUser(auth.currentUser!!.uid)?:return listOf()
        var productList = allProductData.value?:return listOf()

        var retList:List<ProductData>

        retList = productList!!.filter{
            userDTO!!.productLikes.containsKey(it.productName + "," + it.brandName)
        }

        return retList
    }

    fun searchPostData(uid:String,timeStamp:String): PostData? {
        return postDataRepository.getSearchPostData(uid,timeStamp)
    }

    fun getCallBackState(): SingleLiveEvent<Boolean> {
        return postDataRepository.callBackState
    }

    fun editUserImage(uri:String){
        userDataRepository.editUserImageInRepository(uri)
    }

    fun deleteUser(uid:String){
        userDataRepository.deleteUser(uid)
    }



}