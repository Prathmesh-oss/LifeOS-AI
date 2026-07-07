package com.lifeosai.app.ui.nexus

import com.lifeosai.app.domain.model.NexusData

sealed interface NexusUiState {
    data object Loading : NexusUiState
    data class Success(val data: NexusData) : NexusUiState
    data class Error(val message: String) : NexusUiState
}
