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
import kotlinx.android.synthetic.main.major_product_design_for_view_pager2.view.*

import kotlinx.android.synthetic.main.more_product_design.view.*
import kotlinx.android.synthetic.main.more_product_design.view.more_product_number_text
import kotlinx.android.synthetic.main.post_design.view.*
import kotlinx.android.synthetic.main.product_design.view.*
import java.text.DecimalFormat

internal class HomeProductViewPagerHolder(v:View):RecyclerView.ViewHolder(v) {
    var view: View = v

    fun bind(position: Int, list:List<ProductData>){
        if(position == 2 && list.size > 9){
            Glide.with(view).load(list[position*3].imageUrl).into(view.major_product_image1)// url로 불러올때 이거쓰자! 이게 좋다!! 글라이드 개꿀!!
            view.major_product_name_text1.text = list[position*3].productName
            val dec1: DecimalFormat = DecimalFormat("#,###")
            var ret1:String = dec1.format(list[position*3].price)
            view.major_product_price_text1.text = ret1

            Glide.with(view).load(list[position*3 + 1].imageUrl).into(view.major_product_image2)// url로 불러올때 이거쓰자! 이게 좋다!! 글라이드 개꿀!!
            view.major_product_name_text2.text = list[position*3 + 1].productName
            val dec2: DecimalFormat = DecimalFormat("#,###")
            var ret2:String = dec2.format(list[position*3 + 1]!!.price)
            view.major_product_price_text2.text = ret2

            Glide.with(view).load(list[position*3 + 2].imageUrl).into(view.major_product_image3)
            view.major_product_image3.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
            view.more_product_number_text3.visibility = View.VISIBLE
            view.more_product_number_text3.text = "+" + (list.size - 9).toString()
            view.major_product_price_text3.visibility = View.GONE
            view.major_product_name_text3.visibility = View.GONE
        }
        else{

            Glide.with(view).load(list[position*3].imageUrl).into(view.major_product_image1)// url로 불러올때 이거쓰자! 이게 좋다!! 글라이드 개꿀!!
            view.major_product_name_text1.text = list[position*3].productName
            val dec1: DecimalFormat = DecimalFormat("#,###")
            var ret1:String = dec1.format(list[position*3].price)
            view.major_product_price_text1.text = ret1

            Glide.with(view).load(list[position*3 + 1].imageUrl).into(view.major_product_image2)// url로 불러올때 이거쓰자! 이게 좋다!! 글라이드 개꿀!!
            view.major_product_name_text2.text = list[position*3 + 1].productName
            val dec2: DecimalFormat = DecimalFormat("#,###")
            var ret2:String = dec2.format(list[position*3 + 1]!!.price)
            view.major_product_price_text2.text = ret2

            Glide.with(view).load(list[position*3 + 2].imageUrl).into(view.major_product_image3)// url로 불러올때 이거쓰자! 이게 좋다!! 글라이드 개꿀!!
            view.major_product_name_text3.text = list[position*3 + 2].productName
            val dec3: DecimalFormat = DecimalFormat("#,###")
            var ret3:String = dec3.format(list[position*3 + 2]!!.price)
            view.major_product_price_text3.text = ret3
        }

    }
}