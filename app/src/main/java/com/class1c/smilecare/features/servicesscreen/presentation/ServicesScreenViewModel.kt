package com.class1c.smilecare.features.servicesscreen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.class1c.smilecare.domain.repository.ServiceRepository
import com.class1c.smilecare.features.services.servicesformscreen.presentation.ServiceFormUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServicesScreenViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(ServicesScreenUIState())

    val uiState: StateFlow<ServicesScreenUIState> = _uiState.asStateFlow()

    init { // once view model launches...
        viewModelScope.launch { // launch coroutine to get services
            runCatching { serviceRepository.listServices() }
                .onSuccess { list -> _uiState.update { it.copy(services = list) } }
                // TODO: .onFailure {}
        }

    }

}