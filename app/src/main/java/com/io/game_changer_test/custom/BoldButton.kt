package com.io.game_changer_test.custom

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.Button

/**
 * Created by Utkarsh Raj on 5/08/19.
 */

class BoldButton : Button {
    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    internal fun init(context: Context) {
        val font = Typeface.createFromAsset(getContext().assets, "fonts/helvetica_bold.ttf")
        typeface = font
    }


}