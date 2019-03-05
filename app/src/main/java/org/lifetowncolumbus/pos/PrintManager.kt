package org.lifetowncolumbus.pos

import com.epson.epos2.discovery.Discovery
import com.epson.epos2.discovery.FilterOption
import org.lifetowncolumbus.pos.merchant.POSActivity


class PrintManager (
    private val activity: POSActivity,
    private val discoveryWrapper: DiscoveryWrapper = DiscoveryWrapper()) {

    fun stop() {
        discoveryWrapper.stop()
    }

    fun start() {
        discoveryWrapper.start(activity, usbPrinter()) {
            PrinterFactory.create(it.deviceType, activity).connect(it.target)
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