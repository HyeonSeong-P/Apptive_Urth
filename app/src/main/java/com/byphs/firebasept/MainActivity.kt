package com.byphs.firebasept

import android.Manifest.permission.*
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.byphs.firebasept.Data.BrandData
import com.byphs.firebasept.Data.ProductData
import com.byphs.firebasept.Model.BrandProductDataRepository
import com.byphs.firebasept.Model.PostDataRepository
import com.byphs.firebasept.Model.UserDataRepository
import com.byphs.firebasept.viewmodel.MainActivityViewModel
import com.byphs.firebasept.viewmodel.MainActivityViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*

internal class MainActivity : AppCompatActivity(){
    lateinit var menu:Menu
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener
    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]
    lateinit var host:NavHostFragment
    lateinit var navController: NavController
    lateinit var db: FirebaseFirestore
    lateinit var repository: PostDataRepository
    lateinit var repository2: UserDataRepository
    lateinit var repository3: BrandProductDataRepository
    lateinit var factory: MainActivityViewModelFactory
    lateinit var viewModel: MainActivityViewModel
    lateinit var dialog: Dialog

    private val RC_SIGN_IN = 10
    private lateinit var googleSignInClient: GoogleSignInClient
    val REQUEST_IMAGE_CAPTURE = 1
    var homeCount = 0
    private val requiredPermissions = arrayOf(
        READ_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        menu = bottom_navigation_view.menu
        // 다이얼로그 팝업
        dialog = Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        //Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.loading_initial_data_design)
        // 이걸 추가해야 뒤에 흰배경 없어짐!
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.show()
        initViewFinal()
        initBottom()
        checkPermissions()
       // grantUriPermission()

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        repository = PostDataRepository(db)
        repository2 = UserDataRepository(db)
        repository3 = BrandProductDataRepository(db)
        factory = MainActivityViewModelFactory(repository,repository2,repository3)
        viewModel = ViewModelProvider(this,factory).get(
            MainActivityViewModel::class.java)

        viewModel.initB()
        viewModel.initPR()





        viewModel.allBrandData.observe(this, Observer {

        })

        viewModel.allProductData.observe(this, Observer {

            getBrandProductDataFromRDB()
        })

        mAuthStateListener = FirebaseAuth.AuthStateListener{
            val user = it.currentUser
            if(user!=null){
                if(user.isEmailVerified!!){
                    /*Toast.makeText(this, "로그인 완료",
                        Toast.LENGTH_SHORT).show()*/
                    findNavController(R.id.nav_host_fragment).navigate(R.id.homeFragment)
                }
                else{
                    Toast.makeText(this, "이메일 인증을 완료해주세요",
                        Toast.LENGTH_SHORT).show()

                    //auth.signOut() // 여기 넣으면 오류가 나네? 왜지?
                }

            }
            else{ // 로그인 안됐을 경우. 로그아웃등.
                findNavController(R.id.nav_host_fragment).navigate(R.id.mainFragment)
            }

        }
        auth!!.addAuthStateListener(mAuthStateListener!!) // <필수> 어스스테이트리스너 할당하고 추가해줘야함.

        viewModel.dialogDismissCall.observe(this, Observer {
            dialog.dismiss()
        })
    }

    fun initBottom() {
        bottom_navigation_view.run{
            setOnNavigationItemSelectedListener {item ->
                when(item.itemId){
                    R.id.home_menu -> {
                        item.setIcon(R.drawable.home_selected)
                        menu.findItem(R.id.us_news_menu).setIcon(R.drawable.us_news_unselected)
                        menu.findItem(R.id.bp_menu).setIcon(R.drawable.bp_unselected)
                        menu.findItem(R.id.us_style_menu).setIcon(R.drawable.usstyle_unselected)
                        menu.findItem(R.id.my_page_menu).setIcon(R.drawable.mypage_unselected)
                        if(navController.currentDestination!!.id != R.id.homeFragment){
                            findNavController(R.id.nav_host_fragment).navigate(R.id.homeFragment)
                        }


                    }
                    R.id.us_news_menu -> {
                        item.setIcon(R.drawable.us_news_selected)
                        menu.findItem(R.id.home_menu).setIcon(R.drawable.home_unselected)
                        menu.findItem(R.id.bp_menu).setIcon(R.drawable.bp_unselected)
                        menu.findItem(R.id.us_style_menu).setIcon(R.drawable.usstyle_unselected)
                        menu.findItem(R.id.my_page_menu).setIcon(R.drawable.mypage_unselected)
                        if(navController.currentDestination!!.id != R.id.usNewsFragment){
                            findNavController(R.id.nav_host_fragment).navigate(R.id.usNewsFragment)
                        }
                    }
                    R.id.bp_menu -> {
                        item.setIcon(R.drawable.bp_selected)
                        menu.findItem(R.id.us_news_menu).setIcon(R.drawable.us_news_unselected)
                        menu.findItem(R.id.home_menu).setIcon(R.drawable.home_unselected)
                        menu.findItem(R.id.us_style_menu).setIcon(R.drawable.usstyle_unselected)
                        menu.findItem(R.id.my_page_menu).setIcon(R.drawable.mypage_unselected)
                        if(navController.currentDestination!!.id != R.id.brandProductFragment){
                            findNavController(R.id.nav_host_fragment).navigate(R.id.brandProductFragment)
                        }

                    }
                    R.id.us_style_menu -> {
                        item.setIcon(R.drawable.usstyle_selected)
                        menu.findItem(R.id.us_news_menu).setIcon(R.drawable.us_news_unselected)
                        menu.findItem(R.id.bp_menu).setIcon(R.drawable.bp_unselected)
                        menu.findItem(R.id.home_menu).setIcon(R.drawable.home_unselected)
                        menu.findItem(R.id.my_page_menu).setIcon(R.drawable.mypage_unselected)
                        if(navController.currentDestination!!.id != R.id.usStyleFragment){
                            findNavController(R.id.nav_host_fragment).navigate(R.id.usStyleFragment)
                        }
                    }

                    R.id.my_page_menu -> {
                        item.setIcon(R.drawable.mypage_selected)
                        menu.findItem(R.id.us_news_menu).setIcon(R.drawable.us_news_unselected)
                        menu.findItem(R.id.bp_menu).setIcon(R.drawable.bp_unselected)
                        menu.findItem(R.id.us_style_menu).setIcon(R.drawable.usstyle_unselected)
                        menu.findItem(R.id.home_menu).setIcon(R.drawable.home_unselected)
                        if(navController.currentDestination!!.id != R.id.myPageFragment){
                            findNavController(R.id.nav_host_fragment).navigate(R.id.myPageFragment)
                        }

                    }
                }
                true
            }
        }

    }


    //퍼미션 체크 및 권한 요청 함수
    private fun checkPermissions() {
        //거절되었거나 아직 수락하지 않은 권한(퍼미션)을 저장할 문자열 배열 리스트
        var rejectedPermissionList = ArrayList<String>()

        //필요한 퍼미션들을 하나씩 끄집어내서 현재 권한을 받았는지 체크
        for(permission in requiredPermissions){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                //만약 권한이 없다면 rejectedPermissionList에 추가
                rejectedPermissionList.add(permission)
            }
        }
        //거절된 퍼미션이 있다면...
        if(rejectedPermissionList.isNotEmpty()){
            //권한 요청!
            val array = arrayOfNulls<String>(rejectedPermissionList.size)
            ActivityCompat.requestPermissions(this, rejectedPermissionList.toArray(array), 0)
        }
    }


    fun initViewFinal() {
        //setSupportActionBar(main_toolbar) // 전체화면에 메인 툴바를 넣겠다.

        host = nav_host_fragment as NavHostFragment //우리가 만든것(nav_host_fragment)과 이미 있는것을 결합.nav_host_fragment 는 view,xml
        //NavHostFragment 는 클래스
        navController = host.navController // 바로 윗줄 포함 두줄 필수. 네비게이션.xml에 접근위해.


        navController.addOnDestinationChangedListener { _, destination, _ ->
            val dest: String = try{
                resources.getResourceName(destination.id)
            } catch (e: Exception){
                return@addOnDestinationChangedListener
            }
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            when(destination.id){
                R.id.homeFragment -> {
                    /*if(homeCount == 0){
                        bottom_navigation_view.selectedItemId = R.id.home_menu
                        homeCount++
                    }*/
                    bottom_navigation_view.selectedItemId = R.id.home_menu

                    bottom_navigation_view.visibility = View.VISIBLE
                }
                R.id.usNewsFragment -> {
                    bottom_navigation_view.selectedItemId = R.id.us_news_menu

                    bottom_navigation_view.visibility = View.VISIBLE
                }
                R.id.brandProductFragment -> {
                    bottom_navigation_view.selectedItemId = R.id.bp_menu

                    bottom_navigation_view.visibility = View.VISIBLE
                }
                R.id.usStyleFragment -> {
                    bottom_navigation_view.selectedItemId = R.id.us_style_menu

                    bottom_navigation_view.visibility = View.VISIBLE
                }
                R.id.myPageFragment -> {
                    bottom_navigation_view.selectedItemId = R.id.my_page_menu

                    bottom_navigation_view.visibility = View.VISIBLE
                }
                else -> bottom_navigation_view.visibility = View.GONE


            }

        }


    }




    fun priceStringToInt2(string: String):Int{
        var retPrice:Int = 0
        var priceString:String
        var priceStringList = mutableListOf<String>()
        if(string.contains(' ')){
            var priceList = string.split(' ')
                .map { s -> s.replace(("[^\\d]").toRegex(),"")}
                .filter { it != ""}
                .map { ret -> ret.toInt() }.toIntArray()

            retPrice = priceList.min()?:0 // min 메서드가 deprecated 돼서 minOrNull을 사용하려 했으나 무슨 문제인지는 모르겠지만 작동하지 않아 min에 null처리를 해주는 방식으로 대체
        }
        else{
            retPrice = string.replace(("[^\\d.]").toRegex(),"").toInt()
        }

        return retPrice
    }


    private fun getBrandProductDataFromRDB(){
        var i = 0
        var k = 0
        var productHashMap = hashMapOf<String,Boolean>()
        var rdbRef = FirebaseDatabase.getInstance().reference
        var dialogFlag = true
        val brandProductListener = object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for((num,d) in snapshot.children.withIndex()){
                    var brandName = d.key!!
                    //Log.d("브랜드 이름",brandName)
                    var brandData = BrandData()
                    //brandData.brandName = brandName
                    for((n,a) in d.children.withIndex()){
                        //Log.d("카운트",d.children.count().toString())
                        if(a.key!! == "0"){
                            //Log.d("브랜드 이름",brandName)
                            brandData.logoImage = a.child("logo").value.toString()
                            brandData.brandLink = a.child("brandLink").value as String
                            brandData.brandName = a.child("brandName").value as String
                            brandName = brandData.brandName
                            brandData.koreanBrandName = a.child("koreanBrandName").value as String
                            brandData.backgroundImage = a.child("background").value as String
                            brandData.description = a.child("description").value as String
                            brandData.tagKeys = a.child("tagKeys").value as MutableList<String>
                            if(!viewModel.detectBrand(brandData)){
                                viewModel.addBrand(brandData)
                                k++
                                //Log.d(" 브랜드 개수 셈",k.toString()+ ", " +brandData.brandName)
                            }
                        }
                        else{
                            var productData = ProductData()
                            var img = a.child("img").value as String
                            var name = a.child("name").value
                            var price = a.child("price").value
                            var link = a.child("link").value
                            var category = a.child("category").value

                            if(img[0] == '/' && img[1] == '/'){
                                img = "https:$img"
                            }
                            productData.imageUrl = img
                            productData.productName = name as String
                            //Log.d("상품 이름",productData.productName)
                            productData.price = priceStringToInt2(price as String)
                            productData.purchaseLink = link as String
                            productData.brandName = brandName
                            productData.categoryTag = category as String
                            var list = mutableListOf<ProductData>()
                            //i++
                            //Log.d("개수 셈",i.toString()+ ", " +productData.productName)
                            if(!viewModel.detectProduct(productData)) {
                                dialogFlag = false
                                if(!productHashMap.containsKey(productData.productName)){
                                    if(n == d.children.count() - 1 && num == snapshot.children.count() - 1){
                                        viewModel.addProduct(productData,true)
                                        productHashMap.put(productData.productName,true)
                                    }
                                    else{
                                        viewModel.addProduct(productData,false)
                                        productHashMap.put(productData.productName,true)
                                    }


                                }
                            }
                            else{
                                //Log.d("있는데??0","있는데??")
                            }
                        }
                    }
                }
                if(dialogFlag){
                    dialog.dismiss()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        rdbRef.addValueEventListener(brandProductListener)

    }
}