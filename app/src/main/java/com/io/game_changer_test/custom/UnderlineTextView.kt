package com.io.game_changer_test.custom

import android.content.Context
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.widget.TextView

/**
 * Created with IntelliJ IDEA.
 * Created by Utkarsh Raj on 5/08/19.
 * Date: 5/08/19
 * Time: 1:10 AM
 */
class UnderlineTextView : TextView {
    private var m_modifyingText = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //Do nothing here... we don't care
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //Do nothing here... we don't care
            }

            override fun afterTextChanged(s: Editable) {
                if (m_modifyingText)
                    return

                underlineText()
            }
        })

        underlineText()
    }

    private fun underlineText() {
        if (m_modifyingText)
            return

        m_modifyingText = true

        val content = SpannableString(text)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        text = content

        m_modifyingText = false
    }
}