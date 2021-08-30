package com.example.firebasept.recyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.firebasept.Data.BrandData
import com.example.firebasept.Data.CampaignData
import com.example.firebasept.Data.PostData
import kotlinx.android.synthetic.main.brand_design.view.*
import kotlinx.android.synthetic.main.post_design.view.*
import kotlinx.android.synthetic.main.urth_news_design.view.*

internal class CampaignViewHolder(v:View):RecyclerView.ViewHolder(v) {
    var view: View = v
    fun bind(campaignData: CampaignData, position: Int){
        //var f: File? = File(postDTO.imageUrl)
        //var f: File? = File("gs://jellyproject-3dfae.appspot.com/images/IMG_20210322_173503.jpg)
        //view.post_image.setImageURI(Uri.parse(postDTO.imageUrl))
        Glide.with(view).load(campaignData.imgUrl).centerCrop().into(view.urth_news_image)// url로 불러올때 이거쓰자! 이게 좋다!! 글라이드 개꿀!!
        view.urth_news_brand_text.text = campaignData.brandName
        view.urth_news_description_text.text = campaignData.description
    }
}