package org.lifetowncolumbus.pos.printing

import com.epson.epos2.printer.Printer
import com.epson.epos2.printer.PrinterStatusInfo
import org.lifetowncolumbus.pos.merchant.POSActivity

class PrinterWrapper(activity: POSActivity){
    private val printer = Printer(Printer.TM_T20, Printer.MODEL_ANK, activity)
    companion object {
        const val ALIGN_CENTER = Printer.ALIGN_CENTER
        const val CUT_FEED = Printer.CUT_FEED
        const val DRAWER_2PIN = Printer.DRAWER_2PIN
        const val PULSE_200 = Printer.PULSE_200
        const val PARAM_DEFAULT = Printer.PARAM_DEFAULT
        const val TRUE = Printer.TRUE
        const val FALSE = Printer.FALSE
        const val PAPER_EMPTY = Printer.PAPER_EMPTY
    }


    fun connect(target: String) {
        printer.connect(target, Printer.PARAM_DEFAULT)
    }

    fun status() : PrinterStatusInfo {
        return printer.status
    }

    fun beginTransaction() {
        printer.beginTransaction()
    }

    fun addTextAlign(align: Int) {
        printer.addTextAlign(align)
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
