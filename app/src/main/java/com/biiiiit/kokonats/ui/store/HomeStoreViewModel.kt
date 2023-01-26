package com.biiiiit.kokonats.ui.store

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.*
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.bean.NetInAppProduct
import com.biiiiit.kokonats.data.repo.PayTokenRepository
import com.biiiiit.kokonats.data.repo.StoreRepository
import com.biiiiit.kokonats.db.InAppPayToken
import com.biiiiit.kokonats.db.STATUS_BACK_CONFIRM
import com.biiiiit.kokonats.db.STATUS_NEW
import com.biiiiit.lib_base.base.BaseViewModel
import com.biiiiit.lib_base.base.TAG_LOADING_HIDE
import com.biiiiit.lib_base.base.TAG_LOADING_SHOW
import com.biiiiit.lib_base.data.LoginUser
import com.biiiiit.lib_base.net.LOADING_TXT
import com.biiiiit.lib_base.utils.SP_LOGIN_USER
import com.biiiiit.lib_base.utils.getAny
import com.biiiiit.lib_base.utils.logi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @Author yo_hack
 * @Date 2021.11.05
 * @Description
 **/
class HomeStoreViewModel : BaseViewModel() {

    /** 简单初始化 **/
    private val ONLY_INIT = 1

    /** 查寻处google product **/
    private val ONLY_QUERY = 2

    /** 点击购买 **/
    private val QUERY_BUY = 3


    /**
     * 后期考虑 inject
     */
    private val repo: StoreRepository by lazy { StoreRepository() }

//    private val dbRepo: PayTokenRepository by lazy { PayTokenRepository() }


    private var uid: Long = 0

