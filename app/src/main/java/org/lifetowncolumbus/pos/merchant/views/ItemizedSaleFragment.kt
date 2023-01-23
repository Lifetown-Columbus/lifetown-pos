package org.lifetowncolumbus.pos.merchant.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.databinding.FragmentItemizedSaleBinding
import org.lifetowncolumbus.pos.merchant.viewModels.CashPayment
import org.lifetowncolumbus.pos.merchant.viewModels.CurrentSale
import org.lifetowncolumbus.pos.printing.OpenDrawerPrintJob
import org.lifetowncolumbus.pos.printing.PrintManager
import org.lifetowncolumbus.pos.toCurrencyString
import java.math.BigDecimal

class ItemizedSaleFragment : Fragment() {
    private var _binding: FragmentItemizedSaleBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentSale: CurrentSale
    private lateinit var adapter: ItemizedSaleRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initItemizedSaleRecyclerView(view)
        observeCheckoutViewModel()
        initOpenDrawerButton(view)
        initPayCashButton(view)
        initPayCreditButton(view)
        initQuickCashButton(view)
        renderTotal()
    }

    private fun observeCheckoutViewModel() {
        currentSale.items.observe(viewLifecycleOwner, Observer { items ->
            renderTotal()
            items?.let { adapter.setItems(it) }
        })

        currentSale.canCheckout.observe(viewLifecycleOwner, Observer { canCheckout ->
            binding.payCashButton.isEnabled = canCheckout
            binding.payCreditButton.isEnabled = canCheckout
            binding.quickCashButton.isEnabled = canCheckout
            adapter.setEnabled(canCheckout)
        })
    }

    private fun initQuickCashButton(view: View) {
        binding.quickCashButton.setOnClickListener {
            currentSale.payCash(CashPayment.worth(currentSale.total.toDouble()))
            Navigation.findNavController(this.activity!!, R.id.nav_host_fragment)
                .navigate(R.id.action_checkoutFragment_to_printReceiptFragment)
        }

    }

    private fun initOpenDrawerButton(view: View) {
        binding.openDrawerButton.setOnClickListener {
            PrintManager.print(OpenDrawerPrintJob())
        }
    }

    private fun initPayCashButton(view: View) {
        binding.payCashButton.setOnClickListener {
            Navigation.findNavController(this.activity!!, R.id.nav_host_fragment)
                .navigate(R.id.action_checkoutFragment_to_payCashFragment)
        }
    }

    private fun initPayCreditButton(view: View) {
        binding.payCreditButton.setOnClickListener {
            Navigation.findNavController(this.activity!!, R.id.nav_host_fragment)
                .navigate(R.id.action_checkoutFragment_to_swipeCreditFragment)
        }
    }

    private fun renderTotal() {
        val text = when {
            currentSale.total < BigDecimal.ZERO -> "Change Due: ${(currentSale.total.abs()).toCurrencyString()}"
            else -> "Total: ${currentSale.total.toCurrencyString()}"

        }
        binding.total.apply {
            this.text = text
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemizedSaleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        Navigation.findNavController(this.requireView()).addOnDestinationChangedListener { _, destination, _ ->
            currentSale.currentDestination = destination.id
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentSale = activity?.run {
            ViewModelProvider(this).get(CurrentSale::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    private fun initItemizedSaleRecyclerView(view: View) {
        adapter = ItemizedSaleRecyclerViewAdapter(currentSale::removeItem)
        binding.itemizedList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.activity).apply {
            orientation = RecyclerView.VERTICAL
        }
        binding.itemizedList.adapter = adapter
    }
}
