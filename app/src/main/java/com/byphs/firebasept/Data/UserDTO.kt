package com.byphs.firebasept.Data

internal class UserDTO {
    var userImage:String = ""
    var userEmail:String = ""
    var password:String = ""
    var uid:String = ""
    var name:String = ""
    var nickName:String = ""

    var postLikesCount = 0
    var postLikes = hashMapOf<String,Boolean>()// 작성자 uid + timestamp

    var productLikesCount = 0
    var productLikes = hashMapOf<String,Boolean>() // 상품명 + 브랜드명

    var brandLikesCount = 0
    var brandLikes = hashMapOf<String,Boolean>() // 브랜드명

    var tags = hashMapOf<String,Boolean>()

    var userIntroduction:String = ""

    /*fun setUserImage(string: String){
        userImage = string
    }

    fun setUserEmail(string: String){
        userEmail = string
    }

    fun setPassword(string: String){
        password = string
    }*/

    
}