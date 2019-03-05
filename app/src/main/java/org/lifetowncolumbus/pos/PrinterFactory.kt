package org.lifetowncolumbus.pos

import org.lifetowncolumbus.pos.merchant.POSActivity

fun createPrinter(deviceType: Int, activity: POSActivity) : PrinterWrapper {
    return PrinterWrapper(deviceType, activity)
}
