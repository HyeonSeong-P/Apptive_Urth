package com.byphs.firebasept.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.byphs.firebasept.Data.ProductData
import com.byphs.firebasept.R
import com.byphs.firebasept.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

internal class HomeProductViewAdapter(private val viewModel: HomeViewModel):RecyclerView.Adapter<HomeProductViewHolder>() {

    private val TYPE_MORE = 1
    private val TYPE_NORMAL = 0

    override fun getItemCount(): Int {
        if(viewModel.getNewProductData()!!.size > 9){
            return viewModel.getNewProductData()!!.subList(0,9).size
        }
        else return viewModel.getNewProductData()!!.size
    }

    override fun getItemViewType(position: Int): Int {
        var productList: List<ProductData>? = viewModel.getNewProductData()
        //Log.d("dataSize",viewModel.getColdData().size.toString())

        if(position == 8 && productList!!.size > 9){
            return TYPE_MORE
        }
        else return TYPE_NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeProductViewHolder {
        val myLayout: Int = when (viewType) {
            TYPE_NORMAL -> R.layout.major_product_design
            else -> R.layout.more_product_design
        }
        return HomeProductViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: HomeProductViewHolder, position: Int) {
        var auth = FirebaseAuth.getInstance()

        val product = if(viewModel.getNewProductData()!!.size > 9){
            viewModel.getNewProductData()!!.subList(0,9)[position]
        }
        else viewModel.getNewProductData()!![position]
        holder.bind(product,position,viewModel.getNewProductData()!!.size)
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