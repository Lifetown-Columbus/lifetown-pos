package org.lifetowncolumbus.pos.printing

import org.lifetowncolumbus.pos.merchant.POSActivity
import org.lifetowncolumbus.pos.printing.PrinterWrapper

fun createPrinter(activity: POSActivity) : PrinterWrapper {
    return PrinterWrapper(activity)
}
