package com.example.androidcleanarchitecture.utils

import android.app.AlertDialog
import com.example.androidcleanarchitecture.R
import com.example.androidcleanarchitecture.ui.BaseFragment


fun BaseFragment.progressOn() {
    getMain()?.showProgress()
}

fun BaseFragment.progressOff() {
    getMain()?.hideProgress()
}

fun BaseFragment.showError(errorMessage: String) {
    context?.apply {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.attention_text))
            .setMessage(errorMessage)
            .setPositiveButton("OK", null)
            .show()
    }
}