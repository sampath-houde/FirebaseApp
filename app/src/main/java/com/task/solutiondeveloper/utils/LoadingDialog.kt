package com.task.solutiondeveloper.utils

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.task.solutiondeveloper.R

class LoadingDialog(val mAvtivity: Activity) {


    private var builder: AlertDialog.Builder = AlertDialog.Builder(mAvtivity)
    private var alertDialog: AlertDialog = builder.create()


    fun startLoading() {

        val inflater = mAvtivity.layoutInflater

        builder.setView(inflater.inflate(R.layout.loading_dialog, null))

        builder.setCancelable(false)

        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    fun isLoading() : Boolean {
        return alertDialog.isShowing
    }

    fun stopLoading() {
        alertDialog.dismiss()
    }
}