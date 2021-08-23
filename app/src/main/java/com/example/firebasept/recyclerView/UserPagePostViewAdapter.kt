package com.example.firebasept.recyclerView

import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasept.R
import com.example.firebasept.viewmodel.MyPageViewModel
import com.example.firebasept.viewmodel.UsStyleViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.post_design.view.*


class UserPagePostViewAdapter(private val viewModel: UsStyleViewModel): RecyclerView.Adapter<UserPagePostViewHolder>() {

    override fun getItemCount(): Int {
        return viewModel.getUserPagePostData()!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPagePostViewHolder {
        val myLayout:Int = R.layout.post_design_user_page
        return UserPagePostViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: UserPagePostViewHolder, position: Int) {
/*

        val displayMetrics = DisplayMetrics()
        var a = holder.itemView.context as Activity
        a.windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        val width = displayMetrics.widthPixels * (0.32)
        val height = width * (170/118)


        var layoutParams = holder.itemView.layoutParams
        layoutParams.width = width.toInt()
        layoutParams.height = height.toInt()
        holder.itemView.requestLayout();
*/

        var auth = FirebaseAuth.getInstance()
        val post = viewModel.getUserPagePostData()!![position]
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