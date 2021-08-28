package com.example.firebasept.viewPager

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasept.Data.ProductData
import com.example.firebasept.R
import com.example.firebasept.recyclerView.BrandViewHolder
import com.example.firebasept.recyclerView.HomeProductViewHolder
import com.example.firebasept.recyclerView.HomeProductViewPagerHolder
import com.example.firebasept.viewmodel.BrandProductViewModel
import com.example.firebasept.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.major_product_design_for_view_pager2.view.*

class HomeProductViewPagerAdapter(private val viewModel: HomeViewModel):
    RecyclerView.Adapter<HomeProductViewPagerHolder>() {

    private var list = viewModel.getNewProductData()!!
    override fun getItemCount(): Int {
        var size = 0
        if(viewModel.getNewProductData()!!.size > 9){
            size = viewModel.getNewProductData()!!.subList(0,9).size / 3
        }
        else{
            if(viewModel.getNewProductData()!!.size%3 == 0){
                size = viewModel.getNewProductData()!!.size / 3
            }
            else size = viewModel.getNewProductData()!!.size + 1

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


        holder.bind(position,viewModel.getNewProductData()!!)

        holder.view.first_item_layout.setOnClickListener {
            viewModel.setMoreNewOrNormalCall(false,viewModel.getNewProductData()!![position*3])
        }
        holder.view.second_item_layout.setOnClickListener {
            viewModel.setMoreNewOrNormalCall(false,viewModel.getNewProductData()!![position*3 + 1])
        }
        holder.view.third_item_layout.setOnClickListener {
            if(position == 2 && viewModel.getNewProductData()!!.size > 9){
                viewModel.setMoreNewOrNormalCall(true,viewModel.getNewProductData()!![position*3 + 2])
            }
            else{
                viewModel.setMoreNewOrNormalCall(false,viewModel.getNewProductData()!![position*3 + 2])
            }
        }


    }

}