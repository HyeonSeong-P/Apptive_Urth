package com.example.firebasept.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.firebasept.Data.UserDTO
import com.example.firebasept.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment:Fragment() {
    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        sign_up_btn.setOnClickListener{
            createUser(sign_up_id_edit_text.text.toString(),sign_up_password_edit_text.text.toString())
        }

    }

    private fun createUser(email:String, password:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    val userData: UserDTO = UserDTO()
                    userData.userEmail = user.email
                    userData.password = password
                    userData.nickName = sign_up_name_edit_text.text.toString()
                    userData.uid = user.uid

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