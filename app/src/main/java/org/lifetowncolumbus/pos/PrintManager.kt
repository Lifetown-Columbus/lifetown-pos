package org.lifetowncolumbus.pos

import android.util.Log
import com.epson.epos2.Epos2Exception
import com.epson.epos2.discovery.Discovery
import com.epson.epos2.discovery.FilterOption
import org.lifetowncolumbus.pos.merchant.POSActivity


class PrintManager (
    private val activity: POSActivity,
    private val discoveryWrapper: DiscoveryWrapper = DiscoveryWrapper()) {
    companion object {
        lateinit var printer: PrinterWrapper
    }

    fun stop() {
        discoveryWrapper.stop()
    }

    fun start() {
        tryOrLog("Printer discovery failed") {
            discoveryWrapper.start(activity, usbPrinter()) {
                tryOrLog("Printer connection failed") {
                    printer = createPrinter(it.deviceType, activity)
                    printer.connect(it.target)
                }
            }
        }
    }

    private inline fun tryOrLog(
        msg: String,
        block: () -> Unit) {
       try {
           block.invoke()
       } catch (e: Epos2Exception) {
           Log.e(this::class.qualifiedName, msg)
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