package org.lifetowncolumbus.pos.printing

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
}