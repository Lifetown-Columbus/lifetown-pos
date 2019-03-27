package org.lifetowncolumbus.pos.magneticCards

import android.view.KeyEvent

class SwipeEventHandler(val goodSwipe: (data: String) -> Unit){

    private var data: String = ""
        set(value) {
            val tracks = value.split(';')
            if (tracks.size > 1 && tracks[1].endsWith('?')) { // placeholder for LRC check
                goodSwipe(value)
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