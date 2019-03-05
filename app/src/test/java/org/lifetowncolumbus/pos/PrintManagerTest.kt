package org.lifetowncolumbus.pos

import com.epson.epos2.discovery.DeviceInfo
import com.epson.epos2.discovery.Discovery
import com.epson.epos2.discovery.FilterOption
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.lifetowncolumbus.pos.merchant.POSActivity
import org.mockito.Mock

class PrintManagerTest {

    @RelaxedMockK
    lateinit var discovery: DiscoveryWrapper

    @MockK
    lateinit var activity: POSActivity

    lateinit var printer: PrinterWrapper

    @InjectMockKs
    lateinit var subject: PrintManager

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        printer = mockk(relaxed = true)
        mockkObject(PrinterFactory.Companion)
        every { PrinterFactory.create(any(), any()) }.returns(printer)

    }

    @Test
    fun shouldCallStop() {
        subject.stop()
        verify { discovery.stop() }
    }

    @Test
    fun shouldCallStart() {
        subject.start()
        verify { discovery.start(activity, any(), any()) }
    }

    @Test
    fun shouldConfigureFilterOption() {
        val filterOption = slot<FilterOption>()
        every { discovery.start(any(), capture(filterOption), any()) }.just(runs)

        subject.start()

        assertThat(filterOption.captured.portType, `is`(Discovery.PORTTYPE_USB))
        assertThat(filterOption.captured.deviceModel, `is`(Discovery.MODEL_ALL))
        assertThat(filterOption.captured.deviceType, `is`(Discovery.TYPE_PRINTER))
    }

    class FakeDeviceInfo : DeviceInfo() {
        override fun getTarget(): String {
            return "TheTarget"
        }
        override fun getDeviceType(): Int {
            return 42
        }
    }

    @Test
    fun shouldDiscoverThePrinter() {
        every { discovery.start(any(), any(), captureLambda()) }.answers {
           lambda<(DeviceInfo) -> Unit>().invoke(FakeDeviceInfo())
        }

        subject.start()

        verify { printer.connect("TheTarget") }
    }
}
