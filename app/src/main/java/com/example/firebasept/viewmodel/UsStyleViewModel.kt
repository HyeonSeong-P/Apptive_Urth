package com.example.firebasept.viewmodel

import androidx.lifecycle.*
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Data.PostData
import com.example.firebasept.SingleLiveEvent

class UsStyleViewModel(private val postDataRepository: PostDataRepository): ViewModel(){
    //var allPostData: LiveData<List<PostDTO>> = foodDataRepository.getAllData()
    var allPostData: LiveData<List<PostData>> = postDataRepository.getAll()   // 파이어 스토어에서 데이터 들고오기

    private val _postDataForSee = SingleLiveEvent<Pair<PostData,Int>>()
    val postDataForPost get() = _postDataForSee

    private val _notifyCall = SingleLiveEvent<Boolean>()
    val notifyCall get() = _notifyCall

    fun callNotify(){
        _notifyCall.call()
    }

    fun getPostData(): List<PostData>? {

        var postList = allPostData.value
        if (postList != null) {
            if(postList.isEmpty()){
            }
        }

        return postList
    }
    fun initP(){
        postDataRepository.getP()
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
    fun clickLike(position:Int){
        postDataRepository.clickLikeInRepository(position)
    }

    fun registerComment(position: Int,commentInformation:Pair<String,String>){
        postDataRepository.registerCommentInRepository(position, commentInformation)
    }

    fun deleteComment(position: Int, key: String){
        postDataRepository.deleteCommentInRepository(position, key)
    }
}