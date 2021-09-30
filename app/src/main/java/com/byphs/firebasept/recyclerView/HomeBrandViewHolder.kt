package com.byphs.firebasept.recyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.byphs.firebasept.Data.BrandData
import kotlinx.android.synthetic.main.best_brand_design.view.*

internal class HomeBrandViewHolder(v:View):RecyclerView.ViewHolder(v) {
    var view: View = v
    fun bind(brandDTO: BrandData, position: Int){
        //var f: File? = File(postDTO.imageUrl)
        //var f: File? = File("gs://jellyproject-3dfae.appspot.com/images/IMG_20210322_173503.jpg)
        //view.post_image.setImageURI(Uri.parse(postDTO.imageUrl))
        Glide.with(view).load(brandDTO.logoImage).fitCenter().into(view.brand_image_best_brand)// url로 불러올때 이거쓰자! 이게 좋다!! 글라이드 개꿀!!
        view.best_brand_name_text.text = brandDTO.brandName

    }
}