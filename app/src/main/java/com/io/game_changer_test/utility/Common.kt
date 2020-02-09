package com.io.game_changer_test.utility

import android.content.Context
import android.widget.Toast
import com.github.loadingview.LoadingDialog


class Common {

    companion object commonFunction{
        var loadingDialog: LoadingDialog? = null

        fun showShortToast(str: String, context: Context) {
            Toast.makeText(context, "" + str, Toast.LENGTH_SHORT).show()
        }

        fun showLongToast(str: String, context: Context) {
            Toast.makeText(context, "" + str, Toast.LENGTH_LONG).show()
        }


    }
}