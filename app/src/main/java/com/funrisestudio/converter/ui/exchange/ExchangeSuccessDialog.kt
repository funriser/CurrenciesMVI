package com.funrisestudio.converter.ui.exchange

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.funrisestudio.converter.R
import kotlinx.android.synthetic.main.dg_exchange_success.*

class ExchangeSuccessDialog: BottomSheetDialogFragment() {

    private val behavior: BottomSheetBehavior<*>?
        get() = (dialog as? BottomSheetDialog)?.behavior

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : BottomSheetDialog(requireContext(), theme) {
            init {
                setCanceledOnTouchOutside(false)
            }
            override fun onBackPressed() {
                goBack()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dg_exchange_success, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnToRates.setOnClickListener {
            goBack()
        }
    }

    private fun goBack() {
        findNavController().popBackStack(R.id.exchangeFragment, true)
    }

    override fun onStart() {
        super.onStart()
        behavior?.isDraggable = false
    }

}