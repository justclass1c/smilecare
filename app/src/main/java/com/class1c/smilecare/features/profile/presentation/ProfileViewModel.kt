package com.class1c.smilecare.features.profile.presentation

import androidx.lifecycle.ViewModel
import com.class1c.smilecare.di.UserSession
import com.class1c.smilecare.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userSession: UserSession
): ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUIState>(ProfileUIState())
    val uiState: StateFlow<ProfileUIState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(user = userSession.currentUser.value)}
    }
}