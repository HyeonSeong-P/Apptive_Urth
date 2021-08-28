package com.example.firebasept.fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasept.Data.PostData
import com.example.firebasept.Data.ProductData
import com.example.firebasept.Data.UserDTO
import com.example.firebasept.Model.BrandProductDataRepository
import com.example.firebasept.Model.PostDataRepository
import com.example.firebasept.Model.UserDataRepository
import com.example.firebasept.R
import com.example.firebasept.itemDeco.HorizontalItemDeco
import com.example.firebasept.recyclerView.WritePhotoViewAdapter
import com.example.firebasept.recyclerView.WritePostProductViewAdapter
import com.example.firebasept.viewmodel.UsStyleViewModel
import com.example.firebasept.viewmodel.UsStyleViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_write_post.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class WritePostFragment: Fragment() {
    private val tagList = arrayListOf<String>("#비건소재","#사회공헌/기부","#업사이클링","#친환경소재","#동물복지")
    private var tagButtonState = arrayListOf<Boolean>(false,false,false,false,false)
    private var postTags = mutableListOf<String>()
    val GALLERY_CODE = 199
    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    lateinit var  db:FirebaseFirestore
    lateinit var stor:FirebaseStorage
    lateinit var imagePath:String
    lateinit var repository: PostDataRepository
    lateinit var repository2:UserDataRepository
    lateinit var repository3: BrandProductDataRepository
    lateinit var factory: UsStyleViewModelFactory
    lateinit var viewModel: UsStyleViewModel
    lateinit var currUser:UserDTO
    lateinit var itemDeco: HorizontalItemDeco
    lateinit var adapter1:WritePhotoViewAdapter
    lateinit var adapter2:WritePostProductViewAdapter
    var uriList = mutableListOf<Uri>()
    var productList = mutableListOf<ProductData>()
    var imagePathList = mutableListOf<String>()
    var editImagePathList = mutableListOf<String>()
    var h:List<String> = imagePathList + editImagePathList
    var firstImagePathListSize = 0
    var newPhotoFlag = false
    var postData:PostData? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_write_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //stor = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        repository = PostDataRepository(db)
        repository2 = UserDataRepository(db)
        repository3 = BrandProductDataRepository(db)
        //db = FirebaseFirestore.getInstance()
        factory = UsStyleViewModelFactory(repository,repository2,repository3)
        viewModel = ViewModelProvider(requireActivity(),factory).get(
            UsStyleViewModel::class.java)
        viewModel.initU()
        itemDeco =
            HorizontalItemDeco(requireContext())
        adapter1 = WritePhotoViewAdapter(imagePathList)
        adapter2 = WritePostProductViewAdapter(productList)

        setButton()
        viewModel.postEditCallToWrite.observe(viewLifecycleOwner, Observer {
            postData = it
            upload_btn.visibility = View.GONE
            edit_post_btn_write_post.visibility = View.VISIBLE
            imagePathList = it!!.imageUrl
            firstImagePathListSize = it!!.imageUrl.size
            productList = it!!.putOnProductList
            title_edit_text.setText(it!!.title)
            description_edit_text.setText(it!!.description)
            adapter1 = WritePhotoViewAdapter(imagePathList)
            adapter2 = WritePostProductViewAdapter(productList)
            setPhotoRecyclerview()
            setPutOnProductRecyclerview()
/*            adapter1.notifyDataSetChanged()
            adapter2.notifyDataSetChanged()*/
            for((i,tag) in tagList.withIndex()){
                if(it!!.tagKeys.contains(tag)){
                    postTags.add(tag)
                    setTagButtonDesign(i,true)
                    tagButtonState[i] = true
                }
                if(postTags.size == 5) setAllTagButtonDesign(true)
                else setAllTagButtonDesign(false)
            }

        })
        viewModel.allUserData.observe(viewLifecycleOwner, Observer {
            currUser = viewModel.getUser(auth.currentUser.uid)!!
            Log.d("사진","asdasdasd")
            upload_btn.setOnClickListener {
                /*Log.d("사진",imagePathList.size.toString())
                for(image1 in imagePathList){
                    Log.d("사진",image1)
                }*/
                upload(imagePathList)
            }
            edit_post_btn_write_post.setOnClickListener {
                editPost(imagePathList)
            }
        })

        viewModel.put_on_product_data_list.observe(viewLifecycleOwner, Observer {
            Log.d("착용","???")
            for(pair in it!!){
                var pd = viewModel.getProduct(pair.first,pair.second)
                Log.d("착용",pair.first)
                productList.add(pd!!)
            }
            adapter2 = WritePostProductViewAdapter(productList)
            setPutOnProductRecyclerview()
        })

        gallery_btn.setOnClickListener {
            stor = Firebase.storage
            var intent = Intent(Intent.ACTION_PICK)
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            //startActivityForResult(Intent.createChooser(intent, "Select Picture"),  100)
            startActivityForResult(Intent.createChooser(intent,"사진은 9장까지 업로드 가능합니다."),  100)

        }

        write_post_search_btn_put_on.setOnClickListener{
            val bottomSheet = SearchPutOnBottomSheet()
            bottomSheet.show(childFragmentManager,bottomSheet.tag)
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 100){
            //imagePathList.clear() // 초기화
            Log.d("ㅇㅇㅇ",data!!.data.toString())
            val clipData = data.clipData // 클립 데이터 지원 안하는 기기는 null 뜸
            if(data.clipData == null){
                uriList.add(data!!.data!!)
                imagePath = getPath(data!!.data!!)
                imagePathList.add(imagePath)
                Log.d("링크ㅇ",imagePath)
                //imageView.setImageURI(data!!.data)
            }
            else{
                when {
                    clipData!!.itemCount > 5 -> {
                        Toast.makeText(requireContext(),"사진은 5장까지 선택 가능합니다.", Toast.LENGTH_SHORT).show()
                    }
                    clipData!!.itemCount == 1 -> {
                        uriList.add(clipData.getItemAt(0).uri)
                        imagePath = getPath(clipData.getItemAt(0).uri)
                        var f:File? = File(imagePath)
                        imagePathList.add(imagePath)
                        // 리사이클러뷰 노티파이
                    }
                    else -> {
                        for(i in 0 until clipData!!.itemCount){
                            Log.d("i:",i.toString())
                            uriList.add(clipData.getItemAt(i).uri)
                            imagePathList.add(getPath(clipData.getItemAt(i).uri))
                            // 리사이클러뷰 노티파이
                        }
                    }
                }
            }
            adapter1 = WritePhotoViewAdapter(imagePathList)
            setPhotoRecyclerview()
        }
    }
    fun getPath(uri: Uri):String{
        var proj:Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var cursorLoader = CursorLoader(requireContext(),uri,proj,null,null,null)
        var cursor = cursorLoader.loadInBackground()
        //var c = getContentResolver().query
        var index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()

        return cursor!!.getString(index!!)
    }
    fun upload(uriList:MutableList<String>) {
        var downloadUrlList: MutableList<String> = mutableListOf()
        for ((i, uri) in uriList.withIndex()) {
            var downloadUrl1: Uri?
            // 파이어스토리지에 사진 올리기
            // Create a storage reference from our app
            val storageRef = stor.reference
            var file = Uri.fromFile(File(uri))
            val riversRef = storageRef.child("images/${file.lastPathSegment}")
            var uploadTask = riversRef.putFile(file)


            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener {
                //파이어스토리지로 사진 올렸다면 그 url정보를 토대로 파이어스토어로 데이터 올리기기

                var postDTO: PostData =
                    PostData()
                riversRef.downloadUrl.addOnSuccessListener { it1 ->
                    downloadUrl1 = it1
                    if (i == uriList.size - 1) {
                        downloadUrlList.add(downloadUrl1.toString())
                        var postDTO: PostData =
                            PostData()
                        postDTO.imageUrl = downloadUrlList
                        postDTO.title = title_edit_text.text.toString()
                        postDTO.description = description_edit_text.text.toString()
                        postDTO.uid = auth.currentUser.uid
                        postDTO.nickname = currUser.nickName
                        postDTO.tagKeys = postTags
                        postDTO.putOnProductList = productList
                        val now: Long = System.currentTimeMillis()
                        val mDate = Date(now)
                        val simpleDate = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                        val getTime: String = simpleDate.format(mDate)
                        postDTO.timestamp = getTime

                        db.collection("post")
                            .add(postDTO)
                            .addOnSuccessListener { documentReference ->
                                Log.d(
                                    TAG,
                                    "DocumentSnapshot added with ID: ${documentReference.id}"
                                )
                                Toast.makeText(requireContext(),"게시글 등록이 완료됐습니다",Toast.LENGTH_SHORT).show()
                                findNavController().navigateUp()
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                            }
                    }
                    else {
                        downloadUrlList.add(downloadUrl1.toString())
                    }

                    Log.d("성공2?", downloadUrl1.toString())

                }
                //db.reference.child("post").push().setValue(postDTO)
            }
        }
    }

    fun editPost(uriList:MutableList<String>) {
        var newPhotoFlag = false
        for(s in uriList){
            if(!s.contains("https")){
                newPhotoFlag = true
            }
        }

        var downloadUrlList: MutableList<String> = uriList.toMutableList()

        if(newPhotoFlag){
            for ((i, uri) in uriList.withIndex()) {
                if(!uri.contains("https")){
                    var downloadUrl1: Uri?
                    // 파이어스토리지에 사진 올리기
                    // Create a storage reference from our app
                    val storageRef = stor.reference
                    var file = Uri.fromFile(File(uri))
                    val riversRef = storageRef.child("images/${file.lastPathSegment}")
                    var uploadTask = riversRef.putFile(file)


                    // Register observers to listen for when the download is done or if it fails
                    uploadTask.addOnFailureListener {
                        // Handle unsuccessful uploads
                    }.addOnSuccessListener {
                        //파이어스토리지로 사진 올렸다면 그 url정보를 토대로 파이어스토어로 데이터 올리기기
                        riversRef.downloadUrl.addOnSuccessListener { it1 ->
                            downloadUrl1 = it1
                            if (i == uriList.size - 1) {
                                downloadUrlList.add(i,downloadUrl1.toString())
                                downloadUrlList.remove(uri)
                                var postCollectionRef: DocumentReference
                                db.collection("post").get()
                                    .addOnSuccessListener { collection ->
                                        if(collection != null){
                                            for (doc in collection!!) {
                                                var post = doc.toObject(PostData::class.java)
                                                if(post.uid == postData!!.uid && post.timestamp == postData!!.timestamp){
                                                    postCollectionRef = doc.reference // 스냅샷으로부터 리퍼런스 가져오기
                                                    db.runTransaction { transaction ->
                                                        //val snapshot = transaction.get(postCollectionRef)
                                                        post.imageUrl = downloadUrlList
                                                        post.title = title_edit_text.text.toString()
                                                        post.description = description_edit_text.text.toString()
                                                        post.tagKeys = postTags
                                                        post.putOnProductList = productList
                                                        transaction.update(postCollectionRef,"imageUrl",post.imageUrl)
                                                        transaction.update(postCollectionRef,"title",post.title)
                                                        transaction.update(postCollectionRef,"description",post.description)
                                                        transaction.update(postCollectionRef,"tagKeys",post.tagKeys)
                                                        transaction.update(postCollectionRef,"putOnProductList",post.putOnProductList)
                                                    }.addOnSuccessListener {
                                                        Toast.makeText(requireContext(),"게시글 수정이 완료됐습니다",Toast.LENGTH_SHORT).show()
                                                        findNavController().navigateUp()
                                                    }.addOnFailureListener {  }
                                                    break
                                                }
                                            }
                                        }
                                        else{

                                        }

                                    }
                            }
                            else {
                                downloadUrlList.add(i,downloadUrl1.toString())
                                downloadUrlList.remove(uri)
                            }

                            Log.d("성공2?", downloadUrl1.toString())

                        }
                    }
                }

                    //db.reference.child("post").push().setValue(postDTO)

            }
        }
        else{
            var postCollectionRef: DocumentReference
            db.collection("post").get()
                .addOnSuccessListener { collection ->
                    if(collection != null){
                        for (doc in collection!!) {
                            var post = doc.toObject(PostData::class.java)
                            if(post.uid == postData!!.uid && post.timestamp == postData!!.timestamp){
                                postCollectionRef = doc.reference // 스냅샷으로부터 리퍼런스 가져오기
                                db.runTransaction { transaction ->
                                    //val snapshot = transaction.get(postCollectionRef)
                                    post.imageUrl = downloadUrlList
                                    post.title = title_edit_text.text.toString()
                                    post.description = description_edit_text.text.toString()
                                    post.tagKeys = postTags
                                    post.putOnProductList = productList
                                    transaction.update(postCollectionRef,"imageUrl",post.imageUrl)
                                    transaction.update(postCollectionRef,"title",post.title)
                                    transaction.update(postCollectionRef,"description",post.description)
                                    transaction.update(postCollectionRef,"tagKeys",post.tagKeys)
                                    transaction.update(postCollectionRef,"putOnProductList",post.putOnProductList)
                                }.addOnSuccessListener {
                                    Toast.makeText(requireContext(),"수정이 완료됐습니다",Toast.LENGTH_SHORT).show()
                                    findNavController().navigateUp()
                                }.addOnFailureListener {  }
                                break
                            }
                        }
                    }
                    else{

                    }

                }
        }
    }

    fun setAllTagButtonDesign(flag: Boolean){

        if(flag){
            all_tag_btn_in_write.setTextColor(Color.parseColor("#faf8f7"))
            all_tag_btn_in_write.setBackgroundResource(R.drawable.selected_circle_image)

            begun_tag_btn_in_write.setTextColor(Color.parseColor("#3e3a39"))
            begun_tag_btn_in_write.setBackgroundResource(R.drawable.circle_image)

            social_tag_btn_in_write.setTextColor(Color.parseColor("#3e3a39"))
            social_tag_btn_in_write.setBackgroundResource(R.drawable.circle_image)

            up_tag_btn_in_write.setTextColor(Color.parseColor("#3e3a39"))
            up_tag_btn_in_write.setBackgroundResource(R.drawable.circle_image)

            eco_tag_btn_in_write.setTextColor(Color.parseColor("#3e3a39"))
            eco_tag_btn_in_write.setBackgroundResource(R.drawable.circle_image)

            animal_tag_btn_in_write.setTextColor(Color.parseColor("#3e3a39"))
            animal_tag_btn_in_write.setBackgroundResource(R.drawable.circle_image)
        }
        else{
            all_tag_btn_in_write.setTextColor(Color.parseColor("#3e3a39"))
            all_tag_btn_in_write.setBackgroundResource(R.drawable.circle_image)
            for(i in 0..4){
                setTagButtonDesign(i,tagButtonState[i])
            }
        }

    }
    fun setTagButtonDesign(tagPosition:Int,flag:Boolean){
        when(tagPosition){
            1 -> {
                if(!flag){
                    social_tag_btn_in_write.setTextColor(Color.parseColor("#3e3a39"))
                    social_tag_btn_in_write.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    social_tag_btn_in_write.setTextColor(Color.parseColor("#faf8f7"))
                    social_tag_btn_in_write.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }
            2 -> {
                if(!flag){
                    up_tag_btn_in_write.setTextColor(Color.parseColor("#3e3a39"))
                    up_tag_btn_in_write.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    up_tag_btn_in_write.setTextColor(Color.parseColor("#faf8f7"))
                    up_tag_btn_in_write.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }
            3 -> {
                if(!flag){
                    eco_tag_btn_in_write.setTextColor(Color.parseColor("#3e3a39"))
                    eco_tag_btn_in_write.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    eco_tag_btn_in_write.setTextColor(Color.parseColor("#faf8f7"))
                    eco_tag_btn_in_write.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }
            4 -> {
                if(!flag){
                    animal_tag_btn_in_write.setTextColor(Color.parseColor("#3e3a39"))
                    animal_tag_btn_in_write.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    animal_tag_btn_in_write.setTextColor(Color.parseColor("#faf8f7"))
                    animal_tag_btn_in_write.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }
            else -> {
                if(!flag){
                    begun_tag_btn_in_write.setTextColor(Color.parseColor("#3e3a39"))
                    begun_tag_btn_in_write.setBackgroundResource(R.drawable.circle_image)
                }
                else{
                    begun_tag_btn_in_write.setTextColor(Color.parseColor("#faf8f7"))
                    begun_tag_btn_in_write.setBackgroundResource(R.drawable.selected_circle_image)
                }
            }
        }
    }

    fun setButton(){
        begun_tag_btn_in_write.setOnClickListener {
            if(!tagButtonState[0]){
                postTags.add(tagList[0])
                setTagButtonDesign(0,true)
                tagButtonState[0] = true
            }
            else{
                postTags.remove(tagList[0])
                setTagButtonDesign(0,false)
                tagButtonState[0] = false
            }
            if(postTags.size == 5) setAllTagButtonDesign(true)
            else setAllTagButtonDesign(false)
            Log.d("Tags", postTags.toString())
        }
        social_tag_btn_in_write.setOnClickListener {
            if(!tagButtonState[1]){
                postTags.add(tagList[1])
                setTagButtonDesign(1,true)
                tagButtonState[1] = true
            }
            else{
                postTags.remove(tagList[1])
                setTagButtonDesign(1,false)
                tagButtonState[1] = false
            }
            if(postTags.size == 5) setAllTagButtonDesign(true)
            else setAllTagButtonDesign(false)
            Log.d("Tags", postTags.toString())
        }
        up_tag_btn_in_write.setOnClickListener {
            if(!tagButtonState[2]){
                postTags.add(tagList[2])
                setTagButtonDesign(2,true)
                tagButtonState[2] = true
            }
            else{
                postTags.remove(tagList[2])
                setTagButtonDesign(2,false)
                tagButtonState[2] = false
            }
            if(postTags.size == 5) setAllTagButtonDesign(true)
            else setAllTagButtonDesign(false)
            Log.d("Tags", postTags.toString())
        }
        eco_tag_btn_in_write.setOnClickListener {
            if(!tagButtonState[3]){
                postTags.add(tagList[3])
                setTagButtonDesign(3,true)
                tagButtonState[3] = true
            }
            else{
                postTags.remove(tagList[3])
                setTagButtonDesign(3,false)
                tagButtonState[3] = false
            }
            if(postTags.size == 5) setAllTagButtonDesign(true)
            else setAllTagButtonDesign(false)
            Log.d("Tags", postTags.toString())
        }
        animal_tag_btn_in_write.setOnClickListener {
            if(!tagButtonState[4]){
                postTags.add(tagList[4])
                setTagButtonDesign(4,true)
                tagButtonState[4] = true
            }
            else{
                postTags.remove(tagList[4])
                setTagButtonDesign(4,false)
                tagButtonState[4] = false
            }
            if(postTags.size == 5) setAllTagButtonDesign(true)
            else setAllTagButtonDesign(false)
            Log.d("Tags", postTags.toString())
        }

        all_tag_btn_in_write.setOnClickListener {
            if(postTags.size == 5){
                postTags.clear()
                for(i in 0..4){
                    tagButtonState[i] = false
                }
                setAllTagButtonDesign(false)
            }
            else{
                postTags.clear()
                for(i in 0..4){
                    postTags.add(tagList[i])
                    tagButtonState[i] = true
                }
                setAllTagButtonDesign(true)
            }
            Log.d("Tags", postTags.toString())
        }
    }

    fun setPhotoRecyclerview(){
        horizontal_photo_recyclerview.adapter = adapter1
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        horizontal_photo_recyclerview.layoutManager = linearLayoutManager
        if(itemDeco != null)
            horizontal_photo_recyclerview.removeItemDecoration(itemDeco)
        horizontal_photo_recyclerview.addItemDecoration(itemDeco)
        (horizontal_photo_recyclerview.adapter as WritePhotoViewAdapter).setItemClickListener(object:
            WritePhotoViewAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                imagePathList.removeAt(position)
                //uriList.removeAt(position)
                adapter1.notifyDataSetChanged()
            }
        })
    }

    fun setPutOnProductRecyclerview(){
        horizontal_put_on_product_recyclerview.adapter = adapter2
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        horizontal_put_on_product_recyclerview.layoutManager = linearLayoutManager
        if(itemDeco != null)
            horizontal_put_on_product_recyclerview.removeItemDecoration(itemDeco)
        horizontal_put_on_product_recyclerview.addItemDecoration(itemDeco)
        (horizontal_put_on_product_recyclerview.adapter as WritePostProductViewAdapter).setItemClickListener(object:
            WritePostProductViewAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                productList.removeAt(position)
                adapter2.notifyDataSetChanged()
            }
        })
    }
}