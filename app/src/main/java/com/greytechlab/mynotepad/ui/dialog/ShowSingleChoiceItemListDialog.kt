package com.greytechlab.mynotepad.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.greytechlab.mynotepad.common.OnDialogListItemClickListener

class ShowSingleChoiceItemListDialog(val title: String,
                                     private val options: Array<String>,
                                     private val checkedItem: Int) : DialogFragment() {

    private var listener: OnDialogListItemClickListener? = null




    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder =
            AlertDialog.Builder(requireActivity())

        builder.setTitle(title)
//            .setItems(options) { dialog, which -> listener!!.onItemListener(which) }
            .setSingleChoiceItems(options, checkedItem
            ) { _, which ->

                listener!!.onItemListener(which)

            }
        return builder.create()
    }

    fun setOnItemClickListener(listener: OnDialogListItemClickListener?) {
        this.listener = listener
    }
}