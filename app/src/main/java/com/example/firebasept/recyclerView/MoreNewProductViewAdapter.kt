package com.example.firebasept.recyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasept.Data.ProductData
import com.example.firebasept.R
import com.example.firebasept.viewmodel.BrandProductViewModel
import com.example.firebasept.viewmodel.HomeViewModel
import com.example.firebasept.viewmodel.UsStyleViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.post_design.view.*
import kotlinx.android.synthetic.main.product_design.view.*

internal class MoreNewProductViewAdapter(private val viewModel: HomeViewModel):RecyclerView.Adapter<MoreNewProductViewHolder>() {

    private val TYPE_BRAND_NAME = 1
    private val TYPE_PRODUCT = 0

    override fun getItemCount(): Int {
        return viewModel.getMoreNewProductData()!!.size
    }

    override fun getItemViewType(position: Int): Int {
        var productList: List<ProductData>? = viewModel.getMoreNewProductData()
        //Log.d("dataSize",viewModel.getColdData().size.toString())
        if(productList!![position].productName == ""){
            return TYPE_BRAND_NAME
        }
        else return TYPE_PRODUCT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreNewProductViewHolder {
        val myLayout: Int = when (viewType) {
            TYPE_BRAND_NAME -> R.layout.more_new_product_brand_name_design
            else -> R.layout.more_new_product_design
        }
        return MoreNewProductViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: MoreNewProductViewHolder, position: Int) {
        var auth = FirebaseAuth.getInstance()

        val product = viewModel.getMoreNewProductData()[position]
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