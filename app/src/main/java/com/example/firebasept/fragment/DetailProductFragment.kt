package com.example.firebasept.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.firebasept.Data.ProductData
import com.example.firebasept.Data.UserDTO
import com.example.firebasept.Model.BrandProductDataRepository
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Model.UserDataRepository
import com.example.firebasept.R
import com.example.firebasept.itemDeco.ItemDeco
import com.example.firebasept.recyclerView.PostInDetailProductViewAdapter
import com.example.firebasept.viewmodel.BrandProductViewModel
import com.example.firebasept.viewmodel.BrandProductViewModelFactory
import com.example.firebasept.viewmodel.UsStyleViewModel
import com.example.firebasept.viewmodel.UsStyleViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_detail_brand.*
import kotlinx.android.synthetic.main.fragment_detail_product.*
import java.text.DecimalFormat

class DetailProductFragment: Fragment() {
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var repository: PostDataRepository
    lateinit var repository2: UserDataRepository
    lateinit var repository3: BrandProductDataRepository
    lateinit var factory: BrandProductViewModelFactory
    lateinit var viewModel: BrandProductViewModel
    lateinit var factory2: UsStyleViewModelFactory
    lateinit var viewModel2: UsStyleViewModel
    lateinit var itemDeco: ItemDeco
    lateinit var adapter: PostInDetailProductViewAdapter
    var userDTO: UserDTO? = null
    var productData: ProductData? = null
    var recyclerViewFlag = false
    var productName = ""
    var brandName = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        repository = PostDataRepository(db)
        repository2 = UserDataRepository(db)
        repository3 = BrandProductDataRepository(db)
        factory = BrandProductViewModelFactory(repository,repository2,repository3)
        viewModel = ViewModelProvider(requireActivity(),factory).get(
            BrandProductViewModel::class.java)

        factory2 = UsStyleViewModelFactory(repository,repository2,repository3)
        viewModel2 = ViewModelProvider(requireActivity(),factory2).get(
            UsStyleViewModel::class.java)

        adapter = PostInDetailProductViewAdapter(viewModel,productName,brandName)

        viewModel.initU()
        viewModel.initP()
        viewModel.initPR()

        itemDeco = ItemDeco(requireContext())

        viewModel.productDataForDetail.observe(viewLifecycleOwner, Observer {
            productName = it.productName
            brandName = it.brandName
            adapter = PostInDetailProductViewAdapter(viewModel,productName,brandName)

            productData = it

            setView()
            setRecyclerView()


            if(!recyclerViewFlag){
                recyclerViewFlag =true

            }
            else{
                Log.d("반응왔냐???????????????","d왔음")
                adapter.notifyDataSetChanged()
            }

            go_to_website_product.setOnClickListener {
                //Log.d("사이트",productData!!.purchaseLink)
                /*webview_product.visibility = View.VISIBLE
                webview_product.loadUrl(productData!!.purchaseLink)*/
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(productData!!.purchaseLink)
                startActivity(i)
            }
        })

        viewModel.allPostData.observe(viewLifecycleOwner, Observer {
            setRecyclerView()
            adapter.notifyDataSetChanged()

        })

        viewModel.allUserData.observe(viewLifecycleOwner, Observer {
            userDTO = viewModel.getUser(auth.currentUser.uid)
        })

        viewModel.allProductData.observe(viewLifecycleOwner, Observer {
            productData = viewModel.getProduct(productData!!.productName,productData!!.brandName)
            setView()

        })






        detail_product_heart_button.setOnClickListener {
            viewModel.clickLikeProduct(productData!!.productName,productData!!.brandName)
            viewModel.clickLikeProductAdd(productData!!.productName,productData!!.brandName)
        }

        back_btn_detail_product.setOnClickListener {
            findNavController().navigateUp()
        }


        // 버튼 보였다 사라졌다
        var slowAppear: Animation
        var slowDisappear: Animation
        slowDisappear = AnimationUtils.loadAnimation(requireContext(),
            R.anim.fade_out
        );
        slowAppear = AnimationUtils.loadAnimation(requireContext(),
            R.anim.fade_in
        );

        //var animation:Animation =  AlphaAnimation(0, 1)
        //animation.setDuration(1000);

        detail_product_srcoll.setOnScrollChangeListener { view: View, i: Int, i1: Int, i2: Int, i3: Int ->
            if(detail_product_srcoll.scrollY == 0){
                if(go_to_website_product.visibility == View.GONE){
                    go_to_website_product.startAnimation(slowAppear)
                }
                go_to_website_product.visibility = View.VISIBLE
            }
            else{
                if(go_to_website_product.visibility == View.VISIBLE){
                    go_to_website_product.startAnimation(slowDisappear)
                }
                go_to_website_product.visibility = View.GONE
            }
        }

        search_btn_detail_brand.setOnClickListener {
            findNavController().navigate(R.id.searchBrandProductFragment)
        }

        back_btn_detail_product.setOnClickListener {
            findNavController().navigateUp()
        }

    }



    fun setView(){
        Glide.with(this).load(productData!!.imageUrl).centerCrop().into(product_image_detail_product)
        brand_name_text_detail_product.text = productData!!.brandName
        product_name_text_detail_product.text = productData!!.productName


        val dec: DecimalFormat = DecimalFormat("#,###")
        var ret:String = dec.format(productData!!.price)
        product_price_text_detail_product.text = ret

        detail_product_like_count.text = productData!!.likeCount.toString()
        if(productData!!.likes.containsKey(auth.currentUser.uid)){
            detail_product_heart_button.setImageResource(R.drawable.red_heart)
        }
        else{
            detail_product_heart_button.setImageResource(R.drawable.empty_heart)
        }
    }

    fun setRecyclerView(){
        grid_recyclerview_post_in_detail_product.adapter = adapter
        val gridLayoutManager = GridLayoutManager(activity, 2)
        grid_recyclerview_post_in_detail_product.layoutManager = gridLayoutManager
        if(itemDeco != null)
            grid_recyclerview_post_in_detail_product.removeItemDecoration(itemDeco)
        grid_recyclerview_post_in_detail_product.addItemDecoration(itemDeco)
        (grid_recyclerview_post_in_detail_product.adapter as PostInDetailProductViewAdapter).setItemClickListener(object:
            PostInDetailProductViewAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                //Log.d("클릭","됐어??")
                val PD = viewModel.getPostDataWithProductData(productName,brandName)!![position]
                viewModel2.postDataForPost.setValue(Pair(PD,position))
                findNavController().navigate(R.id.postFragment)
            }
        })
    }
}