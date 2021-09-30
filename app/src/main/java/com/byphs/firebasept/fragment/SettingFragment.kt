package com.byphs.firebasept.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.byphs.firebasept.Data.UserDTO
import com.byphs.firebasept.Model.BrandProductDataRepository
import com.byphs.firebasept.Model.PostDataRepository
import com.byphs.firebasept.Model.UserDataRepository
import com.byphs.firebasept.R
import com.byphs.firebasept.viewmodel.MyPageViewModel
import com.byphs.firebasept.viewmodel.MyPageViewModelFactory
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.auth_delete_dialog.*
import kotlinx.android.synthetic.main.fragment_setting.*
import java.io.File

internal class SettingFragment: Fragment() {

    lateinit var stor:FirebaseStorage
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var repository: PostDataRepository
    lateinit var repository2: UserDataRepository
    lateinit var repository3: BrandProductDataRepository
    lateinit var factory: MyPageViewModelFactory
    lateinit var viewModel: MyPageViewModel
    lateinit var dialog: Dialog
    var imagePath:String = ""
    var userDTO: UserDTO? = null

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            imagePath = getPath(result.data?.data!!)
            upload(imagePath)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        stor = Firebase.storage
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        repository = PostDataRepository(db)
        repository2 = UserDataRepository(db)
        repository3 = BrandProductDataRepository(db)
        factory = MyPageViewModelFactory(repository,repository2,repository3)
        viewModel = ViewModelProvider(requireParentFragment(),factory).get(
            MyPageViewModel::class.java)

        // 다이얼로그 팝업
        dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.auth_delete_dialog)
        // 이걸 추가해야 뒤에 흰배경 없어짐!
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));


        viewModel.initU()

        back_btn_setting.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.allUserData.observe(viewLifecycleOwner, Observer {
            userDTO = viewModel.getUser(auth.currentUser!!.uid)
            nickname_edit_text_setting.hint = userDTO!!.nickName
            email_edit_text_setting.hint = userDTO!!.userEmail
            if(userDTO!!.userImage != ""){
                Glide.with(view).load(userDTO!!.userImage).into(user_image_setting)// url로 불러올때 이거쓰자! 이게 좋다!!
            }

            edit_nickname_btn_setting.setOnClickListener {
                if(nickname_edit_text_setting.text.toString().length < 2){
                    Toast.makeText(requireContext(),"닉네임은 두 글자 이상으로 작성해주세요",Toast.LENGTH_SHORT).show()
                }
                else{
                    viewModel.editUserNickname(nickname_edit_text_setting.text.toString())
                }
            }
        })

        edit_password_btn_setting.setOnClickListener {
            findNavController().navigate(R.id.editPasswordFragment)
        }
        exit_urth_btn.setOnClickListener {
            showDialog()
        }
        logout_btn_setting.setOnClickListener {
            auth.signOut()
        }
        edit_user_image_btn.setOnClickListener {
            stor = Firebase.storage
            var intent = Intent(Intent.ACTION_PICK)
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE)
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            getContent.launch(intent)
            //startActivityForResult(Intent.createChooser(intent, "Select Picture"),  100)
        }
    }

    private fun showDialog(){
        dialog.show()

        dialog.auth_delete_btn.setOnClickListener {
            val user = Firebase.auth.currentUser!!

            // Get auth credentials from the user for re-authentication. The example below shows
            // email and password credentials but there are multiple possible providers,
            // such as GoogleAuthProvider or FacebookAuthProvider.
            val credential = EmailAuthProvider
                .getCredential(userDTO!!.userEmail, userDTO!!.password)

            // Prompt the user to re-provide their sign-in credentials
            user.reauthenticate(credential)
                .addOnCompleteListener { Log.d("TAG", "User re-authenticated.") }


            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        viewModel.deleteUser(userDTO!!.uid)
                        Log.d("TAG", "User account deleted.")
                        auth.signOut()
                        dialog.dismiss()
                    }
                }
                .addOnFailureListener {
                    Log.d("TAG", auth.currentUser!!.uid)
                    Log.d("TAG", "User account not deleted.") }
        }
        dialog.cancel_auth_delete_btn.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun getPath(uri: Uri):String{
        var proj:Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var cursorLoader = CursorLoader(requireContext(),uri,proj,null,null,null)
        var cursor = cursorLoader.loadInBackground()
        //var c = getContentResolver().query
        var index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()

        return cursor!!.getString(index!!)
    }
    fun upload(uri:String) {

        var downloadUrl1: Uri?
        // 파이어스토리지에 사진 올리기
        // Create a storage reference from our app
        val storageRef = stor.reference
        var file = Uri.fromFile(File(uri))
        val riversRef = storageRef.child("images/${file.lastPathSegment}")
        var uploadTask = riversRef.putFile(file)


        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener {
            //파이어스토리지로 사진 올렸다면 그 url정보를 토대로 파이어스토어로 데이터 올리기기
            riversRef.downloadUrl.addOnSuccessListener { it1 ->
                downloadUrl1 = it1
                viewModel.editUserImage(downloadUrl1.toString())
                //Glide.with(view).load(downloadUrl1.toString()).into(user_image_setting)
            }
        }
    }

}