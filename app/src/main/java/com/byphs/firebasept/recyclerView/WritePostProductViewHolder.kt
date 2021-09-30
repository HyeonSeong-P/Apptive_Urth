package com.byphs.firebasept.recyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.byphs.firebasept.Data.ProductData
import kotlinx.android.synthetic.main.write_post_product_design.view.*

internal class WritePostProductViewHolder(v:View):RecyclerView.ViewHolder(v) {
    var view: View = v
    fun bind(productData: ProductData, position: Int){
        var price:String = ""
        //var f: File? = File(postDTO.imageUrl)
        //var f: File? = File("gs://jellyproject-3dfae.appspot.com/images/IMG_20210322_173503.jpg)
        //view.post_image.setImageURI(Uri.parse(postDTO.imageUrl))
        Glide.with(view).load(productData.imageUrl).into(view.product_image_write_post)// url로 불러올때 이거쓰자! 이게 좋다!! 글라이드 개꿀!!

    }
}