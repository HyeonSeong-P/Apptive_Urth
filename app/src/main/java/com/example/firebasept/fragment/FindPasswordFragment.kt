package com.example.firebasept.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.firebasept.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_find_password.*
import kotlinx.android.synthetic.main.fragment_sign_up_email.*

class FindPasswordFragment: Fragment() {
    lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_find_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()

        send_password_find_email_btn.setOnClickListener {
            //이메일 유효성 검사
            var pattern = android.util.Patterns.EMAIL_ADDRESS
            if(pattern.matcher(find_password_email_edit_text.text.toString()).matches()){
                auth.sendPasswordResetEmail(find_password_email_edit_text.text.toString())
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            Toast.makeText(requireContext(),"비밀번호 재설정 메일을 보냈습니다.",Toast.LENGTH_SHORT).show()
                            findNavController().navigateUp()
                        }
                        else{
                            Toast.makeText(requireContext(),"비밀번호 재설정 메일을 보내는데 오류가 발생했습니다.",Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            else{
                Toast.makeText(requireContext(),"이메일을 정확히 입력해주세요.",Toast.LENGTH_SHORT).show()
            }

        }
    }
}