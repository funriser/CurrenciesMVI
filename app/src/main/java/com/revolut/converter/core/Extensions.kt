package com.revolut.converter.core

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*


inline fun <reified T : ViewModel> AppCompatActivity.viewModel(factory: ViewModelProvider.Factory, body: T.() -> Unit): T {
    val vm = ViewModelProvider(this, factory)[T::class.java]
    vm.body()
    return vm
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) =
    liveData.observe(this, Observer(body))

fun ViewGroup.inflate (@LayoutRes layoutId: Int, root: ViewGroup = this, attachToRoot: Boolean = false): View
        = LayoutInflater.from(context).inflate(layoutId, root, attachToRoot)

fun View.visibleIf(condition: Boolean) {
    this.visibility = if (condition) {
        View.VISIBLE
    } else {
        View.GONE
    }
}