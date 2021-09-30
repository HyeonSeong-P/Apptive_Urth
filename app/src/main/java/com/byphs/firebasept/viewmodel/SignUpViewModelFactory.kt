package com.byphs.firebasept.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.byphs.firebasept.Model.UserDataRepository

internal class SignUpViewModelFactory(private val userDataRepository: UserDataRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SignUpViewModel(userDataRepository) as T
    }
}