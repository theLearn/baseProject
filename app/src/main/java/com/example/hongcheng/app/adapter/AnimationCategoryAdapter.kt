package com.example.hongcheng.app.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.hongcheng.app.R
import com.example.hongcheng.app.adapter.models.AnimationModel
import com.example.hongcheng.app.adapter.viewholders.AnimationCategoryViewHolder
import com.example.hongcheng.app.databinding.ItemSmartCardBinding
import com.example.hongcheng.common.base.BaseListAdapter

/**
 * Created by hongcheng on 17/6/1.
 */
class AnimationCategoryAdapter(private var mContext: Context?) :
    BaseListAdapter<AnimationModel, AnimationCategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimationCategoryViewHolder {
        val binding: ItemSmartCardBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_smart_card, parent, false);
        val holder = AnimationCategoryViewHolder(binding.root)
        holder.binding = binding
        return holder
    }


    override fun onBindViewHolder(holder: AnimationCategoryViewHolder, position: Int) {
        val model: AnimationModel = data[position]
        holder.binding?.viewModel = model
        holder.binding?.executePendingBindings()
    }
}