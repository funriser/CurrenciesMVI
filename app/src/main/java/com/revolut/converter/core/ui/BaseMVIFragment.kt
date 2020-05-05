package com.revolut.converter.core.ui

import com.revolut.converter.core.mvi.SingleAction
import com.revolut.converter.core.mvi.ViewState

abstract class BaseMVIFragment<S: ViewState, SA: SingleAction>: BaseFragment () {

    private var isCold: Boolean = true

    override fun onStart() {
        super.onStart()
        getViewModel().onAttach(isCold)
        observeViewState()
        observeSingleActions()
        isCold = false
    }

    protected open fun observeViewState() {
        getViewModel().observeViewState()
            .subscribeTillStop(::renderUi)
    }

    private fun observeSingleActions() {
        getViewModel().observeSingleActions()
            .subscribeTillStop(::consumeSingleAction)
    }

    abstract fun renderUi(viewState: S)

    abstract fun consumeSingleAction(action: SA)

    override fun onStop() {
        getViewModel().onDetach()
        super.onStop()
    }

    abstract fun getViewModel(): MVIViewModel<*, SA, S>

}