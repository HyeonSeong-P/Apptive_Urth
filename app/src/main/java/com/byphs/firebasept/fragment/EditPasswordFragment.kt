package com.byphs.firebasept.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.byphs.firebasept.Data.UserDTO
import com.byphs.firebasept.Model.BrandProductDataRepository
import com.byphs.firebasept.Model.PostDataRepository
import com.byphs.firebasept.Model.UserDataRepository
import com.byphs.firebasept.R
import com.byphs.firebasept.viewmodel.MyPageViewModel
import com.byphs.firebasept.viewmodel.MyPageViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_edit_password.*
import kotlinx.android.synthetic.main.fragment_setting.*

internal class EditPasswordFragment: Fragment(){
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var repository: PostDataRepository
    lateinit var repository2: UserDataRepository
    lateinit var repository3: BrandProductDataRepository
    lateinit var factory: MyPageViewModelFactory
    lateinit var viewModel: MyPageViewModel
    var userDTO: UserDTO? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_password, container, false)
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

        viewModel.initU()

        viewModel.allUserData.observe(viewLifecycleOwner, Observer {
            userDTO = viewModel.getUser(auth.currentUser!!.uid)
            editPasswordButtonEvent()
        })

    }

    private fun editPasswordButtonEvent(){
        password_edit_button.setOnClickListener {
            if(confirm_password_guide_text.text.toString() == "" || curr_password_guide_text.text.toString() == ""
                || new_password_guide_text.text.toString() == ""){
                Toast.makeText(requireContext(),"??? ?????? ?????? ??????????????????",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(userDTO!!.password == curr_password_guide_text.text.toString()){
                if(confirm_password_guide_text.text.toString() != new_password_guide_text.text.toString()){
                    Toast.makeText(requireContext(),"????????? ???????????? ????????? ?????? ????????????",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else{
                    auth.currentUser!!.updatePassword(new_password_guide_text.text.toString())
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                viewModel.editUserPassword(new_password_guide_text.text.toString())
                                confirm_password_guide_text.text = null
                                curr_password_guide_text.text = null
                                new_password_guide_text.text = null
                                Toast.makeText(requireContext(),"???????????? ????????? ??????????????????",Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
            else {
                Toast.makeText(requireContext(),"?????? ??????????????? ???????????? ????????????",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
    }
}