package org.lifetowncolumbus.pos.merchant.views.saleComplete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.epson.epos2.printer.Printer
import kotlinx.android.synthetic.main.fragment_sale_complete.view.*
import org.lifetowncolumbus.pos.printing.PrintManager
import org.lifetowncolumbus.pos.printing.PrinterWrapper
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.merchant.POSActivity
import org.lifetowncolumbus.pos.merchant.viewModels.CurrentSale
import org.lifetowncolumbus.pos.toCurrencyString

class SaleCompleteFragment : androidx.fragment.app.Fragment() {
    private lateinit var currentSale: CurrentSale

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.newSaleButton.setOnClickListener {
            currentSale.newSale()
            Navigation.findNavController(view).popBackStack(R.id.checkoutFragment, false)
        }
    }

    override fun onResume() {
        super.onResume()
        PrintManager.print(ReceiptPrintJob(currentSale))
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
