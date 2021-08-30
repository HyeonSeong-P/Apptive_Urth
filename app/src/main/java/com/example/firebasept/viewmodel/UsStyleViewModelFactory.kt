package com.example.firebasept.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebasept.Model.BrandProductDataRepository
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Model.UserDataRepository
import com.google.firebase.firestore.FirebaseFirestore

internal class UsStyleViewModelFactory(private val postDataRepository: PostDataRepository,private val userDataRepository: UserDataRepository
                              , private val brandProductDataRepository: BrandProductDataRepository
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UsStyleViewModel(postDataRepository,userDataRepository,brandProductDataRepository) as T
    }
}