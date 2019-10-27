package org.lifetowncolumbus.pos

import android.app.Application
import android.content.Context

class POSApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: POSApplication? = null

        fun applicationContext() : Context? {
            return instance?.applicationContext
        }
    }
}