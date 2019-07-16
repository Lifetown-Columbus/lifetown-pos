package org.lifetowncolumbus.pos.magneticCards

import android.view.KeyEvent

class SwipeEventHandler(val goodSwipe: (data: BankCard) -> Unit){

    private var data: String = ""
        set(value) {
            val card = BankCard(value)
            if (card.isValid) {
                goodSwipe(card)
            }
            else {
                field = value
            }
        }

    fun dispatchKeyEvent(event: KeyEvent?) : Boolean {
        if(event?.action == KeyEvent.ACTION_DOWN){
            val c = event.unicodeChar.toChar()
            if (c == ';') { data = "" }
            data += c.toString()
        }
        return true
    }
}