package com.example.firebasept.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebasept.Model.PostDataRepository
import com.google.firebase.firestore.FirebaseFirestore

class UsStyleViewModelFactory(private val postDataRepository: PostDataRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UsStyleViewModel(postDataRepository) as T
    }
}