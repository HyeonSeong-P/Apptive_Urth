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

class CommentViewAdapter(private val viewModel: UsStyleViewModel, private val postPosition: Int): RecyclerView.Adapter<CommentViewHolder>() {
    override fun getItemCount(): Int {
        Log.d("개수",viewModel.getPostData()!![postPosition].commentCount.toString())
        return viewModel.getPostData()!![postPosition].commentCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val myLayout:Int = R.layout.comment_design
        return CommentViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        var auth = FirebaseAuth.getInstance()
        val post = viewModel.getPostData()!![postPosition]
        holder.bind(post,position)
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        holder.view.delete_comment_button.setOnClickListener {
            val key = holder.view.nickname_text_comment.text.toString() + "," + holder.view.comment_time_text.text.toString()
            viewModel.deleteComment(postPosition,key)
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