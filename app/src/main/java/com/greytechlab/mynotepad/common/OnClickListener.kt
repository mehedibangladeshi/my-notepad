package com.greytechlab.mynotepad.common

import android.view.View

interface OnClickListener {
    fun onItemClick(position: Int, view: View)
}