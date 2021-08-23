package com.example.firebasept.recyclerView

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasept.Data.PostData
import com.example.firebasept.Data.ProductData
import com.example.firebasept.R
import kotlinx.android.synthetic.main.major_product_design.view.*
import kotlinx.android.synthetic.main.more_new_product_brand_name_design.view.*

import kotlinx.android.synthetic.main.more_product_design.view.*
import kotlinx.android.synthetic.main.post_design.view.*
import kotlinx.android.synthetic.main.product_design.view.*
import java.text.DecimalFormat

class MoreNewProductViewHolder(v:View):RecyclerView.ViewHolder(v) {
    var view: View = v

    fun bind(productData: ProductData, position: Int){
        if(productData.productName == ""){
            if(position == 0){
                view.more_new_product_brand_line.visibility = View.GONE
                view.more_new_product_empty_partition.visibility = View.GONE
            }
            else{
                view.more_new_product_brand_line.visibility = View.VISIBLE
                view.more_new_product_empty_partition.visibility = View.VISIBLE
            }
            view.more_new_product_brand_name_text.text = productData.brandName
        }
        else{

            Glide.with(view).load(productData.imageUrl).into(view.major_product_image)// url로 불러올때 이거쓰자! 이게 좋다!! 글라이드 개꿀!!
            view.major_product_name_text.text = productData.productName

            val dec: DecimalFormat = DecimalFormat("#,###")
            var ret:String = dec.format(productData!!.price)
            view.major_product_price_text.text = ret
        }

    }
}