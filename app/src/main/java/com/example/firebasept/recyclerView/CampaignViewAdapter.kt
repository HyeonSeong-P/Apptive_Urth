package com.example.firebasept.recyclerView


import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasept.R
import com.example.firebasept.viewmodel.BrandProductViewModel
import com.example.firebasept.viewmodel.UsStyleViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.brand_design.view.*
import kotlinx.android.synthetic.main.post_design.view.*
import kotlinx.android.synthetic.main.product_design.view.*
import kotlinx.android.synthetic.main.urth_news_design.view.*

class CampaignViewAdapter(private val viewModel: BrandProductViewModel):RecyclerView.Adapter<CampaignViewHolder>() {

    override fun getItemCount(): Int {
        return viewModel.getCampaignData()!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampaignViewHolder {
        val myLayout:Int = R.layout.urth_news_design
        return CampaignViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: CampaignViewHolder, position: Int) {
        var auth = FirebaseAuth.getInstance()
        val campaign = viewModel.getCampaignData()!![position]
        holder.bind(campaign,position)
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        holder.view.go_to_campaign_btn.setOnClickListener {
            viewModel.setCampaignWebSite(campaign.link)
        }


    }

    //    ClickListener
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

}