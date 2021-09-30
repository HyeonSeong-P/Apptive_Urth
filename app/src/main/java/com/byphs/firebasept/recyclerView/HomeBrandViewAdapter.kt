package com.byphs.firebasept.recyclerView


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.byphs.firebasept.R
import com.byphs.firebasept.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

internal class HomeBrandViewAdapter(private val viewModel: HomeViewModel):RecyclerView.Adapter<HomeBrandViewHolder>() {

    override fun getItemCount(): Int {
        return viewModel.getBestBrandData()!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeBrandViewHolder {
        val myLayout:Int = R.layout.best_brand_design
        return HomeBrandViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: HomeBrandViewHolder, position: Int) {
        var auth = FirebaseAuth.getInstance()
        val brand = viewModel.getBestBrandData()!![position]
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