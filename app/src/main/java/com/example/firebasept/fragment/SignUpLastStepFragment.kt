package com.example.firebasept.fragment

import android.content.ContentValues
import android.graphics.Color
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
import com.example.firebasept.Model.UserDataRepository
import com.example.firebasept.R
import com.example.firebasept.viewmodel.SignUpViewModel
import com.example.firebasept.viewmodel.SignUpViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.bottom_sheet_init_tag.*
import kotlinx.android.synthetic.main.fragment_write_post.*

class SignUpLastStepFragment:Fragment() {
    private var clickState:ArrayList<Boolean> = arrayListOf(false,false,false,false,false)
    private val tagList = arrayListOf<String>("#비건소재","#사회공헌/기부","#업사이클링","#친환경소재","#동물복지")
    private var userTagList = hashMapOf<String,Boolean>()
    private lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var repository: UserDataRepository
    lateinit var factory: SignUpViewModelFactory
    lateinit var viewModel: SignUpViewModel
    var userDTO:UserDTO = UserDTO()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_init_tag, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState) // 상위 클래스에서 함수를 참조하려면 다음 예와 같이 super 키워드를 사용합니다.
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        repository = UserDataRepository(db)
        factory = SignUpViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(),factory).get(
            SignUpViewModel::class.java)

        viewModel.userDTOForLastStep.observe(viewLifecycleOwner, Observer {
            userDTO = it
        })
        complete_sign_up_btn_tag_init.isEnabled = false
        buttonEvent()

    }
    private fun buttonEvent(){

        complete_sign_up_btn_tag_init.setOnClickListener {
            userDTO.tags = userTagList
            createUser(userDTO)

        }

        social_tag_init.setOnClickListener {
            if(userTagList.containsKey(tagList[1])){
                userTagList.remove(tagList[1])
                setTagButtonDesign(1,false)
            }
            else{
                userTagList[tagList[1]] = true
                setTagButtonDesign(1,true)
            }
            if(userTagList.size == 0){
                complete_sign_up_btn_tag_init.setTextColor(Color.parseColor("#aaaaaa"))
                complete_sign_up_btn_tag_init.isEnabled = false
            }
            else{
                complete_sign_up_btn_tag_init.setTextColor(Color.parseColor("#3e3a39"))
                complete_sign_up_btn_tag_init.isEnabled = true
            }
        }
        up_tag_init.setOnClickListener {
            if(userTagList.containsKey(tagList[2])){
                userTagList.remove(tagList[2])
                setTagButtonDesign(2,false)
            }
            else{
                userTagList[tagList[2]] = true
                setTagButtonDesign(2,true)
            }
            if(userTagList.size == 0){
                complete_sign_up_btn_tag_init.setTextColor(Color.parseColor("#aaaaaa"))
                complete_sign_up_btn_tag_init.isEnabled = false
            }
            else{
                complete_sign_up_btn_tag_init.setTextColor(Color.parseColor("#3e3a39"))
                complete_sign_up_btn_tag_init.isEnabled = true
            }
        }
        eco_tag_init.setOnClickListener {
            if(userTagList.containsKey(tagList[3])){
                userTagList.remove(tagList[3])
                setTagButtonDesign(3,false)
            }
            else{
                userTagList[tagList[3]] = true
                setTagButtonDesign(3,true)
            }
            if(userTagList.size == 0){
                complete_sign_up_btn_tag_init.setTextColor(Color.parseColor("#aaaaaa"))
                complete_sign_up_btn_tag_init.isEnabled = false
            }
            else{
                complete_sign_up_btn_tag_init.setTextColor(Color.parseColor("#3e3a39"))
                complete_sign_up_btn_tag_init.isEnabled = true
            }
        }
        animal_tag_init.setOnClickListener {
            if(userTagList.containsKey(tagList[4])){
                userTagList.remove(tagList[4])
                setTagButtonDesign(4,false)
            }
            else{
                userTagList[tagList[4]] = true
                setTagButtonDesign(4,true)
            }
            if(userTagList.size == 0){
                complete_sign_up_btn_tag_init.setTextColor(Color.parseColor("#aaaaaa"))
                complete_sign_up_btn_tag_init.isEnabled = false
            }
            else{
                complete_sign_up_btn_tag_init.setTextColor(Color.parseColor("#3e3a39"))
                complete_sign_up_btn_tag_init.isEnabled = true
            }
        }
        vegan_tag_init.setOnClickListener {
            if(userTagList.containsKey(tagList[0])){
                userTagList.remove(tagList[0])
                setTagButtonDesign(0,false)
            }
            else{
                userTagList[tagList[0]] = true
                setTagButtonDesign(0,true)
            }
            if(userTagList.size == 0){
                complete_sign_up_btn_tag_init.setTextColor(Color.parseColor("#aaaaaa"))
                complete_sign_up_btn_tag_init.isEnabled = false
            }
            else{
                complete_sign_up_btn_tag_init.setTextColor(Color.parseColor("#3e3a39"))
                complete_sign_up_btn_tag_init.isEnabled = true
            }
        }
    }


    fun setTagButtonDesign(tagPosition:Int,flag:Boolean){
        when(tagPosition){
            1 -> {
                if(!flag){
                    social_title_text_init.setTextColor(Color.parseColor("#3e3a39"))
                    social_title_text_init.setBackgroundResource(R.drawable.circle_image)
                    social_tag_init.setBackgroundResource(R.drawable.init_tag_background)
                }
                else{
                    social_title_text_init.setTextColor(Color.parseColor("#faf8f7"))
                    social_title_text_init.setBackgroundResource(R.drawable.selected_circle_image)
                    social_tag_init.setBackgroundResource(R.drawable.init_tag_selected_background)
                }
            }
            2 -> {
                if(!flag){
                    up_title_text_init.setTextColor(Color.parseColor("#3e3a39"))
                    up_title_text_init.setBackgroundResource(R.drawable.circle_image)
                    up_tag_init.setBackgroundResource(R.drawable.init_tag_background)
                }
                else{
                    up_title_text_init.setTextColor(Color.parseColor("#faf8f7"))
                    up_title_text_init.setBackgroundResource(R.drawable.selected_circle_image)
                    up_tag_init.setBackgroundResource(R.drawable.init_tag_selected_background)
                }
            }
            3 -> {
                if(!flag){
                    eco_title_text_init.setTextColor(Color.parseColor("#3e3a39"))
                    eco_title_text_init.setBackgroundResource(R.drawable.circle_image)
                    eco_tag_init.setBackgroundResource(R.drawable.init_tag_background)
                }
                else{
                    eco_title_text_init.setTextColor(Color.parseColor("#faf8f7"))
                    eco_title_text_init.setBackgroundResource(R.drawable.selected_circle_image)
                    eco_tag_init.setBackgroundResource(R.drawable.init_tag_selected_background)
                }
            }
            4 -> {
                if(!flag){
                    animal_title_text_init.setTextColor(Color.parseColor("#3e3a39"))
                    animal_title_text_init.setBackgroundResource(R.drawable.circle_image)
                    animal_tag_init.setBackgroundResource(R.drawable.init_tag_background)
                }
                else{
                    animal_title_text_init.setTextColor(Color.parseColor("#faf8f7"))
                    animal_title_text_init.setBackgroundResource(R.drawable.selected_circle_image)
                    animal_tag_init.setBackgroundResource(R.drawable.init_tag_selected_background)
                }
            }
            else -> {
                if(!flag){
                    vegan_title_text_init.setTextColor(Color.parseColor("#3e3a39"))
                    vegan_title_text_init.setBackgroundResource(R.drawable.circle_image)
                    vegan_tag_init.setBackgroundResource(R.drawable.init_tag_background)
                }
                else{
                    vegan_title_text_init.setTextColor(Color.parseColor("#faf8f7"))
                    vegan_title_text_init.setBackgroundResource(R.drawable.selected_circle_image)
                    vegan_tag_init.setBackgroundResource(R.drawable.init_tag_selected_background)
                }
            }
        }
    }

    private fun createUser(userDTO:UserDTO){
        auth.createUserWithEmailAndPassword(userDTO.userEmail, userDTO.password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    val userData: UserDTO = userDTO
                    userData.userEmail = user!!.email
                    userData.uid = user!!.uid

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