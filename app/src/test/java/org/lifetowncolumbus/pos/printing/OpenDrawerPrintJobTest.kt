package org.lifetowncolumbus.pos.printing

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Before
import org.junit.Test

class OpenDrawerPrintJobTest {
    private val subject : PrintJob = OpenDrawerPrintJob()
    private val printer: PrinterWrapper = mockk(relaxUnitFun = true)

    @Before
    fun setup() {
        every { printer.status().connection }.returns(PrinterWrapper.TRUE)
    }

    @Test
    fun itShouldNotOpenTheDrawerIfThePrinterIsNotConnected(){
        every { printer.status().connection }.returns(0) //false

        subject.execute(printer)

        verify(exactly = 0) { printer.beginTransaction() }
    }

    @Test
    fun itShouldOpenTheDrawerIfThePrinterIsConnected(){
        subject.execute(printer)

        verifySequence {
            printer.status()
            printer.beginTransaction()
            printer.addPulse(PrinterWrapper.DRAWER_2PIN, PrinterWrapper.PULSE_200)
            printer.sendData(PrinterWrapper.PARAM_DEFAULT)
            printer.clearCommandBuffer()
            printer.endTransaction()
        }
    }
}