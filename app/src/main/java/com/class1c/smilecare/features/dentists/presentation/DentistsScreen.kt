package com.class1c.smilecare.features.dentists.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.class1c.smilecare.domain.model.Dentist
import com.class1c.smilecare.ui.theme.SmileCareTheme

/**
 * Stateful entry point: connects the ViewModel to the stateless UI.
 */
@Composable
fun DentistsRoute(
    viewModel: DentistsViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState: DentistsUiState by viewModel.uiState.collectAsStateWithLifecycle()

    DentistsScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        modifier = modifier,
    )
}

/**
 * Stateless UI: only renders [uiState] and reports [onEvent].
 * Easy to preview and test, because it has no ViewModel dependency.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DentistsScreen(
    uiState: DentistsUiState,
    onEvent: (DentistsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    uiState.errorMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message)
            onEvent(DentistsUiEvent.OnErrorShown)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text(text = "Our dentists") }) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        when {
            uiState.isLoading -> LoadingState(modifier = Modifier.padding(innerPadding))

            uiState.errorMessage != null && uiState.dentists.isEmpty() -> ErrorState(
                message = uiState.errorMessage,
                onRetry = { onEvent(DentistsUiEvent.OnRetryClicked) },
                modifier = Modifier.padding(innerPadding),
            )

            uiState.dentists.isEmpty() -> EmptyState(modifier = Modifier.padding(innerPadding))

            else -> DentistList(
                dentists = uiState.dentists,
                onFavoriteToggled = { id -> onEvent(DentistsUiEvent.OnFavoriteToggled(id)) },
                contentPadding = innerPadding,
            )
        }
    }
}

@Composable
private fun DentistList(
    dentists: List<Dentist>,
    onFavoriteToggled: (String) -> Unit,
    contentPadding: PaddingValues,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = contentPadding.calculateTopPadding() + 8.dp,
            bottom = contentPadding.calculateBottomPadding() + 16.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(items = dentists, key = { it.id }) { dentist ->
            DentistCard(
                dentist = dentist,
                onFavoriteToggled = { onFavoriteToggled(dentist.id) },
            )
        }
    }
}

@Composable
private fun DentistCard(
    dentist: Dentist,
    onFavoriteToggled: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 8.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = dentist.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = dentist.specialty,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "★ %.1f  •  %d yrs experience".format(
                        dentist.rating,
                        dentist.yearsOfExperience,
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            FavoriteButton(
                isFavorite = dentist.isFavorite,
                onClick = onFavoriteToggled,
            )
        }
    }
}

@Composable
private fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
) {
    val description = if (isFavorite) "Remove from favorites" else "Add to favorites"

    IconButton(
        onClick = onClick,
        modifier = Modifier.semantics { contentDescription = description },
    ) {
        Text(
            text = if (isFavorite) "★" else "☆",
            style = MaterialTheme.typography.headlineSmall,
            color = if (isFavorite) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
        )
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "No dentists available yet.",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(text = "Try again")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DentistsScreenPreview() {
    SmileCareTheme {
        DentistsScreen(
            uiState = DentistsUiState(
                isLoading = false,
                dentists = listOf(
                    Dentist(
                        id = "1",
                        name = "Dr. Aisha Rahman",
                        specialty = "Orthodontics",
                        rating = 4.9,
                        yearsOfExperience = 12,
                        isFavorite = true,
                    ),
                    Dentist(
                        id = "2",
                        name = "Dr. Marco Silva",
                        specialty = "Pediatric Dentistry",
                        rating = 4.7,
                        yearsOfExperience = 8,
                    ),
                ),
            ),
            onEvent = {},
        )
    }
}
