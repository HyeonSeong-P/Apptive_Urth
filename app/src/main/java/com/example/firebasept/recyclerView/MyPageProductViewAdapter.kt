package com.example.firebasept.recyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasept.R
import com.example.firebasept.viewmodel.BrandProductViewModel
import com.example.firebasept.viewmodel.MyPageViewModel
import com.example.firebasept.viewmodel.UsStyleViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.post_design.view.*
import kotlinx.android.synthetic.main.product_design.view.*

internal class MyPageProductViewAdapter(private val viewModel: MyPageViewModel):RecyclerView.Adapter<MyPageProductViewHolder>() {

    override fun getItemCount(): Int {
        return viewModel.getProductDataInMyPage()!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageProductViewHolder {
        val myLayout:Int = R.layout.product_design
        return MyPageProductViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: MyPageProductViewHolder, position: Int) {
        var auth = FirebaseAuth.getInstance()
        val product = viewModel.getProductDataInMyPage()!![position]
        holder.bind(product,position)
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