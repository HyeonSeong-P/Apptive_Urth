package com.example.firebasept

internal class CommentKey(email: String = "", time: String = "") {
    var timeStamp:String = ""
    var nickname:String = ""
    init{
        nickname = email
        timeStamp = time
    }

}