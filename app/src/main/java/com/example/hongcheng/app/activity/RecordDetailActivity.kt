package com.example.hongcheng.app.activity

import android.view.View
import com.example.hongcheng.app.R
import com.example.hongcheng.common.base.CommonActivity

class RecordDetailActivity : CommonActivity() {
    override fun isNeedShowBack(): Boolean {
        return true
    }

    override fun setToolbarTitle(): Int {
        return R.string.add_card
    }

    override fun getBodyLayoutResId(): Int {
        return 0
    }

    override fun initBodyView(view: View) {
    }
}