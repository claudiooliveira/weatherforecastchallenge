package com.example.weatherforecastchallenge.utils

import android.app.Activity
import android.app.Dialog
import android.view.Window
import com.example.weatherforecastchallenge.R

class LoadingDialog {

    var activity : Activity

    constructor(activity : Activity) {
        this.activity = activity
    }

    lateinit var dialog: Dialog

    fun showDialog() {

        dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_loading_layout)
        dialog.show()

    }

    fun hideDialog() {
        dialog.dismiss()
    }

}