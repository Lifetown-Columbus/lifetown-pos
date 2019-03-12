package org.lifetowncolumbus.pos.printing

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Before
import org.junit.Test
import org.lifetowncolumbus.pos.merchant.viewModels.CurrentSale
import org.lifetowncolumbus.pos.merchant.viewModels.PurchasedItem
import org.lifetowncolumbus.pos.merchant.views.saleComplete.ReceiptPrintJob
import java.math.BigDecimal

class ReceiptPrintJobTest {

    val currentSale : CurrentSale = mockk()
    lateinit var subject : PrintJob
    val printer: PrinterWrapper = mockk(relaxUnitFun = true)

    @Before
    fun setup(){
        every { printer.status().online }.returns(PrinterWrapper.TRUE)
        every { currentSale.items.value }.returns( arrayListOf(
            PurchasedItem(name = "Flerp", value = BigDecimal.valueOf(10.0)),
            PurchasedItem(name = "Derp", value = BigDecimal.valueOf(5.0))
        ))

        every { currentSale.total }.returns(BigDecimal.valueOf(15.0))
        subject = ReceiptPrintJob(currentSale)
    }

    @Test
    fun itShouldNotPrintIfThePinterIsNotOnline(){
        every { printer.status().online }.returns(0) //false

        subject.execute(printer)

        verify(exactly = 0) { printer.beginTransaction() }
    }


    @Test
    fun itShouldPrintEverything() {
        subject.execute(printer)

        verifySequence {
            printer.status()
            printer.beginTransaction()
            printer.addTextAlign(PrinterWrapper.ALIGN_CENTER)

            printer.addText("LifeTown Columbus\n")
            printer.addText("6220 E. Dublin Granville Rd.\n")
            printer.addText("New Albany, OH 43054\n")
            printer.addText("(614) 939-0765\n")
            printer.addFeedLine(2)
            printer.addText("Flerp\t$10.00\n")
            printer.addText("Derp\t$5.00\n")
            printer.addTextSize(2, 2)
            printer.addText("Change\t$15.00\n")
            printer.addTextSize(1, 1)
            printer.addFeedLine(4)
            printer.addCut(PrinterWrapper.CUT_FEED)
            printer.addPulse(PrinterWrapper.DRAWER_2PIN, PrinterWrapper.PULSE_200)
            printer.sendData(PrinterWrapper.PARAM_DEFAULT)
            printer.clearCommandBuffer()
            printer.endTransaction()
        }
    }


}