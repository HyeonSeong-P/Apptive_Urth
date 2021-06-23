package com.example.firebasept

import android.Manifest
import android.Manifest.permission.*
import android.app.ProgressDialog.show
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener
    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]
    private val RC_SIGN_IN = 10
    private lateinit var googleSignInClient: GoogleSignInClient
    val REQUEST_IMAGE_CAPTURE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 간단하게 권한 요청! 권한 요청 자세한건 하울의 유튜브에서 확인하자.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(arrayOf(
                READ_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION),0)
        }

       // grantUriPermission()

        auth = FirebaseAuth.getInstance()
        initViewFinal()
        mAuthStateListener = FirebaseAuth.AuthStateListener{
            val user = it.currentUser
            if(user!=null){
                if(user.isEmailVerified!!){
                    Toast.makeText(this, "로그인 완료",
                        Toast.LENGTH_SHORT).show()
                    findNavController(R.id.nav_host_fragment).navigate(R.id.homeFragment)
                }
                else{
                    Toast.makeText(this, "이메일 인증을 완료해주세요",
                        Toast.LENGTH_SHORT).show()
                    //auth.signOut() // 여기 넣으면 오류가 나네? 왜지?
                }

            }
            else{ // 로그인 안됐을 경우. 로그아웃등.
                findNavController(R.id.nav_host_fragment).navigate(R.id.mainFragment)
            }

        }
        auth!!.addAuthStateListener(mAuthStateListener!!) // <필수> 어스스테이트리스너 할당하고 추가해줘야함.
        // Configure Google Sign In
        /*val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]*/

        /*login_button.setOnClickListener{
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }*/
    }


   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Toast.makeText(this,"생성 완료",Toast.LENGTH_SHORT).show()
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
    }*/

    fun initViewFinal() {
        //setSupportActionBar(main_toolbar) // 전체화면에 메인 툴바를 넣겠다.

        val host = nav_host_fragment as NavHostFragment //우리가 만든것(nav_host_fragment)과 이미 있는것을 결합.nav_host_fragment 는 view,xml
        //NavHostFragment 는 클래스
        val navController = host.navController // 바로 윗줄 포함 두줄 필수. 네비게이션.xml에 접근위해.

        navController.addOnDestinationChangedListener{_, destination, _ ->
            // 화면이 바뀔때 키보드 무조건 숨김
            val dest: String = try{
                resources.getResourceName(destination.id)
            } catch (e: Exception){
                return@addOnDestinationChangedListener
            }
            //handleToolbar(destination)
        } // 원랜 ({})

    }
    /*private fun updateUI(user: FirebaseUser?) {
        hideProgressBar()
        if (user != null) {
            binding.status.text = getString(R.string.google_status_fmt, user.email)
            binding.detail.text = getString(R.string.firebase_status_fmt, user.uid)

            binding.signInButton.visibility = View.GONE
            binding.signOutAndDisconnect.visibility = View.VISIBLE
        } else {
            binding.status.setText(R.string.signed_out)
            binding.detail.text = null

            binding.signInButton.visibility = View.VISIBLE
            binding.signOutAndDisconnect.visibility = View.GONE
        }
    }*/
}