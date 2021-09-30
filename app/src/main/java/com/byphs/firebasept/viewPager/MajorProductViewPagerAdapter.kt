package com.byphs.firebasept.viewPager

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.byphs.firebasept.R
import com.byphs.firebasept.recyclerView.HomeProductViewPagerHolder
import com.byphs.firebasept.viewmodel.BrandProductViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.major_product_design_for_view_pager2.view.*

internal class MajorProductViewPagerAdapter(private val viewModel: BrandProductViewModel,private val brandName:String):
    RecyclerView.Adapter<HomeProductViewPagerHolder>() {

    override fun getItemCount(): Int {
        var size = 0
        if(viewModel.getMajorProductData(brandName)!!.size > 9){
            size = viewModel.getMajorProductData(brandName)!!.subList(0,9).size / 3
        }
        else{
            if(viewModel.getMajorProductData(brandName)!!.size%3 == 0){
                size = viewModel.getMajorProductData(brandName)!!.size / 3
            }
            else size = viewModel.getMajorProductData(brandName)!!.size + 1

        }
        Log.d("사이즈",size.toString())
        return size
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeProductViewPagerHolder {
        val myLayout: Int =R.layout.major_product_design_for_view_pager2

        return HomeProductViewPagerHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: HomeProductViewPagerHolder, position: Int) {
        var auth = FirebaseAuth.getInstance()


        holder.bind(position,viewModel.getMajorProductData(brandName)!!)

        holder.view.first_item_layout.setOnClickListener {
            viewModel.setMajorProductCall(viewModel.getMajorProductData(brandName)!![position*3])
        }
        holder.view.second_item_layout.setOnClickListener {
            viewModel.setMajorProductCall(viewModel.getMajorProductData(brandName)!![position*3 + 1])
        }
        holder.view.third_item_layout.setOnClickListener {
            viewModel.setMajorProductCall(viewModel.getMajorProductData(brandName)!![position*3 + 2])
        }


    }

}