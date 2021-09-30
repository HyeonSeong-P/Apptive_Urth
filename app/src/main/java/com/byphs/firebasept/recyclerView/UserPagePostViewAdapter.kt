package com.byphs.firebasept.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.byphs.firebasept.R
import com.byphs.firebasept.viewmodel.UsStyleViewModel
import com.google.firebase.auth.FirebaseAuth


internal class UserPagePostViewAdapter(private val viewModel: UsStyleViewModel): RecyclerView.Adapter<UserPagePostViewHolder>() {

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