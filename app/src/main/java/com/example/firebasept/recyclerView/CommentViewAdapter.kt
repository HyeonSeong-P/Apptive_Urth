package com.example.firebasept.recyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasept.R
import com.example.firebasept.viewmodel.UsStyleViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.comment_design.view.*
import kotlinx.android.synthetic.main.post_design.view.*

class CommentViewAdapter(private val viewModel: UsStyleViewModel, private val postUid: String, private val postTimeStamp:String): RecyclerView.Adapter<CommentViewHolder>() {
    override fun getItemCount(): Int {
        //Log.d("개수",viewModel.getPost!!.commentCount.toString())
        return viewModel.searchPostData(postUid,postTimeStamp)!!.commentCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val myLayout:Int = R.layout.comment_design
        return CommentViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        var auth = FirebaseAuth.getInstance()
        val post = viewModel.searchPostData(postUid,postTimeStamp)!!
        var list = post.comments.toList().sortedBy { it.first.split(",")[1] }
        //val uid = list[position].first.split(",")[0]
        //val time = list[position].first.split(",")[1]
        holder.bind(post,position,viewModel)
        /*holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }*/
        holder.view.delete_comment_button.setOnClickListener {
            //Log.d("Position",position.toString())
            val auth = FirebaseAuth.getInstance()
            //val key = auth.currentUser.uid + "," + holder.view.comment_time_text.text.toString()
            val key = list[position].first
            viewModel.deleteComment(post.uid,post.timestamp,key)
        }

    }

    //    ClickListener
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}