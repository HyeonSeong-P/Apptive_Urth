package com.byphs.firebasept.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.byphs.firebasept.Data.ProductData
import com.byphs.firebasept.R
import com.google.firebase.auth.FirebaseAuth

internal class WritePostProductViewAdapter(private val list: List<ProductData>):RecyclerView.Adapter<WritePostProductViewHolder>() {

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WritePostProductViewHolder {
        val myLayout:Int = R.layout.write_post_product_design
        return WritePostProductViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: WritePostProductViewHolder, position: Int) {
        var auth = FirebaseAuth.getInstance()
        val product = list!![position]
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