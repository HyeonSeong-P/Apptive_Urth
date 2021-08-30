package com.example.firebasept.recyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasept.Data.PostData
import kotlinx.android.synthetic.main.post_design.view.*

internal class MYPagePostViewHolder(v: View): RecyclerView.ViewHolder(v) {
    var view: View = v
    fun bind(postDTO: PostData, position: Int){
        //var f: File? = File(postDTO.imageUrl)
        //var f: File? = File("gs://jellyproject-3dfae.appspot.com/images/IMG_20210322_173503.jpg)
        //view.post_image.setImageURI(Uri.parse(postDTO.imageUrl))
        Glide.with(view).load(postDTO.imageUrl[0]).into(view.post_image)// url로 불러올때 이거쓰자! 이게 좋다!!
        view.post_user_name_text.setText(postDTO.nickname)
    }
}