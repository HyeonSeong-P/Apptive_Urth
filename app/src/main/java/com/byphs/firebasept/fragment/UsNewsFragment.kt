package com.byphs.firebasept.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.byphs.firebasept.Data.UserDTO
import com.byphs.firebasept.Model.BrandProductDataRepository
import com.byphs.firebasept.Model.PostDataRepository
import com.byphs.firebasept.Model.UserDataRepository
import com.byphs.firebasept.R
import com.byphs.firebasept.itemDeco.UrthNewsItemDeco
import com.byphs.firebasept.recyclerView.*
import com.byphs.firebasept.viewmodel.BrandProductViewModel
import com.byphs.firebasept.viewmodel.BrandProductViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_us_news.*

internal class UsNewsFragment: Fragment() {
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var repository: PostDataRepository
    lateinit var repository2: UserDataRepository
    lateinit var repository3: BrandProductDataRepository
    lateinit var factory: BrandProductViewModelFactory
    lateinit var viewModel: BrandProductViewModel
    var userDTO: UserDTO? = null
    var recyclerViewFlag = false
    lateinit var adapter: CampaignViewAdapter
    lateinit var itemDeco: UrthNewsItemDeco
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_us_news, container, false)
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
        itemDeco =
            UrthNewsItemDeco(requireContext())
        adapter = CampaignViewAdapter(viewModel) // 이부분이 중요 밑에 갱신까지 이어짐, 아근데 이부분 자세히 공부해야할듯 발표끝나고 보자

        viewModel.initC()

        viewModel.allCampaignData.observe(viewLifecycleOwner, Observer {
            setRecyclerView()
        })

        /*viewModel.allCampaignData.observe(viewLifecycleOwner, Observer {
            if(!recyclerViewFlag){
                recyclerViewFlag = true
                setRecyclerView()
            }
            else{
                adapter.notifyDataSetChanged()
            }
        })*/

        viewModel.campaignWebSite.observe(viewLifecycleOwner, Observer {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(it!!)
            startActivity(i)
        })
    }

    fun setRecyclerView(){
        urth_news_recycler_view.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(activity)
        urth_news_recycler_view.layoutManager = linearLayoutManager
        if(itemDeco != null)
            urth_news_recycler_view.removeItemDecoration(itemDeco)
        urth_news_recycler_view.addItemDecoration(itemDeco)
        (urth_news_recycler_view.adapter as CampaignViewAdapter).setItemClickListener(object:
            CampaignViewAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {

            }
        })
    }
}