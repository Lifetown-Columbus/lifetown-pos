package org.lifetowncolumbus.pos.printing

import android.util.Log
import android.widget.Toast
import com.epson.epos2.Epos2Exception
import com.epson.epos2.discovery.DeviceInfo
import com.epson.epos2.discovery.Discovery
import com.epson.epos2.discovery.FilterOption
import com.epson.epos2.printer.Printer
import com.epson.epos2.printer.PrinterStatusInfo
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

    @MockK
    lateinit var toast: Toast

    @MockK
    lateinit var status: PrinterStatusInfo

    @InjectMockKs
    lateinit var subject: PrintManager

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        mockkStatic(Log::class)
        mockkStatic(Toast::class)

        every { printer.status() }.returns(status)
        every { status.online }.returns(Printer.TRUE)

        every { Toast.makeText(null, any<CharSequence>(), any()) }.returns(toast)
        stubFoundPrinter()
    }

    private fun stubFoundPrinter() {
        mockkStatic("org.lifetowncolumbus.pos.printing.PrinterFactoryKt")
        every { createPrinter(any()) }.returns(printer)

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

        verify { Log.e(PrintManager.Companion::class.qualifiedName, "Printer discovery failed") }
    }

    @Test
    fun shouldLogWhenPrinterConnectionFails() {
        every { printer.connect(any()) }.throws(Epos2Exception("boom", Throwable()))

        subject.start()

        verify { Log.e(PrintManager.Companion::class.qualifiedName, "Printer connection failed") }
    }

    @Test
    fun shouldLogWhenPrintJobFails() {
        val printJob = mockk<PrintJob>(relaxUnitFun = true)
        every { printJob.execute(any()) }.throws(Epos2Exception("boom", Throwable()))
        subject.start()

        PrintManager.print(printJob)

        verify { Log.e(PrintManager.Companion::class.qualifiedName, "PrintJob failed") }
    }

    @Test
    fun shouldExecutePrintJobs() {
        subject.start()

        val printJob = mockk<PrintJob>(relaxUnitFun = true)
        PrintManager.print(printJob)

        verify { printJob.execute(printer) }
    }

    @Test
    fun shouldToastUserWhen_PrinterIs_NotConnected() {
        subject.start()

        every { status.online }.returns(Printer.FALSE)
        every { status.connection }.returns(Printer.FALSE)
        every { status.coverOpen }.returns(Printer.FALSE)
        every { status.paper }.returns(Printer.PAPER_OK)

        val printJob = mockk<PrintJob>(relaxUnitFun = true)
        PrintManager.print(printJob)

        verify { Toast.makeText(null, "Printer is not connected! Turn off printer, check cable, turn printer back on.", Toast.LENGTH_LONG) }
    }

    @Test
    fun shouldToastUserWhen_PrinterIs_Open() {
        subject.start()

        every { status.online }.returns(Printer.FALSE)
        every { status.connection }.returns(Printer.TRUE)
        every { status.coverOpen }.returns(Printer.TRUE)
        every { status.paper }.returns(Printer.PAPER_OK)

        val printJob = mockk<PrintJob>(relaxUnitFun = true)
        PrintManager.print(printJob)

        verify { Toast.makeText(null, "Printer cover is open", Toast.LENGTH_LONG) }
    }

    @Test
    fun shouldToastUserWhen_PrinterIs_OutOfPaper() {
        subject.start()

        every { status.online }.returns(Printer.FALSE)
        every { status.connection }.returns(Printer.TRUE)
        every { status.coverOpen }.returns(Printer.FALSE)
        every { status.paper }.returns(Printer.PAPER_EMPTY)

        val printJob = mockk<PrintJob>(relaxUnitFun = true)
        PrintManager.print(printJob)

        verify { Toast.makeText(null, "Printer is out of paper", Toast.LENGTH_LONG) }
    }

    @Test
    fun shouldToastUserWhen_PrinterIs_Offline() {
        subject.start()

        every { status.online }.returns(Printer.FALSE)
        every { status.connection }.returns(Printer.TRUE)
        every { status.coverOpen }.returns(Printer.FALSE)
        every { status.paper }.returns(Printer.PAPER_OK)

        val printJob = mockk<PrintJob>(relaxUnitFun = true)
        PrintManager.print(printJob)

        verify { Toast.makeText(null, "Printer is offline! Turn off printer, check cable, turn printer back on.", Toast.LENGTH_LONG) }
    }

}
