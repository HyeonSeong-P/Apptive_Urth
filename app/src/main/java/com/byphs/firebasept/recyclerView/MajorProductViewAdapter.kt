package com.byphs.firebasept.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.byphs.firebasept.R
import com.byphs.firebasept.viewmodel.BrandProductViewModel
import com.google.firebase.auth.FirebaseAuth

internal class MajorProductViewAdapter(private val viewModel: BrandProductViewModel,private val brandName:String):RecyclerView.Adapter<MajorProductViewHolder>() {

    override fun getItemCount(): Int {
        return viewModel.getMajorProductData(brandName)!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MajorProductViewHolder {
        val myLayout:Int = R.layout.major_product_design
        return MajorProductViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: MajorProductViewHolder, position: Int) {
        var auth = FirebaseAuth.getInstance()
        val product = viewModel.getMajorProductData(brandName)!![position]
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