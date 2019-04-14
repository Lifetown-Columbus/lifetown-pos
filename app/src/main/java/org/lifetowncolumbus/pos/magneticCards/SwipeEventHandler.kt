package org.lifetowncolumbus.pos.magneticCards

import android.view.KeyEvent
import us.fatehi.creditcardnumber.BankCard
import us.fatehi.magnetictrack.bankcard.BankCardMagneticTrack

class SwipeEventHandler(val goodSwipe: (data: BankCard) -> Unit){

    private var data: String = ""
        set(value) {
            val card = BankCardMagneticTrack.from(value).toBankCard()
            if (card.hasPrimaryAccountNumber()) {
                goodSwipe(card)
            }
            else {
                field = value
            }
        }

    fun dispatchKeyEvent(event: KeyEvent?) : Boolean {
        if(event?.action == KeyEvent.ACTION_DOWN){
            val c = event.unicodeChar.toChar()
            if (c == '%') { data = "" }
            data += Character.toString(c)
        }
        return true
    }
}