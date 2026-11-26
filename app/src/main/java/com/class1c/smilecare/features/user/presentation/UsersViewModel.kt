package com.class1c.smilecare.features.user.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.class1c.smilecare.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<UsersUIState>(UsersUIState())
    val uiState: StateFlow<UsersUIState> = _uiState.asStateFlow()

    fun onEvent(event: UsersEvent) = when(event) {
        is UsersEvent.SwitchTab -> {
            _uiState.update { it.copy(selectedTab = event.index, users = null, error = null) }

            viewModelScope.launch {
                runCatching { userRepository.listUsers(uiState.value.selectedTab) }
                    .onSuccess { list -> _uiState.update { it.copy(users = list) } }
                    .onFailure {
                        _uiState.update {
                            it.copy(error = "Failed to load accounts. Check your connection and try again.")
                        }
                    }
            }
        }
        is UsersEvent.UpdateSearchQuery -> _uiState.update { it.copy(searchQuery = event.query) }
        UsersEvent.ViewButton -> TODO()
    }

    init {
        viewModelScope.launch {
            runCatching { userRepository.listUsers(uiState.value.selectedTab) }
                .onSuccess { list -> _uiState.update { it.copy(users = list) } }
                .onFailure {
                    _uiState.update {
                        it.copy(error = "Failed to load accounts. Check your connection and try again.")
                    }
                }
        }
    }
}