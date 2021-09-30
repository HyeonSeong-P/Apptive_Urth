package com.byphs.firebasept.recyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.write_post_photo_design.view.*

internal class WritePhotoViewHolder(v:View):RecyclerView.ViewHolder(v) {
    var view: View = v
    fun bind(uri: String, position: Int){
        var price:String = ""
        //var f: File? = File(postDTO.imageUrl)
        //var f: File? = File("gs://jellyproject-3dfae.appspot.com/images/IMG_20210322_173503.jpg)
        //view.post_image.setImageURI(Uri.parse(postDTO.imageUrl))
        Glide.with(view).load(uri).into(view.photo_image_write_post)// url로 불러올때 이거쓰자! 이게 좋다!! 글라이드 개꿀!!

    }
}