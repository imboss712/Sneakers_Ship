package com.shashi.sneakersship.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shashi.sneakersship.R
import com.shashi.sneakersship.adapters.SneakerAdapter
import com.shashi.sneakersship.databinding.FragmentHomeBinding
import com.shashi.sneakersship.db.SneakerDatabase
import com.shashi.sneakersship.model.Sneaker
import com.shashi.sneakersship.repository.CartRepository
import com.shashi.sneakersship.repository.SneakerRepository
import com.shashi.sneakersship.utils.Resource
import com.shashi.sneakersship.viewmodels.CartViewModel
import com.shashi.sneakersship.viewmodels.CartViewModelProviderFactory
import com.shashi.sneakersship.viewmodels.SneakerViewModelProviderFactory
import com.shashi.sneakersship.viewmodels.SneakersViewModel

class HomeFragment : Fragment() {
    private lateinit var sneakersViewModel: SneakersViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var sneakerAdapter: SneakerAdapter

    private lateinit var binding: FragmentHomeBinding

    private var sneakersList = emptyList<Sneaker>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sneakerRepository = SneakerRepository()
        val sneakerViewModelProviderFactory = SneakerViewModelProviderFactory(
            requireActivity().application, sneakerRepository
        )
        sneakersViewModel = ViewModelProvider(this, sneakerViewModelProviderFactory)[SneakersViewModel::class.java]
        val cartRepository = CartRepository(SneakerDatabase(requireActivity()).getSneakerDao())
        val viewModelProviderFactory = CartViewModelProviderFactory(cartRepository)
        cartViewModel = ViewModelProvider(this, viewModelProviderFactory)[CartViewModel::class.java]

        setupRecyclerView()
        setUpSearchView()

        sneakerAdapter.setOnItemClickListener {
            showSneakerDialog(it)
        }

        sneakersViewModel.getSneakersLiveData().observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data.let { sneakerResponse ->
                        sneakersList = sneakerResponse
                        showSneakerList(sneakerResponse, "No Sneakers found")
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        showMessageText(message)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showMessageText(msgText: String) {
        binding.messageText.visibility = View.VISIBLE
        binding.rvSneakers.visibility = View.GONE
        binding.messageText.text = msgText
    }

    private fun showSneakerList(sneakers: List<Sneaker>, message: String) {
        if (sneakers.isEmpty()) {
            showMessageText(message)
        } else {
            binding.rvSneakers.visibility = View.VISIBLE
            binding.messageText.visibility = View.GONE
            sneakerAdapter.differ.submitList(sneakers)
        }
    }

    private fun setupRecyclerView() {
        sneakerAdapter = SneakerAdapter()
        binding.rvSneakers.apply {
            adapter = sneakerAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    private fun setUpSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    val filteredList = sneakersViewModel.searchSneakers(sneakersList, newText)
                    showSneakerList(filteredList, "No Sneakers matches")
                }
                return true
            }
        })
    }

    private fun showSneakerDialog(sneaker: Sneaker) {
        val dialog = BottomSheetDialog(requireActivity())
        dialog.setContentView(R.layout.layout_sneaker_detail)

        val btnClose = dialog.findViewById<ImageButton>(R.id.closeButton)
        val shoeImage = dialog.findViewById<ImageView>(R.id.productImage)
        val title = dialog.findViewById<TextView>(R.id.productTitle)
        val price = dialog.findViewById<TextView>(R.id.productPrice)
        val color = dialog.findViewById<TextView>(R.id.productColorVal)
        val decreaseCountButton = dialog.findViewById<ImageButton>(R.id.minusButton)
        val increaseCountButton = dialog.findViewById<ImageButton>(R.id.plusButton)
        val countText = dialog.findViewById<EditText>(R.id.quantityInput)
        val addToCartBtn = dialog.findViewById<Button>(R.id.addToCartButton)

        btnClose?.setOnClickListener {
            dialog.dismiss()
        }

        var count = 1

        if (shoeImage != null) {
            Glide.with(requireActivity()).load(sneaker.media.imageUrl).into(shoeImage)
        }
        title?.text = sneaker.name
        price?.text = getString(R.string.item_price, sneaker.retailPrice.toString())
        color?.text = sneaker.colorway
        countText?.setText(count.toString())
        decreaseCountButton?.isEnabled = false

        decreaseCountButton?.setOnClickListener {
            count--
            countText?.setText(count.toString())
            if (count <= 1) {
                decreaseCountButton.isEnabled = false
            }
        }

        increaseCountButton?.setOnClickListener {
            count++
            countText?.setText(count.toString())
            if (count > 1) {
                decreaseCountButton?.isEnabled = true
            }
        }

        addToCartBtn?.setOnClickListener {
            cartViewModel.addToCart(sneaker, 8, count)
            dialog.dismiss()
            Toast.makeText(requireActivity(), "$count items added in the cart", Toast.LENGTH_SHORT)
                .show()
        }

        dialog.setCancelable(true)
        dialog.show()
    }
}