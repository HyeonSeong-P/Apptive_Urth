package com.example.firebasept.viewPager

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasept.R
import com.example.firebasept.recyclerView.HomeProductViewPagerHolder
import com.example.firebasept.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.major_product_design_for_view_pager2.view.*

internal class SeePostViewPagerAdapter(private val list: List<String>):
    RecyclerView.Adapter<SeePostViewPagerHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeePostViewPagerHolder {
        val myLayout: Int = R.layout.see_post_photo_design

        return SeePostViewPagerHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: SeePostViewPagerHolder, position: Int) {
        holder.bind(position,list[position])
    }

}