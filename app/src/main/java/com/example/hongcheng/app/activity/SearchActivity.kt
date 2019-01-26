package com.example.hongcheng.app.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.view.View
import com.example.hongcheng.app.R
import com.example.hongcheng.app.viewModel.CardViewModel
import com.example.hongcheng.app.viewModel.CardViewModelFactory
import com.example.hongcheng.common.base.BaseActivity
import com.example.hongcheng.common.util.ScreenUtils
import com.example.hongcheng.common.view.citylist.CityListSelectView
import com.example.hongcheng.common.view.searchview.ICallBack
import com.example.hongcheng.common.view.searchview.SearchView
import com.example.hongcheng.data.room.entity.CardDetailInfo

class SearchActivity : BaseActivity() {
    private lateinit var svTitle : SearchView
    private lateinit var cs : CityListSelectView

    override fun getTitleLayoutResId(): Int {
        return R.layout.layout_search_title
    }

    override fun getMessageLayoutResId(): Int {
        return R.layout.layout_search_list
    }

    override fun getBodyLayoutResId(): Int {
        return R.layout.body_search
    }

    override fun initTitleView(view: View) {
        val factory = CardViewModelFactory(this)
        val viewModel: CardViewModel = ViewModelProviders.of(this, factory).get(CardViewModel::class.java)

        viewModel.list.observe(this, Observer<ArrayList<CardDetailInfo>> {
            operateLoadingDialog(false)
        })

        val toolbar = view.findViewById<Toolbar>(R.id.tb_search)
        setSupportActionBar(toolbar)
        ScreenUtils.setWindowStatusBarColor(this, resources.getColor(R.color.colorBase))
        toolbar.setNavigationIcon(R.drawable.back)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        val actionBar : ActionBar? = supportActionBar
        actionBar?.title = ""

        svTitle = view.findViewById(R.id.sv_title)

        svTitle.setOnSearchListener(object : ICallBack {
            override fun queryData(string: String?) {
                cs.setSearchSource(string)
            }

            override fun onFocusChange(hasFocus: Boolean) {
            }
        })
    }

    override fun initMessageView(view: View) {
        cs = view.findViewById(R.id.cs_city_search_list)
        cs.setCurrentCity("武汉")
    }

    override fun initBodyView(view: View) {

    }
}