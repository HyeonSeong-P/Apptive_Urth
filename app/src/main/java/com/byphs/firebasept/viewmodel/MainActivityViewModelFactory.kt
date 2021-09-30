package com.byphs.firebasept.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.byphs.firebasept.Model.BrandProductDataRepository
import com.byphs.firebasept.Model.PostDataRepository
import com.byphs.firebasept.Model.UserDataRepository

internal class MainActivityViewModelFactory(private val postDataRepository: PostDataRepository, private val userDataRepository: UserDataRepository, private val brandProductDataRepository: BrandProductDataRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainActivityViewModel(postDataRepository,userDataRepository,brandProductDataRepository) as T
    }
}