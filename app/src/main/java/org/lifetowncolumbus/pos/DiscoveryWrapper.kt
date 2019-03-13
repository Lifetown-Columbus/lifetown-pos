package org.lifetowncolumbus.pos

import android.content.Context
import com.epson.epos2.discovery.DeviceInfo
import com.epson.epos2.discovery.Discovery
import com.epson.epos2.discovery.FilterOption

class DiscoveryWrapper {

    fun start(context: Context, filterOption: FilterOption, callback: (DeviceInfo) -> Unit) {
        Discovery.start(context, filterOption, callback)
    }

    fun stop() {
        Discovery.stop()
    }



}
