package com.example.firebasept.recyclerView

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasept.Data.PostData
import com.example.firebasept.Data.ProductData
import kotlinx.android.synthetic.main.more_product_design.view.*
import kotlinx.android.synthetic.main.post_design.view.*
import kotlinx.android.synthetic.main.product_design.view.*
import kotlinx.android.synthetic.main.select_put_on_product_design.view.*
import java.text.DecimalFormat

class SearchPutOnProductViewHolder(v:View):RecyclerView.ViewHolder(v) {
    var view: View = v
    fun bind(productData: ProductData, position: Int, boolean: Boolean){

        //var f: File? = File(postDTO.imageUrl)
        //var f: File? = File("gs://jellyproject-3dfae.appspot.com/images/IMG_20210322_173503.jpg)
        //view.post_image.setImageURI(Uri.parse(postDTO.imageUrl))
        Glide.with(view).load(productData.imageUrl).into(view.select_put_on_product_image)// url로 불러올때 이거쓰자! 이게 좋다!! 글라이드 개꿀!!
        view.select_put_on_brand_name_text.text = productData.brandName
        view.select_put_on_product_name_text.text = productData.productName

        if(boolean){
            view.select_put_on_product_check_box.isChecked = true
            view.filter_put_on.visibility = View.VISIBLE
        }
        else{
            view.select_put_on_product_check_box.isChecked = false
            view.filter_put_on.visibility = View.GONE
        }
        val dec: DecimalFormat = DecimalFormat("#,###")
        var ret:String = dec.format(productData!!.price)
        view.select_put_on_product_price_text.text = ret
    }
}