package org.lifetowncolumbus.pos.merchant.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.databinding.FragmentPrintReceiptBinding
import org.lifetowncolumbus.pos.merchant.viewModels.CurrentSale
import org.lifetowncolumbus.pos.printing.OpenDrawerPrintJob
import org.lifetowncolumbus.pos.printing.PrintManager
import org.lifetowncolumbus.pos.printing.ReceiptPrintJob

class PrintReceiptFragment : Fragment() {
    private var _binding: FragmentPrintReceiptBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentSale: CurrentSale

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PrintManager.checkPrinter()

        binding.printReceiptButton.setOnClickListener {
            PrintManager.print(ReceiptPrintJob(currentSale))
            Navigation.findNavController(view).popBackStack(R.id.checkoutFragment, false)
        }

        binding.dontPrintReceiptButton.setOnClickListener {
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
    ): View {
        _binding = FragmentPrintReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentSale = activity?.run {
            ViewModelProvider(this).get(CurrentSale::class.java)
        } ?: throw Exception("Invalid Activity")
    }
}
