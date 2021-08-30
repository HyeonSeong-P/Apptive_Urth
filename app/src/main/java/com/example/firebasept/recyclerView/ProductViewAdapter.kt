package com.example.firebasept.recyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasept.Data.ProductData
import com.example.firebasept.R
import com.example.firebasept.viewmodel.BrandProductViewModel
import com.example.firebasept.viewmodel.UsStyleViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.post_design.view.*
import kotlinx.android.synthetic.main.product_design.view.*

internal class ProductViewAdapter(private val viewModel: BrandProductViewModel):RecyclerView.Adapter<ProductViewHolder>() {
    private val TYPE_PROGRESS = 1
    private val TYPE_PRODUCT = 0

    var firstLoading = true
    var lastPosition = 0
    private var pageNum = 0
    override fun getItemCount(): Int {
        return viewModel.getSubProductData(pageNum)!!.size
    }

    override fun getItemViewType(position: Int): Int {
        var productList: List<ProductData>? = viewModel.getSubProductData(pageNum)
        //Log.d("dataSize",viewModel.getColdData().size.toString())
        if(productList!![position].productName == "" && productList!![position].brandName == ""){
            return TYPE_PROGRESS
        }
        else return TYPE_PRODUCT
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val myLayout:Int = when(viewType) {
            TYPE_PRODUCT -> R.layout.product_design
            else -> R.layout.progerss_product_paging_design
        }
        return ProductViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        var auth = FirebaseAuth.getInstance()
        val product = viewModel.getSubProductData(pageNum)!![position]
        holder.bind(product,position)
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        if(position == 0){
            if(!firstLoading){
                Log.d("포지션",lastPosition.toString())
                holder.itemView.requestFocus(lastPosition);
            }
            else{
                firstLoading = false
            }
        }
        var i=0;
        if(product.productName != "" || product.brandName != ""){
            holder.view.heart_button_product.setOnClickListener {
                viewModel.clickLikeProduct(product.productName,product.brandName)
                viewModel.clickLikeProductAdd(product.productName,product.brandName)
                viewModel.setChangedProductPosition(position)
                //viewModel.callNotify()
            }
            if(product.likes.containsKey(auth.uid)){
                holder.view.heart_button_product.setImageResource(R.drawable.red_heart)
            }
            else{
                holder.view.heart_button_product.setImageResource(R.drawable.empty_heart)
            }
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

    fun setPageNum(i:Int){
        this.pageNum = i
    }
}