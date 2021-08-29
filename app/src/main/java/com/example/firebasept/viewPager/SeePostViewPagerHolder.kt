package com.example.firebasept.viewPager

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.brand_design.view.*
import kotlinx.android.synthetic.main.see_post_photo_design.view.*

internal class SeePostViewPagerHolder(v: View): RecyclerView.ViewHolder(v) {
    var view: View = v
    fun bind(position:Int,string:String){
        Glide.with(view).load(string).centerCrop().into(view.photo_image_view_pager)
    }
}