    private val MAX_CONNECT_TIME = 5
    private var reconnectTimes = 0
    private var newQuery: Int = ONLY_INIT


    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        onPurchasesUpdated(billingResult, purchases)
    }

    private var billingClient: BillingClient? = null

    /** 获取到的网络数据 **/
    val productList = MutableLiveData<MutableList<NetInAppProduct>>()

    val hideRefresh = MutableLiveData<Boolean>()

    /** 需要购买的sku **/
    private var tempBuyPid: String = ""

    /** 即将购买的 sku **/
    val buySkuDetails = MutableLiveData<SkuDetails>()

    /** 缓存的 billClient products 数据 **/
    private var cacheGoogleProducts = listOf<SkuDetails>()

    /** chargeSuccess **/
    val purchaseEvent = MutableLiveData<Boolean>()

    fun queryNetInAppProduct(context: Context?) {
        // connect
        connectAndQuery(context, ONLY_INIT)

        request(
            {
                repo.getBackProductList()
            },
            {
                productList.postValue(mapNetAndGoogleProducts(it)?.toMutableList())
                connectAndQuery(context, ONLY_QUERY)
                hideRefresh.postValue(true)
            },
            error = {
                toastMsg.postValue(it.message)
                hideRefresh.postValue(true)
            },
            loadingMsg = null
        )
    }

    private fun connectAndQuery(context: Context?, query: Int) {
        createBillingClient(context)
        connectService(query)
    }

    private fun createBillingClient(context: Context?) {
        if (context == null) {
            return
        }
        reconnectTimes = 0
        if (billingClient != null) {
            return
        }
        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
    }

    private fun connectService(query: Int) {
        val state = billingClient?.connectionState
        if (state == BillingClient.ConnectionState.CONNECTED) {
            reconnectTimes = 0
            querySkuAndBillLocal(query)
            return
        }
        newQuery = query
        if (state == BillingClient.ConnectionState.CONNECTING) {
            return
        }
        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                when (billingResult.responseCode) {
                    BillingClient.BillingResponseCode.OK -> {
                        reconnectTimes = 0
                        querySkuAndBillLocal(newQuery)
                    }
                    BillingClient.BillingResponseCode.SERVICE_TIMEOUT -> {
                        if (reconnectTimes++ < MAX_CONNECT_TIME) {
                            connectService(newQuery)
                        } else {
                            // 购买查询商品超时
                            errorQueryBuy(newQuery)
                        }
                    }
                    else -> {
                        // 购买查询商品超时
                        errorQueryBuy(query)
                    }
                }
            }


            override fun onBillingServiceDisconnected() {
            }
        })
    }

    /**
     * 查询 google 的商品
     */
    private fun querySkuGoogleDetails(query: Int) {
        val netList = productList.value
        if (netList.isNullOrEmpty()) {
            errorQueryBuy(query)
            return
        }

        val skuList = netList.map {
            it.productId
        }
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
        viewModelScope.launch {
            val result = billingClient?.querySkuDetails(params.build())
            // get Result
            val skuDetailsList = result?.skuDetailsList
            if (skuDetailsList.isNullOrEmpty()) {
                errorQueryBuy(query)
            } else {
                cacheGoogleProducts = skuDetailsList
                productList.postValue(mapNetAndGoogleProducts(productList.value)?.toMutableList())
                if (query == QUERY_BUY) {
                    // 需要购买
                    skuDetailsList.find { it.sku == tempBuyPid }?.let {
                        buySkuDetails.postValue(it)
                    } ?: kotlin.run {
                        errorQueryBuy(query)
                    }
                }
            }
        }

    }

    /**
     * 映射google and local products
     */
    private fun mapNetAndGoogleProducts(netPList: List<NetInAppProduct>?): List<NetInAppProduct>? {
        cacheGoogleProducts.forEach { sku ->
            productList.value?.find { it.productId == sku.sku }?.apply {
                this.originJson = sku.originalJson
                this.localPrice = sku.price
            }
        }
        return netPList
    }

    /** 点击购买时， 查询sku时失败 **/
    private fun errorQueryBuy(query: Int) {
        if (query == QUERY_BUY) {
            // 点击购买失败
            postLoading(TAG_LOADING_HIDE, null)
        }
    }

    /** **/
    fun startInAppPurchase(product: NetInAppProduct, context: Context?) {
        retryUploadCount = 0
        product.map2Local()?.let {
            // 有缓存
            buySkuDetails.postValue(it)
        } ?: kotlin.run {
            tempBuyPid = product.productId
            postLoading(TAG_LOADING_SHOW, LOADING_TXT)
            connectAndQuery(context, QUERY_BUY)
        }
    }


    /**
     * 调用支付流程
     */
    fun launchPayIntent(activity: Activity, skuDetails: SkuDetails) {
        postLoading(TAG_LOADING_HIDE, null)
        // show Loading
        val flowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails)
            .build()

        // code launch to purchasesUpdatedListener
        val response = billingClient?.launchBillingFlow(activity, flowParams)
        if (response?.responseCode != BillingClient.BillingResponseCode.OK) {
            // 启动 flow 失败
            toastMsg.postValue(activity.getString(R.string.unknown_error))
        }

    }


    /**
     * 状态更新
     */
    private fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                if (!purchases.isNullOrEmpty()) {
                    // 购买成功 handlePurchase
                    postLoading(TAG_LOADING_SHOW, LOADING_TXT)
                    handlePurchase(purchases, false)
                }
            }
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                // 已经购买了
                queryBillAsync()
            }
            BillingClient.BillingResponseCode.ITEM_UNAVAILABLE,
            BillingClient.BillingResponseCode.DEVELOPER_ERROR -> {
                // developer error

            }

            // hide Loading
        }

    }

    /**
     * 查询sku的数据和本地数据
     */
    private fun querySkuAndBillLocal(query: Int) {
        "ahhh querySkuAndBillLocal ${query}".logi("----")
        if (query == ONLY_QUERY) {
            queryBillAsync()
            queryBillLocal()
        }
        if (query != ONLY_INIT) {
            querySkuGoogleDetails(query)
        }
    }

    private fun setNewUid() {
        uid = getAny<LoginUser>(SP_LOGIN_USER)?.id ?: 0
    }

    /**
     * 本地的bill
     */
    private fun queryBillLocal() {
        setNewUid()
//        doJob(
//            {
//                dbRepo.getAllByUId(uid)
//            },
//            {
//                it.forEach {
//                    when (it.status) {
//                        STATUS_NEW -> {
//                            retryUploadCount = 0
//                            uploadAndConsume(it.token, it.skuId, true)
//                        }
//                        STATUS_BACK_CONFIRM -> {
//                            consumeParams(it.token)
//                        }
//
//                    }
//                }
//            },
//            loadingMsg = null,
//            error = null
//        )
    }

    /**
     * 是否有未核销的订单
     * 核销分两步 --> 服务器核销 --> 本地消费
     */
    private fun queryBillAsync() {
        runCatching {
            billingClient?.queryPurchasesAsync(BillingClient.SkuType.INAPP) { result, list ->
//                    onPurchasesUpdated(result, list)
                if (result.responseCode == BillingClient.BillingResponseCode.OK && !list.isNullOrEmpty()) {
                    handlePurchase(list, true)
                }
            }
        }
    }


    /**
     * 处理 purchase
     */
    private fun handlePurchase(purchases: List<Purchase>, isOld: Boolean) {
        setNewUid()
        purchases.forEach { purchase ->
            when (purchase.purchaseState) {
                Purchase.PurchaseState.PURCHASED -> {// 购买成功
                    uploadAndConsume(
                        purchase.purchaseToken,
                        purchase.skus.getOrNull(0) ?: "",
                        isOld
                    )
                }
                Purchase.PurchaseState.PENDING -> {// 加载中
                    // 关闭弹窗
                }
            }
        }
    }

    /**
     * 上传服务器处理
     */
    private fun uploadAndConsume(purchaseToken: String, skuId: String, isOld: Boolean) {
        // 开始刷新
        request(
            {
                if (!isOld) {
//                    it.async {
//                        dbRepo.insert(
//                            InAppPayToken(uid, purchaseToken, skuId)
//                        )
//                    }
                }
                repo.uploadPayToken(purchaseToken, skuId)
            },
            {
                when (it.state) {
                    1 -> {
                        retryUploadCount = 0
                        chargeSuccess(purchaseToken, skuId)
                    }
                    2 -> {
                        // 重复验证
                        retryUploadCount = 0
                        if (!isOld) {
                            chargeSuccess(purchaseToken, skuId)
                        }
                    }
                    else -> {
                        // 验证失败 或 pending 状态
                        retryUploadAndConsume(purchaseToken, skuId, isOld)
                    }
                }
            },
            error = {
                // 充值失败， 稍后重新充值
                retryUploadAndConsume(purchaseToken, skuId, isOld)
            },
            loadingMsg = null
        )
    }

    /** 充值成功发送消息 **/
    private fun chargeSuccess(purchaseToken: String, skuId: String) {
        purchaseEvent.postValue(true)
        postLoading(TAG_LOADING_HIDE, null)
        consumeParams(purchaseToken)
//        doJob { dbRepo.updateByToken(purchaseToken, STATUS_BACK_CONFIRM) }
    }

    private var retryUploadCount = 0

    var stopRetryConsume = false

    /** 重试上传 **/
    private fun retryUploadAndConsume(purchaseToken: String, skuId: String, isOld: Boolean) {
        viewModelScope.launch {
            delay((2 shl minOf(31, retryUploadCount)) * 50L)

            if (!stopRetryConsume) {
                uploadAndConsume(purchaseToken, skuId, isOld)
            }
            if (retryUploadCount++ > 6 && !isOld) {
                // TODO 需要错误提示 点击购买
                postLoading(TAG_LOADING_HIDE, null)
            }
        }
    }


    /**
     * 验证 token
     */
    private fun consumeParams(purchaseToken: String) {
        //
        val consumeParams =
            ConsumeParams.newBuilder()
                .setPurchaseToken(purchaseToken)
                .build()
        billingClient?.consumeAsync(consumeParams) { result, token ->
            // 更新数据库
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
//                doJob { dbRepo.deleteByToken(purchaseToken) }
            }
        }
    }

}