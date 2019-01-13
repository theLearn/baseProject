package com.example.hongcheng.app.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.database.Cursor
import android.support.v4.widget.CursorAdapter
import android.support.v4.widget.SimpleCursorAdapter
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import com.example.hongcheng.app.R
import com.example.hongcheng.app.viewModel.CardViewModel
import com.example.hongcheng.app.viewModel.CardViewModelFactory
import com.example.hongcheng.common.base.BaseActivity
import com.example.hongcheng.common.util.ScreenUtils
import com.example.hongcheng.common.view.searchview.ICallBack
import com.example.hongcheng.common.view.searchview.SearchView
import com.example.hongcheng.data.room.entity.CardDetailInfo

class SearchActivity : BaseActivity() {
    private lateinit var svTitle : SearchView
    private lateinit var tvClear : View
    private lateinit var searchList : ListView

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

        svTitle.setOnClickSearch(object : ICallBack {
            override fun searchAction(string: String?) {
                viewModel.getAll()
            }

            override fun queryResult(cursor: Cursor?) {
                if(cursor == null) return
                // 1. 创建adapter适配器对象 & 装入模糊搜索的结果
                val adapter = SimpleCursorAdapter(this@SearchActivity, R.layout.item_search_content, cursor, arrayOf("name"),
                        intArrayOf(R.id.tv_search_content), CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
                // 2. 设置适配器
                searchList.adapter = adapter
                adapter.notifyDataSetChanged()

                // 数据库中有搜索记录时，显示 "删除搜索记录"按钮
                setMessageViewVisible(cursor.count != 0)
            }
        })
    }

    override fun initMessageView(view: View) {
        tvClear = view.findViewById(R.id.tv_clear)
        searchList = view.findViewById(R.id.lv_search_list)
        /**
         * "清空搜索历史"按钮
         */
        tvClear.setOnClickListener({
            // 清空数据库->>关注2
            svTitle.deleteData()
            // 模糊搜索空字符 = 显示所有的搜索历史（此时是没有搜索记录的）
            svTitle.queryData("")
        })

        /**
         * 搜索记录列表（ListView）监听
         * 即当用户点击搜索历史里的字段后,会直接将结果当作搜索字段进行搜索
         */
        searchList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            // 获取用户点击列表里的文字,并自动填充到搜索框内
            val textView = view.findViewById<View>(R.id.tv_search_content) as TextView
            val name = textView.text.toString()
            svTitle.setText(name)
            setMessageViewVisible(false)
        }

        view.setOnClickListener({
            setMessageViewVisible(false)
        })

        setMessageViewVisible(false)
    }

    override fun initBodyView(view: View) {

    }
}