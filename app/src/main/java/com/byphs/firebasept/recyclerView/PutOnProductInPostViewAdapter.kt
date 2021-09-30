package com.byphs.firebasept.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.byphs.firebasept.Data.ProductData
import com.byphs.firebasept.R
import com.byphs.firebasept.viewmodel.UsStyleViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.product_design.view.*

internal class PutOnProductInPostViewAdapter(private val viewModel: UsStyleViewModel,private val list:List<ProductData>):RecyclerView.Adapter<PutOnProductInPostViewHolder>() {

    override fun getItemCount(): Int {
        return viewModel.getPutOnProductData(list)!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PutOnProductInPostViewHolder {
        val myLayout:Int = R.layout.product_design_in_post
        return PutOnProductInPostViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: PutOnProductInPostViewHolder, position: Int) {
        var auth = FirebaseAuth.getInstance()
        val product = viewModel.getPutOnProductData(list)!![position]
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