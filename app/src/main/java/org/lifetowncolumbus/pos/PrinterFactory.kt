package org.lifetowncolumbus.pos

import org.lifetowncolumbus.pos.merchant.POSActivity

class PrinterFactory {
    companion object {

        fun create(deviceType: Int, activity: POSActivity) : PrinterWrapper {
            return PrinterWrapper(deviceType, activity)
        }
    }
}