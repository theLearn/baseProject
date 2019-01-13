package com.example.hongcheng.app.viewModel

import android.arch.lifecycle.MutableLiveData
import com.example.hongcheng.app.adapter.models.AnimationDetailModel
import com.example.hongcheng.app.adapter.models.AnimationModel
import com.example.hongcheng.app.application.BaseApplication
import com.example.hongcheng.common.base.BaseViewModel
import com.example.hongcheng.common.base.CommonUI
import com.example.hongcheng.common.util.LoggerUtils
import com.example.hongcheng.common.util.ToastUtils
import com.example.hongcheng.data.retrofit.*
import com.example.hongcheng.data.retrofit.request.CardRetrofit
import com.example.hongcheng.data.retrofit.response.models.Card
import com.example.hongcheng.data.retrofit.response.models.CardRecommend
import com.example.hongcheng.data.room.DBInit
import com.example.hongcheng.data.room.RoomClient
import com.example.hongcheng.data.room.entity.CardDetailInfo
import com.example.hongcheng.data.room.entity.CardEntity
import com.example.hongcheng.data.room.entity.CardInfoEntity
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe


class CardViewModel(private var ui: CommonUI?) : BaseViewModel(ui) {
    val typeList = MutableLiveData<ArrayList<String>>()
    val cardTypeList = MutableLiveData<ArrayList<String>>()
    val liveList = MutableLiveData<ArrayList<AnimationModel>>()
    val liveDetailList = MutableLiveData<ArrayList<AnimationDetailModel>>()
    val list = MutableLiveData<ArrayList<CardDetailInfo>>()

    fun getAnimationModelList(cardType: String) {
        compositeDisposable.add(
            RetrofitClient.getInstance().map<List<Card>>(
                RetrofitManager.createRetrofit<CardRetrofit>(BaseApplication.getInstance(), CardRetrofit::class.java)
                    .listCards(), object : BaseSubscriber<List<Card>>() {
                    override fun onError(e: ActionException) {
                        ToastUtils.show(BaseApplication.getInstance(), e.message)
                        getCardsFromDB(cardType)
                    }

                    override fun onBaseNext(cardResponse: List<Card>) {

                        val data = arrayListOf<AnimationModel>()
                        for (card in cardResponse) {
                            val model = AnimationModel(card.name, card.imageUrl, card.description, card.type)
                            data.add(model)
                        }
                        liveList.postValue(data)
                    }
                })
        )
    }

    fun getAnimationDetailModelList(type: String, cardType: String) {
        ui?.operateLoadingDialog(true)
        compositeDisposable.add(
            RetrofitClient.getInstance().map(RetrofitManager.createRetrofit(
                BaseApplication.getInstance(),
                CardRetrofit::class.java
            )
                .getCardDetail(type), object : BaseSubscriber<List<CardRecommend>>() {
                override fun onError(e: ActionException) {
                    ToastUtils.show(BaseApplication.getInstance(), e.message)
                    getCardInfoFromDB(type, cardType)
                }

                override fun onBaseNext(cardDetailResponse: List<CardRecommend>) {
                    val data = arrayListOf<AnimationDetailModel>()
                    for (item in cardDetailResponse) {
                        val model = AnimationDetailModel(
                            item.imageUrl,
                            item.content,
                            item.description,
                            item.date,
                            item.infoId
                        )
                        data.add(model)
                    }
                    liveDetailList.postValue(data)
                }
            })
        )
    }

    fun getCardsFromDB(cardType: String) {
        val data = arrayListOf<AnimationModel>()
        data.add(AnimationModel("我家网络", "http://aa", "可以管理wifi哦", ""))
        data.add(AnimationModel("我家看看", "http://aa", "可以看视频哦", ""))
        data.add(AnimationModel("我家存储", "http://aa", "里面有好东西哦", ""))
        data.add(AnimationModel("能耗管理", "http://aa", "节约用电，人人有责", ""))
        data.add(AnimationModel("家庭安防", "http://aa", "让你的家更安全", ""))
        data.add(AnimationModel("环境监控", "http://aa", "随时感知房间环境的变化，是生活更舒适", ""))

        val source : Flowable<List<CardEntity>>? = DBInit.getInstance().getAppDatabase()?.getCardDao()?.getCardsByType(cardType)
        if(source != null) {
            compositeDisposable.add(RoomClient.getInstance().map(source, object : BaseFlowableSubscriber<List<CardEntity>>() {
                override fun onBaseNext(t: List<CardEntity>?) {
                    if (t != null && !t.isEmpty()) {
                        data.clear()
                        for (entity : CardEntity in t) {
                            data.add(AnimationModel(entity.name, entity.imageUrl, entity.description, entity.type, entity.cardType))
                        }
                    }
                    liveList.postValue(data)
                }

                override fun onError(e: ActionException?) {
                    ToastUtils.show(BaseApplication.getInstance(), e?.message)
                    liveList.postValue(data)
                }
            }))
        } else {
            liveList.postValue(data)
        }
    }

