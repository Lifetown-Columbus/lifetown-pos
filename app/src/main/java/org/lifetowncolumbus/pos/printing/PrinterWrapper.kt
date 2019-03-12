package org.lifetowncolumbus.pos.printing

import com.epson.epos2.printer.Printer
import com.epson.epos2.printer.PrinterStatusInfo
import org.lifetowncolumbus.pos.merchant.POSActivity

class PrinterWrapper(deviceType: Int, activity: POSActivity){
    private val printer = Printer(deviceType, Printer.MODEL_ANK, activity)

    fun connect(target: String) {
        printer.connect(target, Printer.PARAM_DEFAULT)
    }

    fun status() : PrinterStatusInfo {
        return printer.status
    }

    fun beginTransaction() {
        printer.beginTransaction()
    }

    fun addTextAlign(allign: Int) {
        printer.addTextAlign(allign)
    }

    fun addText(text: String) {
        printer.addText(text)
    }

    fun addFeedLine(lines: Int) {
        printer.addFeedLine(lines)
    }

    fun addTextSize(width: Int, height: Int) {
        printer.addTextSize(width, height)
    }

    fun addCut(type: Int) {
        printer.addCut(type)
    }

    fun addPulse(drawer: Int, time: Int) {
        printer.addPulse(drawer, time)
    }

    fun sendData(timeout: Int) {
        printer.sendData(timeout)
    }

    fun clearCommandBuffer() {
        printer.clearCommandBuffer()
    }

    fun endTransaction() {
        printer.endTransaction()
    }

}
