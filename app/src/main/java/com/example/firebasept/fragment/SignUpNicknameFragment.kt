package com.example.firebasept.fragment

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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_sign_up_nick_name.*
import kotlinx.android.synthetic.main.fragment_sign_up_password.*

class SignUpNicknameFragment: Fragment() {
    lateinit var db: FirebaseFirestore
    lateinit var repository: UserDataRepository
    lateinit var factory: SignUpViewModelFactory
    lateinit var viewModel: SignUpViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up_nick_name, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        db = FirebaseFirestore.getInstance()
        repository = UserDataRepository(db)
        factory = SignUpViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(),factory).get(
            SignUpViewModel::class.java)

        sign_up_nick_name_edit_text.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                //이메일 유효성 검사
                if(sign_up_nick_name_edit_text.text.toString().length >= 2){
                    viewModel.NickNameCheck.setValue(Pair(true,sign_up_nick_name_edit_text.text.toString()))
                }
                else{
                    viewModel.NickNameCheck.setValue(Pair(false,""))
                }
            }
        })
    }
}