package com.example.hongcheng.app.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Color
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import com.example.hongcheng.app.R
import com.example.hongcheng.app.activity.AnimationDetailActivity
import com.example.hongcheng.app.adapter.AnimationCategoryAdapter
import com.example.hongcheng.app.adapter.models.AnimationModel
import com.example.hongcheng.app.viewModel.CardViewModel
import com.example.hongcheng.app.viewModel.CardViewModelFactory
import com.example.hongcheng.common.base.BaseListAdapter
import com.example.hongcheng.common.base.BasicFragment
import com.example.hongcheng.common.util.SafeIntentUtils
import kotlinx.android.synthetic.main.fragment_smart_cards.*

/**
 * Created by hongcheng on 17/5/30.
 */
class AnimationCardFragment : BasicFragment(), SwipeRefreshLayout.OnRefreshListener {

    private var cardType: String = "0"
    private lateinit var mAdapter: AnimationCategoryAdapter
    private lateinit var viewModel: CardViewModel

    override fun getLayoutResId(): Int = R.layout.fragment_smart_cards

    override fun initView() {
        arguments?.let { cardType = it.getString("cardType", "0") }
        mAdapter = AnimationCategoryAdapter(mContext)

        srl_fragment_cards.setProgressBackgroundColorSchemeColor(Color.WHITE)
        srl_fragment_cards.setColorSchemeResources(R.color.colorBase)
        srl_fragment_cards.setProgressViewOffset(
            false, 0, TypedValue
                .applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 30f, resources
                        .displayMetrics
                ).toInt()
        )
        srl_fragment_cards.setOnRefreshListener(this)

        rv_smart_cards.layoutManager = LinearLayoutManager(rv_smart_cards.context)
        rv_smart_cards.itemAnimator = DefaultItemAnimator()
        rv_smart_cards.adapter = mAdapter

        mAdapter.onItemClickListener = object : BaseListAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(mContext, AnimationDetailActivity::class.java)
                intent.putExtra(SafeIntentUtils.INTENT_MODEL, mAdapter.getItem(position))
                mContext.startActivity(intent)
            }

            override fun onItemLongClick(position: Int) {
            }
        }

        val factory = CardViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, factory).get(CardViewModel::class.java)
        viewModel.liveList.observe(this, Observer<ArrayList<AnimationModel>> {
            srl_fragment_cards.isRefreshing = false
            mAdapter.data = it!!
            mAdapter.notifyDataSetChanged()
        })

        srl_fragment_cards.post {
            srl_fragment_cards.isRefreshing = true
            viewModel.getAnimationModelList(cardType)
        }
    }

    override fun onRefresh() {
        viewModel.getAnimationModelList(cardType)
    }
}