package com.class1c.smilecare.features.dentists.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.class1c.smilecare.domain.usecase.ObserveDentistsUseCase
import com.class1c.smilecare.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Unidirectional data flow for the dentists screen:
 *
 * UI event -> ViewModel -> use case -> repository -> Flow -> UiState -> UI
 */
@HiltViewModel
class DentistsViewModel @Inject constructor(
    private val observeDentists: ObserveDentistsUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DentistsUiState())
    val uiState: StateFlow<DentistsUiState> = _uiState.asStateFlow()

    private var observeJob: Job? = null

    init {
        loadDentists()
    }

    fun onEvent(event: DentistsUiEvent) {
        when (event) {
            is DentistsUiEvent.OnFavoriteToggled -> onFavoriteToggled(event.dentistId)
            DentistsUiEvent.OnRetryClicked -> loadDentists()
            DentistsUiEvent.OnErrorShown -> _uiState.update { it.copy(errorMessage = null) }
        }
    }

    private fun loadDentists() {
        observeJob?.cancel()
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        observeJob = observeDentists()
            .onEach { dentists ->
                _uiState.update { state ->
                    state.copy(isLoading = false, dentists = dentists)
                }
            }
            .catch { throwable ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "Could not load dentists.",
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun onFavoriteToggled(dentistId: String) {
        viewModelScope.launch {
            runCatching { toggleFavorite(dentistId) }
                .onFailure { throwable ->
                    _uiState.update { state ->
                        state.copy(
                            errorMessage = throwable.message ?: "Could not update favorite.",
                        )
                    }
                }
        }
    }
}
