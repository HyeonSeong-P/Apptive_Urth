package com.example.firebasept.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasept.R
import com.example.firebasept.viewmodel.MyPageViewModel
import com.example.firebasept.viewmodel.UsStyleViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.post_design.view.*


internal class MyPagePostViewAdapter(private val viewModel: MyPageViewModel): RecyclerView.Adapter<MYPagePostViewHolder>() {

    override fun getItemCount(): Int {
        return viewModel.getPostDataInMyPage()!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MYPagePostViewHolder {
        val myLayout:Int = R.layout.post_design_my_page
        return MYPagePostViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: MYPagePostViewHolder, position: Int) {
        var auth = FirebaseAuth.getInstance()
        val post = viewModel.getPostDataInMyPage()!![position]
        holder.bind(post,position)
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
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