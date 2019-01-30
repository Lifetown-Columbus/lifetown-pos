package org.lifetowncolumbus.pos

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button

abstract class KeyboardHelpers {
    companion object {
        fun closeKeyboard(context: Context, view: View) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun clickButtonWhenKeyboardDone(actionId: Int, button: Button): Boolean {
            return when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    button.performClick()
                    true
                }
                else -> false
            }
        }
    }
}
