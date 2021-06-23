package com.example.firebasept.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.firebasept.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment: Fragment() {
    private lateinit var auth:FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        name_text.text = auth.currentUser.displayName
        email_text.text = auth.currentUser.email

        logout_btn.setOnClickListener {
            auth.signOut()
        }
        go_community_btn.setOnClickListener {
            findNavController().navigate(R.id.usStyleFragment)
        }
    }
}