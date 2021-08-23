package com.example.firebasept.recyclerView


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasept.R
import com.example.firebasept.viewmodel.BrandProductViewModel
import com.example.firebasept.viewmodel.UsStyleViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.brand_design.view.*
import kotlinx.android.synthetic.main.post_design.view.*
import kotlinx.android.synthetic.main.product_design.view.*

class BrandViewAdapter(private val viewModel: BrandProductViewModel):RecyclerView.Adapter<BrandViewHolder>() {

    override fun getItemCount(): Int {
        return viewModel.getBrandData()!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val myLayout:Int = R.layout.brand_design
        return BrandViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        var auth = FirebaseAuth.getInstance()
        val brand = viewModel.getBrandData()!![position]
        holder.bind(brand,position)
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        var i=0;
        holder.view.heart_button_brand.setOnClickListener {
            viewModel.clickLikeBrand(brand.brandName)
            viewModel.clickLikeBrandAdd(brand.brandName)
            //viewModel.callNotify()
        }
        if(brand.likes.containsKey(auth.uid)){
            holder.view.heart_button_brand.setImageResource(R.drawable.red_heart)
        }
        else{
            holder.view.heart_button_brand.setImageResource(R.drawable.empty_heart)
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