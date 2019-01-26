package com.example.hongcheng.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.hongcheng.app.R
import com.example.hongcheng.app.adapter.viewholders.SelectImgViewHolder
import com.example.hongcheng.common.base.BaseListAdapter
import com.example.hongcheng.common.view.ImageViewWithDelete

class SelectImgAdapter : BaseListAdapter<String, SelectImgViewHolder>()  {
    companion object {
        const val MAX : Int = 5
    }

    fun setSource(source : List<String>) {
        data.clear()
        data.addAll(source)
        if(source.size < MAX) {
            data.add("")
        }

        notifyDataSetChanged()
    }

    var listener : ImageViewWithDelete.OnImgDeleteListener? = null

    override fun onBaseCreateViewHolder(parent: ViewGroup, viewType: Int): SelectImgViewHolder {
        return SelectImgViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_select_img, parent, false))
    }

    override fun onBaseBindViewHolder(holder: SelectImgViewHolder, position: Int) {
        holder.iwd.setImageModule(data[position])
        holder.iwd.deleteView.setOnClickListener({
            listener?.onImgDelete(position)
        })
    }
}