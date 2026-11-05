package com.class1c.smilecare.features.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.class1c.smilecare.ui.components.AppointmentCard

/** Stateful entry point: owns the ViewModel and wires the UDF loop. */
@Composable
fun HomeRoute(
    onSeeAllClick: () -> Unit,
    onDentistsClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onSeeAllClick = onSeeAllClick,
        onDentistsClick = onDentistsClick,
    )
}

/** Stateless screen: easy to preview and test, no Android/DI dependencies. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    onSeeAllClick: () -> Unit,
    onDentistsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("SmileCare") },
                actions = {
                    TextButton(onClick = onDentistsClick) {
                        Text("Dentists")
                    }
                    TextButton(onClick = onSeeAllClick) {
                        Text("See all")
                    }
                },
            )
        },
    ) { innerPadding ->
        when (uiState) {
            HomeUiState.Loading -> Message(
                text = "Loading...",
                modifier = Modifier.padding(innerPadding),
                showProgress = true,
            )

            HomeUiState.Empty -> Message(
                text = "No appointments yet.",
                modifier = Modifier.padding(innerPadding),
            )

            is HomeUiState.Error -> Message(
                text = uiState.message,
                modifier = Modifier.padding(innerPadding),
            )

            is HomeUiState.Success -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(uiState.appointments, key = { it.id }) { appointment ->
                    AppointmentCard(
                        appointment = appointment,
                        onCancel = { id ->
                            onEvent(HomeUiEvent.CancelAppointment(id))
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun Message(
    text: String,
    modifier: Modifier = Modifier,
    showProgress: Boolean = false,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (showProgress) {
            CircularProgressIndicator()
        } else {
            Text(text)
        }
    }
}
