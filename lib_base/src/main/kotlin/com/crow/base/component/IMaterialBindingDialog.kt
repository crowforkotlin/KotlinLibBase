package com.crow.base.component

import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * ● IBaseAppDialog
 *
 * ● 2024/4/20 21:02
 * @author crowforkotlin
 * @formatter:on
 */
interface IMaterialBindingDialog<VB: ViewBinding?> {
    fun getBinding(layoutInflater: LayoutInflater?) : VB?
    fun onDialogConfig(dialog: MaterialAlertDialogBuilder, binding: VB?)
    fun onDialog(dialog: AlertDialog)
}