package com.byphs.firebasept.recyclerView


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.byphs.firebasept.R
import com.byphs.firebasept.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.search_brand_design.view.*

internal class SearchBrandViewAdapter(private val viewModel: HomeViewModel):RecyclerView.Adapter<SearchBrandViewHolder>() {

    override fun getItemCount(): Int {
        return viewModel.getSearchBrandData()!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchBrandViewHolder {
        val myLayout:Int = R.layout.search_brand_design
        return SearchBrandViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: SearchBrandViewHolder, position: Int) {
        var auth = FirebaseAuth.getInstance()
        val brand = viewModel.getSearchBrandData()!![position]
        holder.bind(brand,position)
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        var i=0;
        holder.view.heart_button_search_brand.setOnClickListener {
            viewModel.clickLikeBrand(brand.brandName)
            viewModel.clickLikeBrandAdd(brand.brandName)
            //viewModel.callNotify()
        }
        if(brand.likes.containsKey(auth.uid)){
            holder.view.heart_button_search_brand.setImageResource(R.drawable.red_heart)
        }
        else{
            holder.view.heart_button_search_brand.setImageResource(R.drawable.empty_heart)
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