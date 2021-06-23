package com.example.firebasept.fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.loader.content.CursorLoader
import androidx.navigation.fragment.findNavController
import com.example.firebasept.Data.PostData
import com.example.firebasept.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_write_post.*
import java.io.File

class WritePostFragment: Fragment() {
    val GALLERY_CODE = 199
    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    lateinit var  db:FirebaseFirestore
    lateinit var stor:FirebaseStorage
    lateinit var imagePath:String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_write_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //stor = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        gallery_btn.setOnClickListener {
            stor = Firebase.storage
            var intent = Intent(Intent.ACTION_PICK)
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE)

            startActivityForResult(intent,GALLERY_CODE)
        }

        upload_btn.setOnClickListener {
            upload(imagePath)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == GALLERY_CODE){
            imagePath = getPath(data?.data!!)
            var f:File? = File(imagePath)
            imageView.setImageURI(Uri.fromFile(f))

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
    fun upload(uri:String){
        var downloadUrl1:Uri?
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
           var postDTO: PostData =
               PostData()
            riversRef.downloadUrl.addOnSuccessListener {
                    it1 ->
                Log.d("성공?","성공했다!!!!")
                downloadUrl1 = it1
                Log.d("성공2?",downloadUrl1.toString())
                postDTO.imageUrl = downloadUrl1.toString()
                postDTO.title= title_edit_text.text.toString()
                postDTO.description = description_edit_text.text.toString()
                postDTO.uid = auth.currentUser.uid
                postDTO.userID = auth.currentUser.email

                db.collection("post")
                    .add(postDTO)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        findNavController().navigateUp()
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }
            }




            //db.reference.child("post").push().setValue(postDTO)

        }
    }
}