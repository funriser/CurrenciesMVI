package com.revolut.converter.core.ui

abstract class BaseMVIFragment: BaseFragment () {

    private var isCold: Boolean = true

    override fun onStart() {
        super.onStart()
        getViewModel().onAttach(isCold)
        isCold = false
    }

    override fun onStop() {
        getViewModel().onDetach()
        super.onStop()
    }

    abstract fun getViewModel(): MVIViewModel<*, *>

}