package com.revolut.converter.core.ui

import com.revolut.converter.core.mvi.ViewState

abstract class BaseMVIFragment<S: ViewState>: BaseFragment () {

    private var isCold: Boolean = true

    override fun onStart() {
        super.onStart()
        getViewModel().onAttach(isCold)
        observeViewState()
        isCold = false
    }

    protected open fun observeViewState() {
        getViewModel().observeViewState()
            .subscribeTillStop(::renderUi)
    }

    abstract fun renderUi(viewState: S)

    override fun onStop() {
        getViewModel().onDetach()
        super.onStop()
    }

    abstract fun getViewModel(): MVIViewModel<*, S>

}