    fun getCardInfoFromDB(type: String, cardType: String) {
        val data = arrayListOf<AnimationDetailModel>()
        data.add(AnimationDetailModel("http://aa", "从零开始", "20话", "2016-09-15 00:00:00", ""))
        data.add(AnimationDetailModel("http://aa", "龙珠超", "20话", "2016-09-15 00:00:00", ""))
        data.add(AnimationDetailModel("http://aa", "灵能百分百", "20话", "2016-09-15 00:00:00", ""))
        data.add(AnimationDetailModel("http://aa", "驱魔少年", "20话", "2016-09-15 00:00:00", ""))
        data.add(AnimationDetailModel("http://aa", "热诚传说", "20话", "2016-09-15 00:00:00", ""))
        data.add(AnimationDetailModel("http://aa", "弹丸论破", "20话", "2016-09-15 00:00:00", ""))
        data.add(AnimationDetailModel("http://aa", "海贼王", "20话", "2016-09-15 00:00:00", ""))
        data.add(AnimationDetailModel("http://aa", "美食的俘虏", "20话", "2016-09-15 00:00:00", ""))
        data.add(AnimationDetailModel("http://aa", "食戟之灵", "20话", "2016-09-15 00:00:00", ""))
        data.add(AnimationDetailModel("http://aa", "狐妖小红娘", "20话", "2016-09-15 00:00:00", ""))

        val source : Flowable<List<CardInfoEntity>>? = DBInit.getInstance().getAppDatabase()?.getCardInfoDao()?.getAllInfoByCard(type, cardType)
        if(source != null) {
            compositeDisposable.add(RoomClient.getInstance().map(source, object : BaseFlowableSubscriber<List<CardInfoEntity>>() {
                override fun onBaseNext(t: List<CardInfoEntity>?) {
                    if (t != null && !t.isEmpty()) {
                        data.clear()
                        for (entity : CardInfoEntity in t) {
                            data.add(AnimationDetailModel(entity.imageUrl, entity.name, entity.description, entity.date, entity.infoId.toString(), entity.type, entity.cardType))
                        }
                    }
                    liveDetailList.postValue(data)
                }

                override fun onError(e: ActionException?) {
                    ToastUtils.show(BaseApplication.getInstance(), e?.message)
                    liveDetailList.postValue(data)
                }
            }))
        } else {
            liveDetailList.postValue(data)
        }
    }

    fun getAll() {
        ui?.operateLoadingDialog(true)
        val data = arrayListOf<CardDetailInfo>()

        val source : Flowable<List<CardDetailInfo>>? = DBInit.getInstance().getAppDatabase()?.getCardDao()?.getAllCardAndInfo()
        if(source != null) {
            compositeDisposable.add(RoomClient.getInstance().map(source, object : BaseFlowableSubscriber<List<CardDetailInfo>>() {
                override fun onBaseNext(t: List<CardDetailInfo>?) {
                    if (t != null && !t.isEmpty()) {
                        for (entity : CardDetailInfo in t) {
                            data.add(entity)
                        }
                    }

                    list.postValue(data)
                }

                override fun onError(e: ActionException?) {
                    ToastUtils.show(BaseApplication.getInstance(), e?.message)
                    list.postValue(data)
                }
            }))
        } else {
            list.postValue(data)
        }
    }

    fun getAllType() {
        val types = arrayListOf<String>()
        val cardTypes = arrayListOf<String>()

        val source : Flowable<List<CardEntity>>? = DBInit.getInstance().getAppDatabase()?.getCardDao()?.getAllCards()
        if(source != null) {
            compositeDisposable.add(RoomClient.getInstance().map(source, object : BaseFlowableSubscriber<List<CardEntity>>() {
                override fun onBaseNext(t: List<CardEntity>?) {
                    if (t != null && !t.isEmpty()) {
                        for (entity : CardEntity in t) {
                            types.add(entity.type)
                            cardTypes.add(entity.cardType)
                        }
                    }

                    typeList.postValue(types)
                    cardTypeList.postValue(cardTypes)
                }

                override fun onError(e: ActionException?) {
                    ToastUtils.show(BaseApplication.getInstance(), e?.message)

                    typeList.postValue(types)
                    cardTypeList.postValue(cardTypes)
                }
            }))
        } else {
            typeList.postValue(types)
            cardTypeList.postValue(cardTypes)
        }
    }

    fun insertCard(cards: List<CardEntity>) {
        compositeDisposable.add(RoomClient.getInstance().create(FlowableOnSubscribe {
            val result = DBInit.getInstance().getAppDatabase()?.getCardDao()?.insertOrReplace(cards)
            if (result != null) {
                it.onNext(result)
            }
            it.onComplete()
        }, object : BaseFlowableSubscriber<List<Long>>() {
            override fun onBaseNext(t: List<Long>?) {
                if (t != null && !t.isEmpty()) {
                    ToastUtils.show(BaseApplication.getInstance(), t.toString())
                    LoggerUtils.error("insert", t.toString())
                }
            }

            override fun onError(e: ActionException?) {
                ToastUtils.show(BaseApplication.getInstance(), e?.message)
            }
        }))
    }

    fun insertCardInfo(cards: List<CardInfoEntity>) {
        compositeDisposable.add(RoomClient.getInstance().create(FlowableOnSubscribe {
            val result = DBInit.getInstance().getAppDatabase()?.getCardInfoDao()?.insertOrReplace(cards)
            if (result != null) {
                it.onNext(result)
            }
            it.onComplete()
        }, object : BaseFlowableSubscriber<List<Long>>() {
            override fun onBaseNext(t: List<Long>?) {
                if (t != null && !t.isEmpty()) {
                    ToastUtils.show(BaseApplication.getInstance(), t.toString())
                    LoggerUtils.error("insert", t.toString())
                }
            }

            override fun onError(e: ActionException?) {
                ToastUtils.show(BaseApplication.getInstance(), e?.message)
            }
        }))
    }
}