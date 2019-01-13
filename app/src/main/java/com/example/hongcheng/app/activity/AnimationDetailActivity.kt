package com.example.hongcheng.app.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.example.hongcheng.app.R
import com.example.hongcheng.app.adapter.AnimationDetailAdapter
import com.example.hongcheng.app.adapter.models.AnimationDetailModel
import com.example.hongcheng.app.adapter.models.AnimationModel
import com.example.hongcheng.app.databinding.ActivityAnimationDetailBinding
import com.example.hongcheng.app.viewModel.CardViewModel
import com.example.hongcheng.app.viewModel.CardViewModelFactory
import com.example.hongcheng.common.base.BasicActivity
import com.example.hongcheng.common.util.SafeIntentUtils
import com.example.hongcheng.common.util.ScreenUtils
import com.example.hongcheng.common.view.DividerItemDecoration
import com.example.hongcheng.common.view.viewHelper.AppBarStateChangeListener
import kotlinx.android.synthetic.main.activity_animation_detail.*

class AnimationDetailActivity : BasicActivity() {
    private val mAdapter = AnimationDetailAdapter()

    override fun getLayoutResId(): Int {
        return R.layout.activity_animation_detail
    }

    override fun isNeedBind(): Boolean {
        return true
    }

    override fun initView() {
        setSupportActionBar(tb_detail)
        tb_detail.setNavigationIcon(R.drawable.back)
        tb_detail.setNavigationOnClickListener { onBackPressed() }
        appBar_detail.addOnOffsetChangedListener(listener)

        val model: AnimationModel = intent.getParcelableExtra(SafeIntentUtils.INTENT_MODEL)
        (binding as ActivityAnimationDetailBinding).model = model
        rv_detail.layoutManager = LinearLayoutManager(rv_detail.context)
        rv_detail.addItemDecoration(DividerItemDecoration(
                this, LinearLayoutManager.HORIZONTAL, 1, resources.getColor(R.color.line_gray)))

        rv_detail.itemAnimator = DefaultItemAnimator()
        rv_detail.adapter = mAdapter

        val factory = CardViewModelFactory(this)
        val viewModel: CardViewModel = ViewModelProviders.of(this, factory).get(CardViewModel::class.java)
        viewModel.liveDetailList.observe(this, Observer<ArrayList<AnimationDetailModel>> {
            operateLoadingDialog(false)
            mAdapter.data = it!!
            mAdapter.notifyDataSetChanged()
        })

        fab_detail.setOnClickListener({
            val intent = Intent(this, AddCardInfoActivity::class.java)
            startActivity(intent)
        })

        viewModel.getAnimationDetailModelList(model.type!!, model.cardType!!)
    }

    private var listener = object : AppBarStateChangeListener() {
        override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
            when (state) {
                //展开状态
                State.EXPANDED -> ScreenUtils.setWindowStatusBarColor(
                    this@AnimationDetailActivity,
                    R.color.colorTranslucent
                )
                //折叠状态
                State.COLLAPSED -> ScreenUtils.setWindowStatusBarColor(
                    this@AnimationDetailActivity,
                    R.color.colorBase
                )
                //中间状态
                else -> {
                }
            }
        }
    }

}
