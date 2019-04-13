package org.lifetowncolumbus.pos.magneticCards

import android.view.KeyEvent
import us.fatehi.magnetictrack.bankcard.BankCardMagneticTrack

class SwipeEventHandler(val goodSwipe: (data: BankCardMagneticTrack) -> Unit){

    private var data: String = ""
        set(value) {
            val tracks = value.split(';')
            if (tracks.size > 1 && tracks[1].endsWith('?')) { // placeholder for LRC check
                val tracks = BankCardMagneticTrack.from(value)
                goodSwipe(tracks)
            }
            field = value
        }

    fun dispatchKeyEvent(event: KeyEvent?) : Boolean {
        if(event?.action == KeyEvent.ACTION_DOWN){
            val c = event.unicodeChar.toChar()
            data += Character.toString(c)
        }
        return true
    }
}