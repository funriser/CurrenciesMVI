package com.funrisestudio.converter.core.navigation

import androidx.navigation.NavController
import com.funrisestudio.converter.core.mvi.Action

abstract class Navigator<NavAction: Action> {

    abstract fun handleAction(controller: NavController, action: NavAction)

    fun goBack(controller: NavController) {
        controller.popBackStack()
    }

}