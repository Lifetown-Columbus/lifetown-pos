package org.lifetowncolumbus.pos

import com.epson.epos2.printer.Printer
import org.lifetowncolumbus.pos.merchant.POSActivity

class PrinterWrapper(deviceType: Int, activity: POSActivity){
    private val printer = Printer(deviceType, Printer.MODEL_ANK, activity)

    fun connect(target: String) {
        printer.connect(target, Printer.PARAM_DEFAULT)
    }

}
