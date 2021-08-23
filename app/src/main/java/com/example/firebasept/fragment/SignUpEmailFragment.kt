package com.example.firebasept.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.firebasept.Model.UserDataRepository
import com.example.firebasept.R
import com.example.firebasept.viewmodel.SignUpViewModel
import com.example.firebasept.viewmodel.SignUpViewModelFactory
import com.example.firebasept.viewmodel.UsStyleViewModel
import com.example.firebasept.viewmodel.UsStyleViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_sign_up_base.*
import kotlinx.android.synthetic.main.fragment_sign_up_email.*

class SignUpEmailFragment:Fragment() {
    lateinit var db: FirebaseFirestore
    lateinit var repository: UserDataRepository
    lateinit var factory: SignUpViewModelFactory
    lateinit var viewModel: SignUpViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        db = FirebaseFirestore.getInstance()
        repository = UserDataRepository(db)
        factory = SignUpViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(),factory).get(
            SignUpViewModel::class.java)


        sign_up_email_edit_text.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                //이메일 유효성 검사
                var pattern = android.util.Patterns.EMAIL_ADDRESS
                if(pattern.matcher(sign_up_email_edit_text.text.toString()).matches()){
                    viewModel.EmailCheck.setValue(Pair(true,sign_up_email_edit_text.text.toString()))
                }
                else{
                    viewModel.EmailCheck.setValue(Pair(false,""))
                }
            }
        })
    }
}