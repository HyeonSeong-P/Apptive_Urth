package com.example.firebasept.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.firebasept.Data.PostData
import com.example.firebasept.Data.UserDTO
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Model.UserDataRepository
import com.example.firebasept.SingleLiveEvent

internal class SignUpViewModel(private val userDataRepository: UserDataRepository): ViewModel() {
    private val _EmailCheck = SingleLiveEvent<Pair<Boolean,String>>()
    val EmailCheck get() = _EmailCheck

    private val _PasswordCheck = SingleLiveEvent<Pair<Boolean,String>>()
    val PasswordCheck get() = _PasswordCheck

    private val _NickNameCheck = SingleLiveEvent<Pair<Boolean,String>>()
    val NickNameCheck get() = _NickNameCheck

    private val _userDTOForLastStep = SingleLiveEvent<UserDTO>()
    val userDTOForLastStep: LiveData<UserDTO> get() = _userDTOForLastStep

    fun setUserDTOForLastStep(userDTO: UserDTO){
        _userDTOForLastStep.setValue(userDTO)
    }

}