package com.revolut.converter.ui.exchange

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.revolut.converter.R
import com.revolut.converter.core.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_exchange.*

class ExchangeFragment: BaseFragment() {

    override var layoutId = R.layout.fragment_exchange

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}