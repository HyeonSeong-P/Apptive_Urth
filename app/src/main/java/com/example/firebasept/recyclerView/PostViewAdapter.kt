package com.example.firebasept.recyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasept.R
import com.example.firebasept.viewmodel.UsStyleViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.post_design.view.*

class PostViewAdapter(private val viewModel: UsStyleViewModel):RecyclerView.Adapter<PostViewHolder>() {

    override fun getItemCount(): Int {
        return viewModel.getPostData()!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val myLayout:Int = R.layout.post_design
        return PostViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        var auth = FirebaseAuth.getInstance()
        val post = viewModel.getPostData()!![position]
        holder.bind(post,position)
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        var i=0;
        holder.view.heart_button.setOnClickListener {
            viewModel.clickLike(position)
            //viewModel.callNotify()
        }
        if(post.likes.containsKey(auth.uid)){
            holder.view.heart_button.setImageResource(R.drawable.ic_baseline_favorite_24)
        }
        else{
            holder.view.heart_button.setImageResource(R.drawable.ic_baseline_favorite_border_24)
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