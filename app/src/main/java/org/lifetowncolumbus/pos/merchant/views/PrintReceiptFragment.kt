package org.lifetowncolumbus.pos.merchant.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_print_receipt.view.*
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.merchant.viewModels.CurrentSale
import org.lifetowncolumbus.pos.printing.OpenDrawerPrintJob
import org.lifetowncolumbus.pos.printing.PrintManager
import org.lifetowncolumbus.pos.printing.ReceiptPrintJob

class PrintReceiptFragment : Fragment() {
    private lateinit var currentSale: CurrentSale

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PrintManager.checkPrinter()

        view.printReceiptButton.setOnClickListener {
            PrintManager.print(ReceiptPrintJob(currentSale))
            Navigation.findNavController(view).popBackStack(R.id.checkoutFragment, false)
        }

        view.dontPrintReceiptButton.setOnClickListener {
            PrintManager.print(OpenDrawerPrintJob())
            Navigation.findNavController(view).popBackStack(R.id.checkoutFragment, false)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        currentSale.newSale()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_print_receipt, container, false)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentSale = activity?.run {
            ViewModelProvider(this).get(CurrentSale::class.java)
        } ?: throw Exception("Invalid Activity")
    }
}
