package com.example.firebasept.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasept.Data.UserDTO
import com.example.firebasept.Model.BrandProductDataRepository
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Model.UserDataRepository
import com.example.firebasept.R
import com.example.firebasept.itemDeco.ItemDeco
import com.example.firebasept.recyclerView.ProductViewAdapter
import com.example.firebasept.viewmodel.BrandProductViewModel
import com.example.firebasept.viewmodel.BrandProductViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_product.*

internal class ProductFragment:Fragment() {
    val categoryList = arrayListOf<String>("#아우터","#상의","#하의","#원피스","#신발","#가방","#기타")
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var repository: PostDataRepository
    lateinit var repository2: UserDataRepository
    lateinit var repository3: BrandProductDataRepository
    lateinit var factory: BrandProductViewModelFactory
    lateinit var viewModel: BrandProductViewModel
    var userDTO: UserDTO? = null
    var recyclerViewFlag = false
    var page = 0
    lateinit var adapter: ProductViewAdapter
    lateinit var itemDeco: ItemDeco

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product, container, false)
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

        viewModel.initU()
        viewModel.initPR()

        itemDeco = ItemDeco(requireContext())
        adapter = ProductViewAdapter(viewModel) // 이부분이 중요 밑에 갱신까지 이어짐, 아근데 이부분 자세히 공부해야할듯 발표끝나고 보자

        buttonBehavior()

        viewModel.allProductData.observe(viewLifecycleOwner, Observer {
            setRecyclerView()

            if(!recyclerViewFlag){
                recyclerViewFlag =true
                viewModel.initSortDataProduct()
            }
            else{
                Log.d("반응왔냐???????????????","d왔음")
                adapter.notifyDataSetChanged()
                grid_recyclerview_product.scrollToPosition(viewModel.changedProductPosition.value?:0)
            }
        })
        viewModel.allUserData.observe(viewLifecycleOwner, Observer {
            userDTO = viewModel.getUser(auth.currentUser.uid)

        })
        viewModel.productTagCategory.observe(viewLifecycleOwner, Observer {
            //my_page_user_nickname.text = userDTO!!.nickName
            Log.d("반응","반응옴?")
            var clickTagNum = 0
            for((i, tagString) in categoryList.withIndex()){
                if(it!!.containsKey(tagString)){
                    setTagButtonDesign(i,true)
                    clickTagNum++
                }
                else{
                    Log.d("반응","없나??")
                    setTagButtonDesign(i,false)
                }
            }
            if(clickTagNum == 7) setAllTagButtonDesign(true)
            else setAllTagButtonDesign(false)
            if(recyclerViewFlag){
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.sortDataProduct.observe(viewLifecycleOwner, Observer {
            if(recyclerViewFlag){
                when(it){
                    3 -> sort_name_text_product.text = "낮은가격순"
                    2 -> sort_name_text_product.text = "높은가격순"
                    1 -> sort_name_text_product.text = "최신순"
                    else -> sort_name_text_product.text = "인기순"
                }
                adapter.notifyDataSetChanged()
            }
        })

        /*viewModel.productPageNum.observe(viewLifecycleOwner, Observer {
            adapter.notifyItemRangeInserted(it * 10,10)
        })*/
    }


    private fun setAllTagButtonDesign(flag: Boolean){
        //("#ALL","#아우터","#상의","#하의","#원피스","#신발","#가방","#기타")
        if(flag){
            all_tag_btn_in_product.setTextColor(Color.parseColor("#faf8f7"))
            all_tag_btn_in_product.setBackgroundResource(R.drawable.selected_circle_image)

            outer_tag_btn_in_product.setTextColor(Color.parseColor("#3e3a39"))
            outer_tag_btn_in_product.setBackgroundResource(R.drawable.circle_image)

            top_tag_btn_in_product.setTextColor(Color.parseColor("#3e3a39"))
            top_tag_btn_in_product.setBackgroundResource(R.drawable.circle_image)

            bottom_tag_btn_in_product.setTextColor(Color.parseColor("#3e3a39"))
            bottom_tag_btn_in_product.setBackgroundResource(R.drawable.circle_image)

            one_piece_tag_btn_in_product.setTextColor(Color.parseColor("#3e3a39"))
            one_piece_tag_btn_in_product.setBackgroundResource(R.drawable.circle_image)

            shoes_tag_btn_in_product.setTextColor(Color.parseColor("#3e3a39"))
            shoes_tag_btn_in_product.setBackgroundResource(R.drawable.circle_image)

            bag_tag_btn_in_product.setTextColor(Color.parseColor("#3e3a39"))
            bag_tag_btn_in_product.setBackgroundResource(R.drawable.circle_image)

            ex_tag_btn_in_product.setTextColor(Color.parseColor("#3e3a39"))
            ex_tag_btn_in_product.setBackgroundResource(R.drawable.circle_image)
        }
        else{
            all_tag_btn_in_product.setTextColor(Color.parseColor("#3e3a39"))
            all_tag_btn_in_product.setBackgroundResource(R.drawable.circle_image)
        }

    }
    private fun setTagButtonDesign(tagPosition:Int,flag:Boolean){
        when(tagPosition){
            1 -> {
                if(!flag){
                    top_tag_btn_in_product.setTextColor(Color.parseColor("#3e3a39"))
                    top_tag_btn_in_product.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    top_tag_btn_in_product.setTextColor(Color.parseColor("#faf8f7"))
                    top_tag_btn_in_product.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }
            2 -> {
                if(!flag){
                    bottom_tag_btn_in_product.setTextColor(Color.parseColor("#3e3a39"))
                    bottom_tag_btn_in_product.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    bottom_tag_btn_in_product.setTextColor(Color.parseColor("#faf8f7"))
                    bottom_tag_btn_in_product.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }
            3 -> {
                if(!flag){
                    one_piece_tag_btn_in_product.setTextColor(Color.parseColor("#3e3a39"))
                    one_piece_tag_btn_in_product.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    one_piece_tag_btn_in_product.setTextColor(Color.parseColor("#faf8f7"))
                    one_piece_tag_btn_in_product.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }
            4 -> {
                if(!flag){
                    shoes_tag_btn_in_product.setTextColor(Color.parseColor("#3e3a39"))
                    shoes_tag_btn_in_product.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    shoes_tag_btn_in_product.setTextColor(Color.parseColor("#faf8f7"))
                    shoes_tag_btn_in_product.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }

            5 -> {
                if(!flag){
                    bag_tag_btn_in_product.setTextColor(Color.parseColor("#3e3a39"))
                    bag_tag_btn_in_product.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    bag_tag_btn_in_product.setTextColor(Color.parseColor("#faf8f7"))
                    bag_tag_btn_in_product.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }

            6 -> {
                if(!flag){
                    ex_tag_btn_in_product.setTextColor(Color.parseColor("#3e3a39"))
                    ex_tag_btn_in_product.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    ex_tag_btn_in_product.setTextColor(Color.parseColor("#faf8f7"))
                    ex_tag_btn_in_product.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }


            else -> {
                if(!flag){
                    outer_tag_btn_in_product.setTextColor(Color.parseColor("#3e3a39"))
                    outer_tag_btn_in_product.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    outer_tag_btn_in_product.setTextColor(Color.parseColor("#faf8f7"))
                    outer_tag_btn_in_product.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }
        }
    }

    private fun buttonBehavior(){

        sort_btn_product.setOnClickListener{
            val bottomSheet = BottomSheetProduct(sort_name_text_product.text.toString())
            bottomSheet.show(childFragmentManager,bottomSheet.tag)
        }

        // 태그 포지션
        //("#ALL","#아우터","#상의","#하의","#원피스","#신발","#가방","#기타")
        all_tag_btn_in_product.setOnClickListener {
            viewModel.setProductAllCategory()
        }
        outer_tag_btn_in_product.setOnClickListener {
            viewModel.setProductCategory("#아우터")
        }
        top_tag_btn_in_product.setOnClickListener {
            viewModel.setProductCategory("#상의")
        }
        bottom_tag_btn_in_product.setOnClickListener {
            viewModel.setProductCategory("#하의")
        }
        one_piece_tag_btn_in_product.setOnClickListener {
            viewModel.setProductCategory("#원피스")
        }
        shoes_tag_btn_in_product.setOnClickListener {
            viewModel.setProductCategory("#신발")
        }
        bag_tag_btn_in_product.setOnClickListener {
            viewModel.setProductCategory("#가방")
        }
        ex_tag_btn_in_product.setOnClickListener {
            viewModel.setProductCategory("#기타")
        }
    }

    fun setRecyclerView(){
        //콜백 안써도 그냥 라이브데이터 감지로 해도 되네?.. 로직은 비슷하긴 하니까 뭐..
        grid_recyclerview_product.adapter = adapter
        val gridLayoutManager = GridLayoutManager(activity, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                // 카페고리 header 나 footer 영역은 grid 4칸을 전부 차지하여 표시
                if (viewModel.getSubProductData(page)!![position].productName == ""
                    && viewModel.getSubProductData(page)!![position].brandName == "") {
                    return gridLayoutManager.spanCount
                }
                return 1 // 나머지는 1칸만 사용
            }
        }

        grid_recyclerview_product.layoutManager = gridLayoutManager

        if(itemDeco != null)
            grid_recyclerview_product.removeItemDecoration(itemDeco)
        grid_recyclerview_product.addItemDecoration(itemDeco)
        (grid_recyclerview_product.adapter as ProductViewAdapter).setItemClickListener(object:
            ProductViewAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                //Log.d("클릭","됐어??")
                val PRD = viewModel.getProductData()!![position]
                viewModel.setProductDataForDetail(PRD)
                findNavController().navigate(R.id.detailProductFragment)
            }
        })


        // 스크롤 리스너

        grid_recyclerview_product.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as GridLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount-1

                // 스크롤이 끝에 도달했는지 확인
                if (!grid_recyclerview_product.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount) {
                    // 페이지 수를 어댑터의 멤버변수로 둬서 뷰모델의 상품데이터 함수의 인수로 전해줘도 되고 뷰모델 라이브데이터로 해서 뷰모델 상품데이터 함수안에서 바로 써도 된다
                    // 뷰모델의 경우 위에 옵저버와 밑에 코드를 통해 사용
                    //viewModel.setProductPageNum(++page)
                    adapter.setPageNum(++page)
                    if(page*10 + 10 < viewModel.getProductData().size){
                        adapter.notifyItemRangeInserted(page * 10,10)
                    }
                }
            }
        })
    }
}