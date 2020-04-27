package com.revolut.converter.ui.exchange

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.revolut.converter.R
import com.revolut.converter.core.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_exchange.*

class ExchangeFragment: BaseFragment() {

    override var layoutId = R.layout.fragment_exchange

    private val args: ExchangeFragmentArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        tvStub.text = "From: ${args.exchangeState.from}\nTo: ${args.exchangeState.to}"
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}