package com.example.hongcheng.app.viewModel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.hongcheng.common.base.CommonUI

class CardViewModelFactory(private val ui: CommonUI) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CardViewModel(ui) as T
    }
}