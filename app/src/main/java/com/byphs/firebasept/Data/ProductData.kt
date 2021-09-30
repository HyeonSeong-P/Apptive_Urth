package com.byphs.firebasept.Data

internal class ProductData {

    var imageUrl:String = ""
    var purchaseLink:String = ""
    var productName:String = ""
    var price:Int = 0
    var brandName:String = ""

    // <좋아요 관련
    var likeCount:Int = 0
    //var likes:Map<String, Boolean> = hashMapOf()
    var likes = hashMapOf<String,Boolean>()
    // 좋아요 관련>

    var categoryTag:String = ""
}