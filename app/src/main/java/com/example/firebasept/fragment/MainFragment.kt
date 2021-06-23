package com.example.firebasept.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.firebasept.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment:Fragment() {

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]
    //private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener
    private val RC_SIGN_IN = 100
    private var googleSignInClient: GoogleSignInClient? = null

    // 이거 쓰지마라. 괜히 오류난다. requireActivity,requireContext 사용해라
    //val mainActivity = activity as MainActivity


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)

            .requestIdToken(getString(R.string.default_web_client_id))

            .requestEmail()

            .build()

        auth = FirebaseAuth.getInstance()


        googleSignInClient = GoogleSignIn.getClient(requireContext(),gso)

        google_sign_up_button.setOnClickListener { // google login button
            val signInIntent = googleSignInClient?.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        email_sign_in_button.setOnClickListener{
            loginUser(email_edit_text.text.toString(),password_edit_text.text.toString())
        }
        go_to_sign_up_btn.setOnClickListener {
            findNavController().navigate(R.id.signUpFragment)
        }

        /*mAuthStateListener = FirebaseAuth.AuthStateListener{
            val user = it.currentUser
            if(user!=null){
                Toast.makeText(requireContext(), "로그인 완료",
                    Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.homeFragment)
            }
            else{
            }

        }
        auth!!.addAuthStateListener(mAuthStateListener!!) // <필수> 어스스테이트리스너 할당하고 추가해줘야함.*/

    }
    /*private fun createUser(email:String, password:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "createUserWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(requireContext(), "회원 가입 성공",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(user)
                } else { // 회원가입 실패일때 즉 이미 아이디 있을때.
                    // If sign in fails, display a message to the user.
                    loginUser(email,password)
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    //updateUI(null)
                }

                // ...
            }
    }*/

    private fun loginUser(email:String, password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    if(user?.isEmailVerified!!){
                        /*Toast.makeText(requireContext(), "로그인 완료",
                            Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.homeFragment)*/
                    }
                    else{
                       /* Toast.makeText(requireContext(), "이메일 인증을 완료해주세요",
                            Toast.LENGTH_SHORT).show()*/
                        // 이걸 액티비티에 있는 어스리스너 안에 넣으니 오류가 나네?
                        // 왜일까
                        auth.signOut()

                        //findNavController().navigate(R.id.homeFragment)
                    }
                    // 이와 같은 로그인 성공 후 프로세스를 이런식으로 해도 되지만
                    // AuthStateListener 를 사용하게 되면 이메일 로그인 뿐만 아니라 구글이든 페이스북이든 모든 종류의 로그인 성공 후 프로세스를 다룰 수 있다
                    // 인프런 강의에서 다시보고 익히자.
                    /*Toast.makeText(requireContext(), "로그인 완료",
                        Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.homeFragment)*/
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    //Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(requireContext(), "로그인 실패",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                    // ...
                }

                // ...
            }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("tag", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("tagW", "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Toast.makeText(requireActivity(),"로그인 성공", Toast.LENGTH_SHORT).show()
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    //Log.w(TAG, "signInWithCredential:failure", task.exception)
                    // ...
                    //Snackbar.make(view, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    //updateUI(null)
                }

                // ...
            }
    }
}