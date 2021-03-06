package com.byphs.firebasept.recyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.byphs.firebasept.Data.ProductData
import kotlinx.android.synthetic.main.product_design.view.*
import java.text.DecimalFormat

internal class MyPageProductViewHolder(v:View):RecyclerView.ViewHolder(v) {
    var view: View = v
    fun bind(productData: ProductData, position: Int){
        view.heart_button_product.visibility = View.GONE

        //var f: File? = File(postDTO.imageUrl)
        //var f: File? = File("gs://jellyproject-3dfae.appspot.com/images/IMG_20210322_173503.jpg)
        //view.post_image.setImageURI(Uri.parse(postDTO.imageUrl))
        Glide.with(view).load(productData.imageUrl).into(view.product_image)// url로 불러올때 이거쓰자! 이게 좋다!! 글라이드 개꿀!!
        view.brand_name_text_product.text = productData.brandName
        view.product_name_text.text = productData.productName
        val dec: DecimalFormat = DecimalFormat("#,###")
        var ret:String = dec.format(productData!!.price)
        view.product_price_text.text = ret
    }
}