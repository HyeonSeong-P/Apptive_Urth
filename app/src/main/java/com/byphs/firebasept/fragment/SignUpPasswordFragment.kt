package com.byphs.firebasept.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.byphs.firebasept.Model.UserDataRepository
import com.byphs.firebasept.R
import com.byphs.firebasept.viewmodel.SignUpViewModel
import com.byphs.firebasept.viewmodel.SignUpViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.fragment_sign_up_password.*

internal class SignUpPasswordFragment:Fragment() {
    lateinit var db: FirebaseFirestore
    lateinit var repository: UserDataRepository
    lateinit var factory: SignUpViewModelFactory
    lateinit var viewModel: SignUpViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        db = FirebaseFirestore.getInstance()
        repository = UserDataRepository(db)
        factory = SignUpViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(),factory).get(
            SignUpViewModel::class.java)

        sign_up_password_edit_text.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                //이메일 유효성 검사
                if(sign_up_password_edit_text.text.toString().length >= 6){
                    viewModel.PasswordCheck.setValue(Pair(true,sign_up_password_edit_text.text.toString()))
                }
                else{
                    viewModel.PasswordCheck.setValue(Pair(false,""))
                }
            }
        })
        password_sight_check_box.setOnClickListener {
            if(password_sight_check_box.isChecked){
                //sign_up_password_edit_text.inputType = InputType.TYPE_CLASS_TEXT
                sign_up_password_edit_text.transformationMethod = null
            }
            else{
                //sign_up_password_edit_text.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                sign_up_password_edit_text.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }


    }


}