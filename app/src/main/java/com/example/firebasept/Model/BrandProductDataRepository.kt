package com.example.firebasept.Model

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.firebasept.Data.*
import com.example.firebasept.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class BrandProductDataRepository(private val db: FirebaseFirestore) {
    private val mAuth = FirebaseAuth.getInstance()
    private var shareBrandList:MutableList<BrandData> = mutableListOf()
    private var shareProductList:MutableList<ProductData> = mutableListOf()
    private var shareCampaignList:MutableList<CampaignData> = mutableListOf()

    private var brandHashMapForDetect = hashMapOf<String,Boolean>()
    private var productHashMapForDetect = hashMapOf<String,Boolean>()

    private var liveBrandData: MutableLiveData<List<BrandData>> = MutableLiveData<List<BrandData>>()
    private var liveProductData: MutableLiveData<List<ProductData>> = MutableLiveData<List<ProductData>>()
    private var liveCampaignData: MutableLiveData<List<CampaignData>> = MutableLiveData()

    private var liveDialogDismissCall: SingleLiveEvent<Boolean> = SingleLiveEvent()


    fun getProduct(productName:String,brandName:String): ProductData? {
        var returnProduct: ProductData? = null
        for(product in shareProductList){
            if(product.productName == productName && product.brandName == brandName){
                returnProduct = product
                break
            }
        }
        return returnProduct
    }

    fun getBrand(brandName:String): BrandData? {
        var returnBrand: BrandData? = null
        for(brand in shareBrandList){
            if(brand.brandName == brandName){
                returnBrand = brand
            }
        }
        return returnBrand
    }

    fun detectProduct(productData: ProductData):Boolean{
        /*Log.d("갯수",shareProductList.size.toString())
        Log.d("갯수",shareProductList.contains(productData).toString())
        Log.d("1빠",shareProductList[1].productName.toString())*/
        if(productData.productName == "Warm scarf tweed Nest Bag (Black)"){
            Log.d("상품 이름 관련",productHashMapForDetect.containsKey(productData.productName).toString())
        }
        return productHashMapForDetect.containsKey(productData.productName)

    }

    fun detectBrand(brandData: BrandData):Boolean{

        return brandHashMapForDetect.containsKey(brandData.brandName)
    }

    fun getPR(){
        var productList:MutableList<ProductData> = mutableListOf()

        db.collection("product").get().addOnSuccessListener { snapshot->
            if (snapshot != null) {
                for (doc in snapshot!!) {
                    var productData = doc.toObject(ProductData::class.java)
                    productList.add(productData)
                    productHashMapForDetect[productData.productName] = true
                }
                shareProductList = productList
                liveProductData.setValue(productList.toList())// 무조건 여기 안에 넣어라!


            } else {
                //Log.d(TAG, "Current data: null")
            }
        }
    }

    fun getB(){
        var brandList:MutableList<BrandData> = mutableListOf()

        db.collection("brand").get().addOnSuccessListener { snapshot->
            if (snapshot != null) {
                for (doc in snapshot!!) {
                    var brandData = doc.toObject(BrandData::class.java)
                    brandList.add(brandData)
                    brandHashMapForDetect[brandData.brandName] = true
                }
                shareBrandList = brandList
                liveBrandData.setValue(brandList.toList())// 무조건 여기 안에 넣어라!


            } else {
                //Log.d(TAG, "Current data: null")
            }
        }
    }

    fun getC(){
        var campaignList:MutableList<CampaignData> = mutableListOf()

        db.collection("campaign").get().addOnSuccessListener { snapshot->
            if (snapshot != null) {
                for (doc in snapshot!!) {
                    var campaignData = doc.toObject(CampaignData::class.java)
                    campaignList.add(campaignData)
                }
                shareCampaignList = campaignList
                liveCampaignData.setValue(campaignList.toList())// 무조건 여기 안에 넣어라!


            } else {
                //Log.d(TAG, "Current data: null")
            }
        }
    }

    fun addProduct(productData: ProductData,boolean: Boolean){
        db.collection("product").add(productData)
            .addOnSuccessListener { documentReference ->
                if(boolean){
                    liveDialogDismissCall.setValue(true)
                }
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }

    fun addBrand(brandData: BrandData){
        db.collection("brand").add(brandData)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                // 자동 뒤로가기는 라이브 데이터를 셋해서 처리해주자. 위에 콜백 스테이트처럼.
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }

    //브랜드 좋아요
    fun clickLikeBrandInRepository(brandName: String){
        var brandCollectionRef: DocumentReference
        db.collection("brand").get()
            .addOnSuccessListener { collection ->
                if(collection != null){
                    for (doc in collection!!) {
                        var brandData = doc.toObject(BrandData::class.java)
                        if(brandData.brandName == brandName){
                            brandCollectionRef = doc.reference // 스냅샷으로부터 리퍼런스 가져오기
                            db.runTransaction { transaction ->
                                //val snapshot = transaction.get(postCollectionRef)
                                if(brandData.likes.containsKey(mAuth.currentUser.uid)){
                                    brandData.likeCount -= 1
                                    brandData.likes.remove(mAuth.currentUser.uid)
                                }
                                else{
                                    brandData.likeCount += 1
                                    brandData.likes.put(mAuth.currentUser.uid,true)
                                }
                                transaction.update(brandCollectionRef,"likes",brandData.likes)
                                transaction.update(brandCollectionRef,"likeCount",brandData.likeCount)

                            }.addOnSuccessListener {
                                getB()
                                //_likeState.setValue(true)
                            }.addOnFailureListener {  }
                            break
                        }
                    }
                }
                else{

                }

            }


    }

    //상품 좋아요
    fun clickLikeProductInRepository(productName: String, brandName: String){
        var productCollectionRef: DocumentReference
        db.collection("product").get()
            .addOnSuccessListener { collection ->
                if(collection != null){
                    for (doc in collection!!) {
                        var productData = doc.toObject(ProductData::class.java)
                        if(productData.productName == productName && productData.brandName == brandName){
                            productCollectionRef = doc.reference // 스냅샷으로부터 리퍼런스 가져오기
                            db.runTransaction { transaction ->
                                //val snapshot = transaction.get(postCollectionRef)
                                if(productData.likes.containsKey(mAuth.currentUser.uid)){
                                    productData.likeCount -= 1
                                    productData.likes.remove(mAuth.currentUser.uid)
                                }
                                else{
                                    productData.likeCount += 1
                                    productData.likes.put(mAuth.currentUser.uid,true)
                                }
                                transaction.update(productCollectionRef,"likes",productData.likes)
                                transaction.update(productCollectionRef,"likeCount",productData.likeCount)

                            }.addOnSuccessListener {
                                getPR()
                                //_likeState.setValue(true)
                            }.addOnFailureListener {  }
                            break
                        }
                    }
                }
                else{

                }

            }


    }

    fun getAllBrand(): LiveData<List<BrandData>> { return liveBrandData }
    fun getAllProduct(): LiveData<List<ProductData>> { return liveProductData }
    fun getAllCampaign(): LiveData<List<CampaignData>> { return liveCampaignData }
    fun getDialogCall(): SingleLiveEvent<Boolean>{ return liveDialogDismissCall }
}