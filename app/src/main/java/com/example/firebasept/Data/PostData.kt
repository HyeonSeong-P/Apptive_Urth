package com.example.firebasept.Data

import androidx.room.PrimaryKey
import java.io.Serializable

class PostData {
    private var imageUrl:MutableList<String> = mutableListOf<String>()
    var title:String = ""
    var description:String = ""
    var uid:String = ""
    var nickname:String = ""

    // <좋아요 관련
    var likeCount:Int = 0
    //var likes:Map<String, Boolean> = hashMapOf()
    var likes = hashMapOf<String,Boolean>()
    // 좋아요 관련>

    // <댓글 관련
    var commentCount:Int = 0
    var comments = hashMapOf<String,String>() // Key: 닉넴,댓글 작성시간, value: 댓글 내용.*닉네임(현재는 이메일)
    // 댓글 관련>

    var timestamp:String = "" // 문서 구별자 개중요!! 구독 기능 만들때 아주 중요!!!

    var tagKeys = mutableListOf<String>() // 태그 키 리스트

    var putOnProductList = mutableListOf<ProductData>()
}