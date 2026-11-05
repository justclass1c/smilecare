package com.class1c.smilecare.navigation

/**
 * Every destination in the app, in one place.
 * Add new screens here so route strings are never duplicated.
 */
sealed interface Route {
    val path: String

    data object Home : Route {
        override val path: String = "home"
    }

    data object Appointments : Route {
        override val path: String = "appointments"
    }

    data object Dentists : Route {
        override val path: String = "dentists"
    }
}
