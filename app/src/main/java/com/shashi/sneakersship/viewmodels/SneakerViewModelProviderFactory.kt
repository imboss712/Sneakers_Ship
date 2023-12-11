package com.shashi.sneakersship.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shashi.sneakersship.repository.SneakerRepository

class SneakerViewModelProviderFactory(
    private val application: Application,
    private val repository: SneakerRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SneakersViewModel(application, repository) as T
    }
}
