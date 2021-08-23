package com.example.firebasept.Model

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.firebasept.Data.PostData
import com.example.firebasept.Data.UserDTO
import com.example.firebasept.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class UserDataRepository(private val db: FirebaseFirestore) {
    private val tagList = arrayListOf<String>("#비건소재","#사회공헌/기부","#업사이클링","#친환경소재","#동물복지")

    private val mAuth = FirebaseAuth.getInstance()
    private var shareUserList:MutableList<UserDTO> = mutableListOf()

    //lateinit var db:FirebaseFirestore
    private val _callBackState = SingleLiveEvent<Boolean>()
    val callBackState get() = _callBackState

    private var liveUserData: MutableLiveData<List<UserDTO>> = MutableLiveData<List<UserDTO>>()


    fun getU(){
        var userList:MutableList<UserDTO> = mutableListOf()

        db.collection("user").get().addOnSuccessListener { snapshot->
            if (snapshot != null) {
                for (doc in snapshot!!) {
                    var userDTO = doc.toObject(UserDTO::class.java)
                    userList.add(userDTO)
                }
                shareUserList = userList
                liveUserData.setValue(userList.toList())// 무조건 여기 안에 넣어라!
                _callBackState.setValue(true)

            } else {
                //Log.d(TAG, "Current data: null")
            }
        }
    }

    /*fun getUserDirect(uid: String): UserDTO? {
        var returnUser:UserDTO? = null
        db.collection("user").get().addOnSuccessListener { snapshot->
            if (snapshot != null) {
                for (doc in snapshot!!) {
                    var userDTO = doc.toObject(UserDTO::class.java)
                    if(userDTO.uid == uid){
                        returnUser = userDTO
                        break
                    }
                }
            } else {
                //Log.d(TAG, "Current data: null")
            }
        }
        return returnUser
    }*/
    fun getUserDTO(uid:String): UserDTO? {
        var returnUser:UserDTO? = null
        for(user in shareUserList){
            if(user.uid == uid){
                returnUser = user
            }
        }
        return returnUser
    }

    fun addUser(userDTO: UserDTO){
        db.collection("user").add(userDTO)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                // 자동 뒤로가기는 라이브 데이터를 셋해서 처리해주자. 위에 콜백 스테이트처럼.
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }






    // 태그 선택
    // 태그 포지션
    // 0. #비건소재  1. #사회공헌/기부  2. #업사이클링  3. #친환경소재  4. #동물복지
    fun clickTagInRepository(tagPosition:Int) {
        var currUserUid = mAuth.currentUser.uid
        var userCollectionRef: DocumentReference
        db.collection("user").get()
            .addOnSuccessListener { collection ->
                if (collection != null) {
                    for (doc in collection!!) {
                        var userData = doc.toObject(UserDTO::class.java)
                        if (userData.uid == currUserUid) {
                            userCollectionRef = doc.reference // 스냅샷으로부터 리퍼런스 가져오기
                            db.runTransaction { transaction ->
                                if (userData.tags.containsKey(tagList[tagPosition])) {
                                    userData.tags.remove(tagList[tagPosition])
                                } else {
                                    userData.tags.put(tagList[tagPosition], true)
                                }
                                transaction.update(userCollectionRef, "tags", userData.tags)
                            }.addOnSuccessListener {
                                getU()
                                //따로 콜백을 줄까?
                            }.addOnFailureListener { }
                            break
                        }
                    }
                }
                else {
                }
            }
    }

    fun clickAllTagInRepository() {
        var allFlag:Boolean = true
        var currUserUid = mAuth.currentUser.uid
        var userCollectionRef: DocumentReference
        db.collection("user").get()
            .addOnSuccessListener { collection ->
                if (collection != null) {
                    for (doc in collection!!) {
                        var userData = doc.toObject(UserDTO::class.java)
                        if (userData.uid == currUserUid) {
                            userCollectionRef = doc.reference // 스냅샷으로부터 리퍼런스 가져오기
                            db.runTransaction { transaction ->
                                for(tagString in tagList){
                                    if(!(userData.tags.containsKey(tagString))) allFlag = false
                                }
                                if(!allFlag){
                                    for(tagString in tagList){
                                        userData.tags[tagString] = true
                                    }
                                }
                                else userData.tags.clear()
                                transaction.update(userCollectionRef, "tags", userData.tags)
                            }.addOnSuccessListener {
                                getU()
                                //따로 콜백을 줄까?
                            }.addOnFailureListener { }
                            break
                        }
                    }
                }
                else {
                }
            }
    }
    fun getAll(): LiveData<List<UserDTO>> { return liveUserData }

    //좋아요한 포스트
    fun clickLikePostAddInRepository(uid: String,timeStamp:String) {
        var userCollectionRef: DocumentReference
        db.collection("user").get()
            .addOnSuccessListener { collection ->
                if (collection != null) {
                    for (doc in collection!!) {
                        var userDTO = doc.toObject(UserDTO::class.java)
                        if (mAuth.currentUser.uid == userDTO.uid) {
                            userCollectionRef = doc.reference // 스냅샷으로부터 리퍼런스 가져오기
                            db.runTransaction { transaction ->
                                //val snapshot = transaction.get(postCollectionRef)
                                if (userDTO.postLikes.containsKey("$uid,$timeStamp")) {
                                    userDTO.postLikesCount -= 1
                                    userDTO.postLikes.remove("$uid,$timeStamp")
                                } else {
                                    userDTO.postLikesCount += 1
                                    userDTO.postLikes.put("$uid,$timeStamp",true)
                                }
                                transaction.update(userCollectionRef, "postLikes", userDTO.postLikes)
                                transaction.update(
                                    userCollectionRef,
                                    "postLikesCount",
                                    userDTO.postLikesCount
                                )

                            }.addOnSuccessListener {
                                getU()
                            }.addOnFailureListener { }
                            break
                        }
                    }
                } else {

                }

            }
    }

    //좋아요한 브랜드
    fun clickLikeBrandAddInRepository(brandName: String) {
        var userCollectionRef: DocumentReference
        db.collection("user").get()
            .addOnSuccessListener { collection ->
                if (collection != null) {
                    for (doc in collection!!) {
                        var userDTO = doc.toObject(UserDTO::class.java)
                        if (mAuth.currentUser.uid == userDTO.uid) {
                            userCollectionRef = doc.reference // 스냅샷으로부터 리퍼런스 가져오기
                            db.runTransaction { transaction ->
                                //val snapshot = transaction.get(postCollectionRef)
                                if (userDTO.brandLikes.containsKey(brandName)) {
                                    userDTO.brandLikesCount -= 1
                                    userDTO.brandLikes.remove(brandName)
                                } else {
                                    userDTO.brandLikesCount += 1
                                    userDTO.brandLikes.put(brandName,true)
                                }
                                transaction.update(userCollectionRef, "brandLikes", userDTO.brandLikes)
                                transaction.update(
                                    userCollectionRef,
                                    "brandLikesCount",
                                    userDTO.brandLikesCount
                                )

                            }.addOnSuccessListener {
                                getU()
                            }.addOnFailureListener { }
                            break
                        }
                    }
                } else {

                }

            }
    }


    //좋아요한 상품
    fun clickLikeProductAddInRepository(productName: String,brandName:String) {
        var userCollectionRef: DocumentReference
        db.collection("user").get()
            .addOnSuccessListener { collection ->
                if (collection != null) {
                    for (doc in collection!!) {
                        var userDTO = doc.toObject(UserDTO::class.java)
                        if (mAuth.currentUser.uid == userDTO.uid) {
                            userCollectionRef = doc.reference // 스냅샷으로부터 리퍼런스 가져오기
                            db.runTransaction { transaction ->
                                //val snapshot = transaction.get(postCollectionRef)
                                if (userDTO.productLikes.containsKey("$productName,$brandName")) {
                                    userDTO.productLikesCount -= 1
                                    userDTO.productLikes.remove("$productName,$brandName")
                                } else {
                                    userDTO.productLikesCount += 1
                                    userDTO.productLikes.put("$productName,$brandName",true)
                                }
                                transaction.update(userCollectionRef, "productLikes", userDTO.productLikes)
                                transaction.update(
                                    userCollectionRef,
                                    "productLikesCount",
                                    userDTO.productLikesCount
                                )

                            }.addOnSuccessListener {
                                getU()
                            }.addOnFailureListener { }
                            break
                        }
                    }
                } else {

                }

            }
    }


    fun editUserNicknameInRepository(nickname:String){
        var userCollectionRef: DocumentReference
        db.collection("user").get()
            .addOnSuccessListener { collection ->
                if (collection != null) {
                    for (doc in collection!!) {
                        var userDTO = doc.toObject(UserDTO::class.java)
                        if (mAuth.currentUser.uid == userDTO.uid) {
                            userCollectionRef = doc.reference // 스냅샷으로부터 리퍼런스 가져오기
                            db.runTransaction { transaction ->
                                userDTO.nickName = nickname
                                transaction.update(userCollectionRef, "nickName", userDTO.nickName)
                            }.addOnSuccessListener {
                                getU()
                            }.addOnFailureListener { }
                            break
                        }
                    }
                } else {

                }

            }
    }

    fun editUserPasswordInRepository(password:String){
        var userCollectionRef: DocumentReference
        db.collection("user").get()
            .addOnSuccessListener { collection ->
                if (collection != null) {
                    for (doc in collection!!) {
                        var userDTO = doc.toObject(UserDTO::class.java)
                        if (mAuth.currentUser.uid == userDTO.uid) {
                            userCollectionRef = doc.reference // 스냅샷으로부터 리퍼런스 가져오기
                            db.runTransaction { transaction ->
                                userDTO.password = password
                                transaction.update(userCollectionRef, "password", userDTO.password)
                            }.addOnSuccessListener {
                                getU()
                            }.addOnFailureListener { }
                            break
                        }
                    }
                } else {

                }

            }
    }

    fun editUserImageInRepository(uri:String){
        var userCollectionRef: DocumentReference
        db.collection("user").get()
            .addOnSuccessListener { collection ->
                if (collection != null) {
                    for (doc in collection!!) {
                        var userDTO = doc.toObject(UserDTO::class.java)
                        if (mAuth.currentUser.uid == userDTO.uid) {
                            userCollectionRef = doc.reference // 스냅샷으로부터 리퍼런스 가져오기
                            db.runTransaction { transaction ->
                                userDTO.userImage = uri
                                transaction.update(userCollectionRef, "userImage", userDTO.userImage)
                            }.addOnSuccessListener {
                                getU()
                            }.addOnFailureListener { }
                            break
                        }
                    }
                } else {

                }

            }
    }

    fun editUserIntroductionInRepository(introduction:String){
        var userCollectionRef: DocumentReference
        db.collection("user").get()
            .addOnSuccessListener { collection ->
                if (collection != null) {
                    for (doc in collection!!) {
                        var userDTO = doc.toObject(UserDTO::class.java)
                        if (mAuth.currentUser.uid == userDTO.uid) {
                            userCollectionRef = doc.reference // 스냅샷으로부터 리퍼런스 가져오기
                            db.runTransaction { transaction ->
                                userDTO.userIntroduction = introduction
                                transaction.update(userCollectionRef, "userIntroduction", userDTO.userIntroduction)
                            }.addOnSuccessListener {
                                getU()
                            }.addOnFailureListener { }
                            break
                        }
                    }
                } else {

                }

            }
    }

    fun deleteUser(uid:String){
        var userCollectionRef:DocumentReference
        db.collection("user").get()
            .addOnSuccessListener { collection ->
                if(collection != null){
                    for (doc in collection!!) {
                        var userDTO = doc.toObject(UserDTO::class.java)
                        if(userDTO.uid == uid){
                            userCollectionRef = doc.reference // 스냅샷으로부터 리퍼런스 가져오기
                            userCollectionRef.delete()
                                .addOnSuccessListener {
                                    getU()
                                }
                                .addOnFailureListener {  }
                            break
                        }
                    }
                }
                else{

                }

            }
    }
}
