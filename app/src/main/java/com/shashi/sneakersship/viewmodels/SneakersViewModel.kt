package com.shashi.sneakersship.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.shashi.sneakersship.model.Sneaker
import com.shashi.sneakersship.repository.SneakerRepository
import com.shashi.sneakersship.utils.Resource
import kotlinx.coroutines.launch
import java.util.Locale

class SneakersViewModel(
    private val application: Application,
    private val repository: SneakerRepository
) : AndroidViewModel(application) {

    private lateinit var sneakers: LiveData<Resource<List<Sneaker>>>

    init {
        getSneakers()
    }

    private fun getSneakers() = viewModelScope.launch {
        sneakers = repository.getSneakers(application.applicationContext).asLiveData()
    }

    fun getSneakersLiveData(): LiveData<Resource<List<Sneaker>>> {
        if (!::sneakers.isInitialized || sneakers.value == null) {
            getSneakers()
        }

        return sneakers
    }

    fun searchSneakers(list: List<Sneaker>, query: String): List<Sneaker> {
        return list.filter { sneaker ->
            sneaker.name.lowercase(Locale.getDefault())
                .contains(query.lowercase(Locale.getDefault()))
        }
    }
}