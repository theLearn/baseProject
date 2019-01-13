package com.example.hongcheng.app.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.widget.*
import android.view.View
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.example.hongcheng.app.R
import com.example.hongcheng.app.adapter.SelectImgAdapter
import com.example.hongcheng.app.viewModel.CardViewModel
import com.example.hongcheng.app.viewModel.CardViewModelFactory
import com.example.hongcheng.common.base.BaseListAdapter.OnItemClickListener
import com.example.hongcheng.common.base.CommonActivity
import com.example.hongcheng.common.util.DateUtils
import com.example.hongcheng.common.util.ScreenUtils
import com.example.hongcheng.common.util.StringUtils
import com.example.hongcheng.common.view.DividerItemDecoration
import com.example.hongcheng.common.view.ImageViewWithDelete
import com.example.hongcheng.data.room.entity.CardInfoEntity
import kotlinx.android.synthetic.main.body_add_card_info.*
import me.iwf.photopicker.PhotoPicker
import me.iwf.photopicker.PhotoPreview


class AddCardInfoActivity : CommonActivity(), ImageViewWithDelete.OnImgDeleteListener, OnItemClickListener {

    private val mAdapter = SelectImgAdapter()
    private val selectedPhotos = arrayListOf<String>()

    override fun setToolbarTitle(): Int {
        return R.string.add_card
    }

    override fun isNeedShowBack(): Boolean {
        return true
    }

    override fun getMessageLayoutResId(): Int {
        return 0
    }

    override fun getBodyLayoutResId(): Int {
        return R.layout.body_add_card_info
    }

    override fun initBodyView(view: View) {
        val recyclerView = findViewById<RecyclerView>(R.id.rv_select_img_info)
        recyclerView.layoutManager = StaggeredGridLayoutManager(5, OrientationHelper.VERTICAL)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(DividerItemDecoration(
                this, LinearLayoutManager.VERTICAL, ScreenUtils.dp2px(this, 10f), resources.getColor(R.color.colorTranslucent)))

        mAdapter.listener = this
        recyclerView.adapter = mAdapter
        mAdapter.setData(selectedPhotos)

        mAdapter.onItemClickListener = this

        val factory = CardViewModelFactory(this)
        val viewModel: CardViewModel = ViewModelProviders.of(this, factory).get(CardViewModel::class.java)

        ll_card_info_date_select.setOnClickListener({
            val pvTime = TimePickerBuilder(this@AddCardInfoActivity, OnTimeSelectListener { date, v -> tv_card_info_date.text = DateUtils.getFormatTime(DateUtils.DATE_FULL_STR, date)}).build()
            pvTime.show()
        })

        bt_save_card_info.setOnClickListener({
            val cardType = ns_card_type.text.toString()
            val type = ns_type.text.toString()
            val cardName = et_add_card_info_name.text.toString()
            val cardDes = et_add_card_info_content.text.toString()
            val cardDate = tv_card_info_date.text.toString()

            if(StringUtils.isEmpty(cardName) || StringUtils.isEmpty(cardDes) || StringUtils.isEmpty(type) || StringUtils.isEmpty(cardType) || StringUtils.isEmpty(cardDate)) return@setOnClickListener

            val entity = CardInfoEntity(
                    type = type,
                    cardType = cardType,
                    name = cardName,
                    description = cardDes,
                    date = cardDate,
                    imageUrl = if (selectedPhotos.size > 0) selectedPhotos[0] else ""
            )

            viewModel.insertCardInfo(arrayListOf(entity))
        })

        viewModel.typeList.observe(this, Observer {
            ns_type.attachDataSource<String>(it)
        })

        viewModel.cardTypeList.observe(this, Observer {
            ns_card_type.attachDataSource<String>(it)
        })
        viewModel.getAllType()
    }

    override fun onItemClick(position: Int) {
        if (StringUtils.isEmpty(mAdapter.getItem(position))) {
            PhotoPicker.builder()
                    .setPhotoCount(SelectImgAdapter.MAX)
                    .setShowCamera(true)
                    .setPreviewEnabled(false)
                    .setSelected(selectedPhotos)
                    .start(this@AddCardInfoActivity)
        } else {
            PhotoPreview.builder()
                    .setPhotos(selectedPhotos)
                    .setCurrentItem(position)
                    .start(this@AddCardInfoActivity)
        }
    }

    override fun onItemLongClick(position: Int) {
    }

    override fun onImgDelete(position: Int) {
        selectedPhotos.removeAt(position)
        mAdapter.setData(selectedPhotos)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {

            var photos: List<String>? = null
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS)
            }
            selectedPhotos.clear()

            if (photos != null) {
                selectedPhotos.addAll(photos)
            }

            mAdapter.setData(selectedPhotos)
        }
    }

}