package org.lifetowncolumbus.pos.printing

import android.util.Log
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import com.epson.epos2.Epos2Exception
import com.epson.epos2.discovery.Discovery
import com.epson.epos2.discovery.FilterOption
import org.lifetowncolumbus.pos.DiscoveryWrapper
import org.lifetowncolumbus.pos.POSApplication
import org.lifetowncolumbus.pos.merchant.POSActivity


class PrintManager (
    private val activity: POSActivity,
    private val discoveryWrapper: DiscoveryWrapper = DiscoveryWrapper()
) {

    companion object {
        lateinit var printer: PrinterWrapper
        fun print(printJob: PrintJob) {
            if (checkPrinter()) {
                tryOrLog("PrintJob failed") {
                    printJob.execute(printer)
                }
            }
        }

        fun checkPrinter() : Boolean {
            return if(this::printer.isInitialized && !printerOnline()) {
                val status = printer.status()
                when {
                    status.connection == PrinterWrapper.FALSE -> toast("Printer is not connected! Turn off printer, check cable, turn printer back on.")
                    status.coverOpen == PrinterWrapper.TRUE -> toast("Printer cover is open")
                    status.paper == PrinterWrapper.PAPER_EMPTY -> toast("Printer is out of paper")
                    else -> toast("Printer is offline! Turn off printer, check cable, turn printer back on.")
                }
                false
            } else if(!this::printer.isInitialized) {
                toast("Printer is offline! Turn off printer, check cable, turn printer back on.")
                false
            } else {
                true
            }
        }

        private fun printerOnline() : Boolean {
           return printer.status().online == PrinterWrapper.TRUE
        }
        private fun toast(msg: CharSequence) {
            Toast.makeText(POSApplication.applicationContext(), msg, Toast.LENGTH_LONG).show()
        }

        private inline fun tryOrLog(
            msg: String,
            block: () -> Unit) {
            try {
                block.invoke()
            } catch (e: Epos2Exception) {
                Crashlytics.log(Log.ERROR, this::class.qualifiedName, msg)
            }
        }
    }

    fun stop() {
        discoveryWrapper.stop()
    }

    fun start() {
        tryOrLog("Printer discovery failed") {
            discoveryWrapper.start(activity, usbPrinter()) {
                tryOrLog("Printer connection failed") {
                    printer =
                        createPrinter(it.deviceType, activity)
                    printer.connect(it.target)
                }
            }
        }
    }

    private fun usbPrinter(): FilterOption {
        return FilterOption().apply {
            portType = Discovery.PORTTYPE_USB
            deviceModel = Discovery.MODEL_ALL
            deviceType = Discovery.TYPE_PRINTER
        }
    }
}