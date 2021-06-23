package com.example.firebasept.recyclerView

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasept.Data.PostData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.comment_design.view.*

class CommentViewHolder(v: View): RecyclerView.ViewHolder(v) {
    val auth = FirebaseAuth.getInstance()
    var view: View = v
    fun bind(postDTO: PostData, position: Int){
        val nickname = postDTO.comments.toList()[position].first.split(",")[0]
        val time = postDTO.comments.toList()[position].first.split(",")[1]
        Log.d("닉네임",nickname)
        view.comment_time_text.text = time
        view.nickname_text_comment.text = nickname
        view.comment_text.text = postDTO.comments.toList()[position].second
        if(nickname == auth.currentUser.email){
            view.delete_comment_button.visibility = View.VISIBLE
        }
        else{
            view.delete_comment_button.visibility = View.GONE
        }
    }

}