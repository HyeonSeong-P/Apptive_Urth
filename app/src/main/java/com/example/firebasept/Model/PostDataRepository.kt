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


internal class PostDataRepository(private val db: FirebaseFirestore) {
    private val mAuth = FirebaseAuth.getInstance()
    private var sharePostList:MutableList<PostData> = mutableListOf()
    //lateinit var db:FirebaseFirestore
    private val _callBackState = SingleLiveEvent<Boolean>()
    val callBackState get() = _callBackState

    private val _likeState = SingleLiveEvent<Boolean>()
    val likeState get() = _likeState

    private val _commentState = SingleLiveEvent<Boolean>()
    val commentState get() = _commentState

    private var livePostData: MutableLiveData<List<PostData>> = MutableLiveData<List<PostData>>()



    fun getSearchPostData(uid:String,timeStamp:String): PostData? {
        var returnPost: PostData? = null
        for(post in sharePostList){
            if(post.uid == uid && post.timestamp == timeStamp){
                returnPost = post
            }
        }
        //Log.d("in rep", returnPost!!.likeCount.toString())
        return returnPost
    }

    fun getP(){
        var postList:MutableList<PostData> = mutableListOf()

        db.collection("post").get().addOnSuccessListener { snapshot->
            if (snapshot != null) {
                for (doc in snapshot!!) {
                    var postData = doc.toObject(PostData::class.java)
                    postList.add(postData)
                }
                sharePostList = postList
                livePostData.setValue(postList.toList())// 무조건 여기 안에 넣어라!
                _callBackState.setValue(true)

            } else {
                //Log.d(TAG, "Current data: null")
            }
        }
        /*db.collection("post").addSnapshotListener { snapshot, e ->
            Log.d("bull","dk?" )
            if (e != null) {
                //Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                for (doc in snapshot!!) {
                    var postData = doc.toObject(PostData::class.java)
                    postList.add(postData)
                    Log.d("tttt",postData.title)
                }
                livePostData.setValue(postList.toList())// 무조건 여기 안에 넣어라!
                _callBackState.setValue(true)
                //Log.d("wow","메롱")
                //(grid_recyclerview_post.adapter as PostViewAdapter).notifyDataSetChanged()
                //Log.d(TAG, "Current data: ${snapshot.data}")
            } else {
                //Log.d(TAG, "Current data: null")
            }
        }*/
    }

    fun addPost(postData: PostData){
        db.collection("post").add(postData)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                // 자동 뒤로가기는 라이브 데이터를 셋해서 처리해주자. 위에 콜백 스테이트처럼.
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }

    //댓글 달기
    fun registerCommentInRepository(postUid:String, postTimestamp:String, commentInformation:Pair<String,String>) {
        var postCollectionRef: DocumentReference
        db.collection("post").get()
            .addOnSuccessListener { collection ->
                if (collection != null) {
                    var i: Int = 0
                    for (doc in collection!!) {
                        var postData = doc.toObject(PostData::class.java)
                        if (postData.uid == postUid && postData.timestamp == postTimestamp) {
                            postCollectionRef = doc.reference // 스냅샷으로부터 리퍼런스 가져오기
                            db.runTransaction { transaction ->
                                //val snapshot = transaction.get(postCollectionRef)
                                //var postData = doc.toObject(PostData::class.java)
                                postData.comments[commentInformation.first] =
                                    commentInformation.second
                                postData.commentCount += 1
                                transaction.update(postCollectionRef, "comments", postData.comments)
                                transaction.update(
                                    postCollectionRef,
                                    "commentCount",
                                    postData.commentCount
                                )
                            }.addOnSuccessListener {
                                getP()
                                _commentState.setValue(true)
                            }.addOnFailureListener { }
                            break
                        }
                        i += 1

                    }
                } else {

                }
            }
    }

