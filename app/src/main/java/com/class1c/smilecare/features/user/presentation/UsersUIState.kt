package com.class1c.smilecare.features.user.presentation

import com.class1c.smilecare.domain.model.user.User

data class UsersUIState(
    val selectedTab: Int = 0,
    val searchQuery: String = "",
    val users: List<User>? = null,
    val error: String? = null
)