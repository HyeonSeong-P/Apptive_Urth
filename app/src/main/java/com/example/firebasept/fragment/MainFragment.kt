package com.example.firebasept.fragment

import android.content.Intent
import android.os.Bundle
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
import com.example.firebasept.Model.BrandProductDataRepository
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Model.UserDataRepository
import com.example.firebasept.R
import com.example.firebasept.viewmodel.MyPageViewModel
import com.example.firebasept.viewmodel.MyPageViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment:Fragment() {

    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var repository: PostDataRepository
    lateinit var repository2: UserDataRepository
    lateinit var repository3: BrandProductDataRepository
    lateinit var factory: MyPageViewModelFactory
    lateinit var viewModel: MyPageViewModel
    var userDTO: UserDTO? = null
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


        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        repository = PostDataRepository(db)
        repository2 = UserDataRepository(db)
        repository3 = BrandProductDataRepository(db)
        factory = MyPageViewModelFactory(repository,repository2,repository3)
        viewModel = ViewModelProvider(requireParentFragment(),factory).get(
            MyPageViewModel::class.java)



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
        /*email_sign_in_button.setOnClickListener{
            loginUser(email_edit_text.text.toString(),password_edit_text.text.toString())
        }*/
        go_to_sign_up_btn.setOnClickListener {
            findNavController().navigate(R.id.signUpBaseFragment)
        }
        go_to_find_password_btn.setOnClickListener {
            findNavController().navigate(R.id.findPasswordFragment)
        }

        email_sign_in_button.setOnClickListener{
            loginUser(email_edit_text.text.toString(),password_edit_text.text.toString())
        }

    }


    private fun loginUser(email:String, password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    if(user?.isEmailVerified!!){
                        viewModel.editUserPassword(password_edit_text.text.toString())
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