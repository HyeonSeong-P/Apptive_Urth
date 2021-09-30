package com.byphs.firebasept.recyclerView


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.byphs.firebasept.R
import com.byphs.firebasept.viewmodel.MyPageViewModel
import com.google.firebase.auth.FirebaseAuth

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