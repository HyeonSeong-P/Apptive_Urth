package com.byphs.firebasept.viewPager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.byphs.firebasept.R

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