package com.byphs.firebasept.Data

internal class BrandData {
    var logoImage:String = ""
    var backgroundImage:String = ""
    var description:String = ""
    var brandName:String = ""
    var koreanBrandName:String = ""
    var brandLink:String = ""
    // <좋아요 관련
    var likeCount:Int = 0
    //var likes:Map<String, Boolean> = hashMapOf()
    var likes = hashMapOf<String,Boolean>()
    // 좋아요 관련>

    var tagKeys = mutableListOf<String>() // 태그 키 리스트

}