package com.example.firebasept.recyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasept.R
import com.example.firebasept.viewmodel.BrandProductViewModel
import com.example.firebasept.viewmodel.HomeViewModel
import com.example.firebasept.viewmodel.UsStyleViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.post_design.view.*
import kotlinx.android.synthetic.main.product_design.view.*

class SearchProductViewAdapter(private val viewModel: HomeViewModel):RecyclerView.Adapter<SearchProductViewHolder>() {

    override fun getItemCount(): Int {
        return viewModel.getSearchProductData()!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchProductViewHolder {
        val myLayout:Int = R.layout.product_design
        return SearchProductViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: SearchProductViewHolder, position: Int) {
        var auth = FirebaseAuth.getInstance()
        val product = viewModel.getSearchProductData()!![position]
        holder.bind(product,position)
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        var i=0;
        holder.view.heart_button_product.setOnClickListener {
            viewModel.clickLikeProduct(product.productName,product.brandName)
            viewModel.clickLikeProductAdd(product.productName,product.brandName)
            //viewModel.callNotify()
        }
        if(product.likes.containsKey(auth.uid)){
            holder.view.heart_button_product.setImageResource(R.drawable.red_heart)
        }
        else{
            holder.view.heart_button_product.setImageResource(R.drawable.empty_heart)
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