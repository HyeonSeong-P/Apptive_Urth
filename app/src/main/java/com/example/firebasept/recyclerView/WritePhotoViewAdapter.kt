package com.example.firebasept.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasept.R
import com.google.firebase.auth.FirebaseAuth

class WritePhotoViewAdapter(private val list: List<String>):RecyclerView.Adapter<WritePhotoViewHolder>() {

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WritePhotoViewHolder {
        val myLayout:Int = R.layout.write_post_photo_design
        return WritePhotoViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: WritePhotoViewHolder, position: Int) {
        var auth = FirebaseAuth.getInstance()
        val uri = list!![position]
        holder.bind(uri,position)
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