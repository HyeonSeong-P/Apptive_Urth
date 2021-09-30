package com.byphs.firebasept.recyclerView

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.byphs.firebasept.Data.ProductData
import kotlinx.android.synthetic.main.major_product_design.view.*

import kotlinx.android.synthetic.main.more_product_design.view.*
import java.text.DecimalFormat

internal class HomeProductViewHolder(v:View):RecyclerView.ViewHolder(v) {
    var view: View = v

    fun bind(productData: ProductData, position: Int, newProductSize:Int){
        if(position == 8 && newProductSize > 9){
            Glide.with(view).load(productData.imageUrl).into(view.more_product_image)
            view.more_product_image.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)

            view.more_product_number_text.text = "+" + (newProductSize - 9).toString()
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