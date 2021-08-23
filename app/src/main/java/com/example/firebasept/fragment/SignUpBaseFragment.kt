package com.example.firebasept.fragment

import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.firebasept.Data.UserDTO
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Model.UserDataRepository
import com.example.firebasept.R
import com.example.firebasept.SignUpViewPagerAdapter
import com.example.firebasept.viewmodel.SignUpViewModel
import com.example.firebasept.viewmodel.SignUpViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_sign_up_base.*
import kotlinx.android.synthetic.main.fragment_sign_up_email.*
import kotlinx.android.synthetic.main.fragment_sign_up_nick_name.*
import kotlinx.android.synthetic.main.fragment_sign_up_password.*

class SignUpBaseFragment: Fragment() {
    var emailString: String = ""
    var passwordString: String = ""
    var nicknameString: String = ""
    private lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var repository: UserDataRepository
    lateinit var factory: SignUpViewModelFactory
    lateinit var viewModel: SignUpViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var emailCheck: Boolean
        var passwordCheck: Boolean
        var nicknameCheck: Boolean


        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        repository = UserDataRepository(db)
        factory = SignUpViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(),factory).get(
            SignUpViewModel::class.java)

        sign_up_view_pager.adapter = SignUpViewPagerAdapter(childFragmentManager,lifecycle)
        sign_up_view_pager.isUserInputEnabled = false // swipe disable
        sign_up_view_pager.offscreenPageLimit = 2
        sign_up_progress_bar.progress = 10
        sign_up_progress_bar.max = 30

        next_btn_sign_up.setBackgroundColor(Color.parseColor("#aaaaaa"))
        next_btn_sign_up.isEnabled= false


        viewModel.EmailCheck.observe(viewLifecycleOwner, Observer {
            emailCheck = it.first
            emailString = it.second
            if(emailCheck){
                email_error_text.visibility = View.GONE
                next_btn_sign_up.setBackgroundColor(Color.parseColor("#3e3a39"))
                next_btn_sign_up.isEnabled= true
            }
            else{
                email_error_text.visibility = View.VISIBLE
                next_btn_sign_up.setBackgroundColor(Color.parseColor("#aaaaaa"))
                next_btn_sign_up.isEnabled= false
            }
        })

        viewModel.PasswordCheck.observe(viewLifecycleOwner, Observer {
            passwordCheck = it.first
            passwordString = it.second
            if(passwordCheck){
                password_error_text.visibility = View.GONE
                next_btn_sign_up.setBackgroundColor(Color.parseColor("#3e3a39"))
                next_btn_sign_up.isEnabled= true
            }
            else{
                password_error_text.visibility = View.VISIBLE
                next_btn_sign_up.setBackgroundColor(Color.parseColor("#aaaaaa"))
                next_btn_sign_up.isEnabled= false
            }
        })

        viewModel.NickNameCheck.observe(viewLifecycleOwner, Observer {
            nicknameCheck = it.first
            nicknameString = it.second
            if(nicknameCheck){
                nick_name_error_text.visibility = View.GONE
                create_user_btn.setBackgroundColor(Color.parseColor("#3e3a39"))
                create_user_btn.isEnabled= true
            }
            else{
                nick_name_error_text.visibility = View.VISIBLE
                create_user_btn.setBackgroundColor(Color.parseColor("#aaaaaa"))
                create_user_btn.isEnabled= false
            }
        })
        back_btn_sign_up.setOnClickListener {
            create_user_btn.visibility = View.GONE
            next_btn_sign_up.visibility = View.VISIBLE
            next_btn_sign_up.setBackgroundColor(Color.parseColor("#3e3a39"))
            next_btn_sign_up.isEnabled= true

            if(sign_up_progress_bar.progress == 10){
                findNavController().navigateUp()
            }
            else{
                sign_up_progress_bar.incrementProgressBy(-10)
                sign_up_view_pager.currentItem = sign_up_progress_bar.progress/10 - 1
            }
        }
        next_btn_sign_up.setOnClickListener {
            next_btn_sign_up.setBackgroundColor(Color.parseColor("#aaaaaa"))
            next_btn_sign_up.isEnabled= false
            Log.d("email",sign_up_email_edit_text.text.toString())
            sign_up_progress_bar.incrementProgressBy(10)
            sign_up_view_pager.currentItem = sign_up_progress_bar.progress/10 - 1

            if(sign_up_progress_bar.progress == 30){
                next_btn_sign_up.isEnabled = false
                next_btn_sign_up.visibility = View.GONE

                create_user_btn.visibility = View.VISIBLE
            }
        }


        create_user_btn.setOnClickListener {
            //Log.d("User data",emailString + " " + passwordString + " " + nicknameString)
            createUser(emailString, passwordString)
        }

    }

    private fun createUser(email:String, password:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    val userData: UserDTO = UserDTO()
                    userData.userEmail = user?.email.toString()
                    userData.password = password
                    userData.nickName = nicknameString
                    userData.uid = user?.uid.toString()

                    db.collection("user")
                        .add(userData)
                        .addOnSuccessListener { documentReference ->
                            Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(ContentValues.TAG, "Error adding document", e)
                        }
                    user?.sendEmailVerification()?.addOnCompleteListener { verifyTask ->
                        if(verifyTask.isSuccessful){
                            /*Toast.makeText(requireContext(), "회원 가입 성공",
                                Toast.LENGTH_SHORT).show()*/
                            auth.signOut()
                            findNavController().navigate(R.id.mainFragment)
                        }
                        else{
                        }
                    }

                    //updateUI(user)
                } else { // 회원가입 실패일때 즉 이미 아이디 있을때.
                    // If sign in fails, display a message to the user.
                    Toast.makeText(requireContext(), "이미 회원 가입하셨습니다~",
                        Toast.LENGTH_SHORT).show()
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    //updateUI(null)
                }

                // ...
            }
    }
}