package com.shashi.sneakersship.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shashi.sneakersship.R
import com.shashi.sneakersship.adapters.CartAdapter
import com.shashi.sneakersship.databinding.FragmentCartBinding
import com.shashi.sneakersship.db.SneakerDatabase
import com.shashi.sneakersship.model.CartItem
import com.shashi.sneakersship.repository.CartRepository
import com.shashi.sneakersship.viewmodels.CartViewModel
import com.shashi.sneakersship.viewmodels.CartViewModelProviderFactory

class CartFragment : Fragment() {

    private lateinit var viewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter

    private lateinit var binding: FragmentCartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cartRepository = CartRepository(SneakerDatabase(requireActivity()).getSneakerDao())
        val viewModelProviderFactory = CartViewModelProviderFactory(cartRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[CartViewModel::class.java]

        setupRecyclerView()

        cartAdapter.setOnCloseButtonClickListener {
            viewModel.removeFromCart(it.sneaker, it.quantity)
        }

        cartAdapter.setOnPlusButtonClickListener {
            viewModel.addToCart(it.sneaker, it.size, 1)
        }

        cartAdapter.setOnMinusButtonClickListener {
            viewModel.removeFromCart(it.sneaker, 1)
        }

        viewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            showCartItemList(cartItems)
        }

        viewModel.totalItems.observe(viewLifecycleOwner) {
            binding.itemCount.text = getString(R.string.item_count, it.toString())
        }

        viewModel.totalPrice.observe(viewLifecycleOwner) {
            binding.priceTextView.text =
                getString(R.string.item_price, it.toString())
        }

        //viewModel.getCartItems()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter()
        binding.rvCart.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun showCartItemList(cartItems: List<CartItem>) {
        if (cartItems.isEmpty()) {
            binding.apply {
                itemCount.visibility = View.GONE
                priceTextView.visibility = View.GONE
                checkoutButton.visibility = View.GONE
                rvCart.visibility = View.GONE
                messageText.visibility = View.VISIBLE
                messageText.text = getString(R.string.no_items_in_cart)
            }
        } else {
            binding.apply {
                itemCount.visibility = View.VISIBLE
                priceTextView.visibility = View.VISIBLE
                checkoutButton.visibility = View.VISIBLE
                checkoutButton.setOnClickListener {
                    Toast.makeText(
                        requireActivity(),
                        "Payment Amount: ${priceTextView.text}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                rvCart.visibility = View.VISIBLE
                messageText.visibility = View.GONE
                cartAdapter.differ.submitList(cartItems)
            }
        }
    }
}