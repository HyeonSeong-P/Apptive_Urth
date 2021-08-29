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
import kotlinx.android.synthetic.main.brand_design.view.*
import kotlinx.android.synthetic.main.post_design.view.*
import kotlinx.android.synthetic.main.product_design.view.*

internal class MyPageBrandViewAdapter(private val viewModel: MyPageViewModel):RecyclerView.Adapter<MyPageBrandViewHolder>() {

    override fun getItemCount(): Int {
        return viewModel.getBrandDataInMyPage()!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageBrandViewHolder {
        val myLayout:Int = R.layout.my_page_brand_design
        return MyPageBrandViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: MyPageBrandViewHolder, position: Int) {
        var auth = FirebaseAuth.getInstance()
        val brand = viewModel.getBrandDataInMyPage()!![position]
        holder.bind(brand,position)
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