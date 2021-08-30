package com.example.firebasept.recyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasept.R
import com.example.firebasept.viewmodel.BrandProductViewModel
import com.example.firebasept.viewmodel.UsStyleViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.post_design.view.*
import kotlinx.android.synthetic.main.product_design.view.*
import kotlinx.android.synthetic.main.select_put_on_product_design.view.*

internal class SearchPutOnProductViewAdapter(private val viewModel: UsStyleViewModel):RecyclerView.Adapter<SearchPutOnProductViewHolder>() {

    companion object{
        //체크박스를 유지하기 위한 방법쓰
        private var checkMap = hashMapOf<String,String>()

       /* fun checkItem(productName:String){
            checkMap.put(productName,true)
        }

        fun uncheckItem(productName:String){
            checkMap.remove(productName)
        }

        fun getCheckMapKeysToList(): List<String> {
            return checkMap.keys.toList()
        }

        fun clearCheckMap(){
            checkMap.clear()
        }*/
    }
    private fun checkItem(productName:String, brandName:String){
        checkMap.put(productName,brandName)
    }

    private fun uncheckItem(productName:String){
        checkMap.remove(productName)
    }

    fun getCheckMapToList(): List<Pair<String, String>> {
        return checkMap.toList()
    }

    fun clearCheckMap(){
        checkMap.clear()
    }
    override fun getItemCount(): Int {
        Log.d("어댑터 아이템 개수",viewModel.getSearchPutOnProductData()!!.size.toString())
        return viewModel.getSearchPutOnProductData()!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPutOnProductViewHolder {
        val myLayout:Int = R.layout.select_put_on_product_design
        return SearchPutOnProductViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    override fun onBindViewHolder(holder: SearchPutOnProductViewHolder, position: Int) {
        var auth = FirebaseAuth.getInstance()
        val product = viewModel.getSearchPutOnProductData()!![position]
        holder.itemView.setOnClickListener {
            if(checkMap.containsKey(product.productName)){
                uncheckItem(product.productName)
            }
            else{
                checkItem(product.productName,product.brandName)
            }

            itemClickListener.onClick(it, position)
        }
        holder.bind(product,position,checkMap.containsKey(product.productName))

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