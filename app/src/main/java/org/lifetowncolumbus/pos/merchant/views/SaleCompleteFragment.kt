package org.lifetowncolumbus.pos.merchant.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.epson.epos2.printer.Printer
import kotlinx.android.synthetic.main.fragment_sale_complete.view.*
import org.lifetowncolumbus.pos.PrintManager
import org.lifetowncolumbus.pos.PrinterWrapper
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.merchant.POSActivity
import org.lifetowncolumbus.pos.merchant.viewModels.CurrentSale
import org.lifetowncolumbus.pos.toCurrencyString

class SaleCompleteFragment : androidx.fragment.app.Fragment() {
    private lateinit var currentSale: CurrentSale

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO: test this shit
        if (activity is POSActivity){
            val printer = PrintManager.printer

            if (printer.status().online == Printer.TRUE) {
                printReceipt(printer)
            }
        }

        view.newSaleButton.setOnClickListener {
            currentSale.newSale()
            Navigation.findNavController(view).popBackStack(R.id.checkoutFragment, false)
        }
    }

    private fun printReceipt(printer: PrinterWrapper)
    {
        printer.beginTransaction()
        printer.addTextAlign(Printer.ALIGN_CENTER)

        printer.addText("Lifetown Columbus\n")
        printer.addFeedLine(2)
        currentSale.items.value?.forEach {
            printer.addText("${it.name}\t${it.value.toCurrencyString()}\n")
        }
        printer.addTextSize(2, 2)
        printer.addText("Change\t${currentSale.total.toCurrencyString()}")
        printer.addTextSize(1, 1)
        printer.addFeedLine(4)
        printer.addCut(Printer.CUT_FEED)
        printer.addPulse(Printer.DRAWER_2PIN, Printer.PULSE_200)

        printer.sendData(Printer.PARAM_DEFAULT)
        printer.clearCommandBuffer()
        printer.endTransaction()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sale_complete, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentSale = activity?.run {
            ViewModelProviders.of(this).get(CurrentSale::class.java)
        } ?: throw Exception("Invalid Activity")
    }


}
