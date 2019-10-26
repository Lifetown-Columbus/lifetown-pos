package org.lifetowncolumbus.pos.printing

class OpenDrawerPrintJob : PrintJob {
    override fun execute(printer: PrinterWrapper) {
        if(printer.status().connection == PrinterWrapper.TRUE) {
            printer.beginTransaction()
            printer.addPulse(PrinterWrapper.DRAWER_2PIN, PrinterWrapper.PULSE_200)
            printer.sendData(PrinterWrapper.PARAM_DEFAULT)
            printer.clearCommandBuffer()
            printer.endTransaction()
        }
    }
}