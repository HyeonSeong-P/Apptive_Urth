package com.example.firebasept.viewmodel

import androidx.lifecycle.ViewModel
import com.example.firebasept.Data.PostData
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Model.UserDataRepository
import com.example.firebasept.SingleLiveEvent

class SignUpViewModel(private val userDataRepository: UserDataRepository): ViewModel() {
    private val _EmailCheck = SingleLiveEvent<Pair<Boolean,String>>()
    val EmailCheck get() = _EmailCheck

    private val _PasswordCheck = SingleLiveEvent<Pair<Boolean,String>>()
    val PasswordCheck get() = _PasswordCheck

    private val _NickNameCheck = SingleLiveEvent<Pair<Boolean,String>>()
    val NickNameCheck get() = _NickNameCheck
}