package com.example.firebasept.Data

import androidx.room.PrimaryKey

class PostData {
    @PrimaryKey(autoGenerate = true) val id: Int = 0
    var imageUrl:String = ""
    var title:String = ""
    var description:String = ""
    var uid:String = ""
    var userID:String = ""
    // <좋아요 관련
    var likeCount:Int = 0
    //var likes:Map<String, Boolean> = hashMapOf()
    var likes = hashMapOf<String,Boolean>()
    // 좋아요 관련>

    // <댓글 관련
    var commentCount:Int = 0
    var comments = hashMapOf<String,String>() // Key: 닉넴,댓글 작성시간, value: 댓글 내용.*닉네임(현재는 이메일)
    // 댓글 관련>
}