    fun deleteCommentInRepository(postUid:String, postTimestamp:String, key: String){
        var postCollectionRef: DocumentReference
        db.collection("post").get()
            .addOnSuccessListener { collection ->
                if (collection != null) {
                    var i: Int = 0
                    for (doc in collection!!) {
                        var postData = doc.toObject(PostData::class.java)
                        if (postData.uid == postUid && postData.timestamp == postTimestamp) {
                            postCollectionRef = doc.reference // 스냅샷으로부터 리퍼런스 가져오기
                            db.runTransaction { transaction ->
                                //val snapshot = transaction.get(postCollectionRef)
                                postData.comments.remove(key)
                                postData.commentCount -= 1
                                transaction.update(postCollectionRef, "comments", postData.comments)
                                transaction.update(
                                    postCollectionRef,
                                    "commentCount",
                                    postData.commentCount
                                )
                            }.addOnSuccessListener {
                                getP()
                                _commentState.setValue(true)
                            }.addOnFailureListener { }
                            break
                        }
                        i += 1

                    }
                } else {

                }
            }
    }
    //좋아요
    fun clickLikeInRepository(uid:String,timeStamp: String){
        var postCollectionRef:DocumentReference
        db.collection("post").get()
            .addOnSuccessListener { collection ->
                if(collection != null){
                    for (doc in collection!!) {
                        var postData = doc.toObject(PostData::class.java)
                        if(postData.uid == uid && postData.timestamp == timeStamp){
                            postCollectionRef = doc.reference // 스냅샷으로부터 리퍼런스 가져오기
                            db.runTransaction { transaction ->
                                //val snapshot = transaction.get(postCollectionRef)
                                if(postData.likes.containsKey(mAuth.currentUser.uid)){
                                    postData.likeCount -= 1
                                    postData.likes.remove(mAuth.currentUser.uid)
                                }
                                else{
                                    postData.likeCount += 1
                                    postData.likes.put(mAuth.currentUser.uid,true)
                                }
                                transaction.update(postCollectionRef,"likes",postData.likes)
                                transaction.update(postCollectionRef,"likeCount",postData.likeCount)

                            }.addOnSuccessListener {
                                getP()
                                _likeState.setValue(true)
                            }.addOnFailureListener {  }
                            break
                        }
                    }
                }
                else{

                }

            }

        /*db.collection("post").addSnapshotListener { snapshot, e ->
            if (e != null) {
                //Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                var i:Int = 0
                for (doc in snapshot!!) {
                    if(i == position){
                        postCollectionRef = doc.reference // 스냅샷으로부터 리퍼런스 가져오기
                        db.runTransaction { transaction ->
                            //val snapshot = transaction.get(postCollectionRef)
                            var postData = doc.toObject(PostData::class.java)
                            if(postData.likes.containsKey(mAuth.currentUser.uid)){
                                postData.likeCount -= 1
                                postData.likes.remove(mAuth.currentUser.uid)
                            }
                            else{
                                postData.likeCount += 1
                                postData.likes.put(mAuth.currentUser.uid,true)
                            }
                            transaction.update(postCollectionRef,"likes",postData.likes)
                            transaction.update(postCollectionRef,"likeCount",postData.likeCount)
                        }.addOnSuccessListener {  }.addOnFailureListener {  }
                    }

                }
            } else {
                //Log.d(TAG, "Current data: null")
            }
        }*/

    }


    fun deletePost(uid:String,timeStamp: String){
        var postCollectionRef:DocumentReference
        db.collection("post").get()
            .addOnSuccessListener { collection ->
                if(collection != null){
                    for (doc in collection!!) {
                        var postData = doc.toObject(PostData::class.java)
                        if(postData.uid == uid && postData.timestamp == timeStamp){
                            postCollectionRef = doc.reference // 스냅샷으로부터 리퍼런스 가져오기
                            postCollectionRef.delete()
                                .addOnSuccessListener {
                                    getP()
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
    fun getAll(): LiveData<List<PostData>> { return livePostData }

}
