package org.lifetowncolumbus.pos.printing

interface PrintJob {
    fun execute(printer: PrinterWrapper)
}