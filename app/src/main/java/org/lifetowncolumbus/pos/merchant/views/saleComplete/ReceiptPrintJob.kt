package org.lifetowncolumbus.pos.merchant.views.saleComplete

import org.lifetowncolumbus.pos.merchant.viewModels.CurrentSale
import org.lifetowncolumbus.pos.printing.PrintJob
import org.lifetowncolumbus.pos.printing.PrinterWrapper
import org.lifetowncolumbus.pos.printing.serializeReceiptItem
import org.lifetowncolumbus.pos.toCurrencyString

class ReceiptPrintJob(currentSale: CurrentSale) : PrintJob {
    private val total = currentSale.total.negate().toCurrencyString()
    private val items = currentSale.items.value?.map {
        serializeReceiptItem(it.name, it.value, 40)
    }

    override fun execute(printer: PrinterWrapper) {
        if (printer.status().online == PrinterWrapper.TRUE) {
            printer.let {
                it.beginTransaction()
                it.addTextAlign(PrinterWrapper.ALIGN_CENTER)
                printContactInfo(it)
                it.addFeedLine(2)
                items?.forEach { item -> it.addText(item) }
                it.addFeedLine(2)
                it.addTextSize(2, 2)
                it.addText("Change Due:\t$total\n")
                it.addTextSize(1, 1)
                it.addFeedLine(4)
                it.addCut(PrinterWrapper.CUT_FEED)
                it.addPulse(PrinterWrapper.DRAWER_2PIN, PrinterWrapper.PULSE_200)
                it.sendData(PrinterWrapper.PARAM_DEFAULT)
                it.clearCommandBuffer()
                it.endTransaction()
            }
        }
    }

    private fun printContactInfo(printer: PrinterWrapper) {
        printer.addText("LifeTown Columbus\n")
        printer.addText("6220 E. Dublin Granville Rd.\n")
        printer.addText("New Albany, OH 43054\n")
        printer.addText("(614) 939-0765\n")
    }
}

