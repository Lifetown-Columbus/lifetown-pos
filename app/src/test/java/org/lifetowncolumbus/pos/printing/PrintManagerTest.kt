package org.lifetowncolumbus.pos.printing

import android.util.Log
import com.epson.epos2.Epos2Exception
import com.epson.epos2.discovery.DeviceInfo
import com.epson.epos2.discovery.Discovery
import com.epson.epos2.discovery.FilterOption
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.lifetowncolumbus.pos.DiscoveryWrapper
import org.lifetowncolumbus.pos.merchant.POSActivity

class PrintManagerTest {

    @MockK
    lateinit var discovery: DiscoveryWrapper

    @MockK
    lateinit var activity: POSActivity

    @MockK
    lateinit var printer: PrinterWrapper

    @InjectMockKs
    lateinit var subject: PrintManager

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        mockkStatic(Log::class)
        stubFoundPrinter()
    }

    private fun stubFoundPrinter() {
        mockkStatic("org.lifetowncolumbus.pos.printing.PrinterFactoryKt")
        every { createPrinter(any(), any()) }.returns(printer)

        every { discovery.start(any(), any(), captureLambda()) }.answers {
            lambda<(DeviceInfo) -> Unit>().invoke(FakeDeviceInfo())
        }
    }

    @After
    fun teardown() {
        unmockkAll()
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
        subject.start()

        verify { printer.connect("TheTarget") }
    }

    @Test
    fun shouldLogErrorWhenDiscoveryFails() {
        every { discovery.start(any(), any(), any()) }.throws(Epos2Exception("boom", Throwable()))

        subject.start()

        verify { Log.e(PrintManager::class.qualifiedName, "Printer discovery failed") }
    }

    @Test
    fun shouldLogWhenPrinterConnectionFails() {
        every { printer.connect(any()) }.throws(Epos2Exception("boom", Throwable()))

        subject.start()

        verify { Log.e(PrintManager::class.qualifiedName, "Printer connection failed") }
    }

    @Test
    fun shouldExecutePrintJobs() {
        subject.start()

        val printJob = mockk<PrintJob>(relaxUnitFun = true)
        PrintManager.print(printJob)

        verify { printJob.execute(printer) }
    }

}
