package com.example.hongcheng.app.adapter.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.hongcheng.app.R
import com.example.hongcheng.common.view.ImageViewWithDelete

class SelectImgViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    val iwd : ImageViewWithDelete = itemView.findViewById(R.id.iwd_select_img)
}