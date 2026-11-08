package com.class1c.smilecare.navigation

/**
 * Every destination in the app, in one place.
 * Add new screens here so route strings are never duplicated.
 */
sealed interface Route {
    val path: String

    data object Login : Route {
        override val path: String = "login"
    }

    data object Register : Route {
        override val path: String = "register"
    }

    data object Home : Route {
        override val path: String = "home"
    }

    data object Appointments : Route {
        override val path: String = "appointments"
    }

    data object AppointmentDetail : Route {
        const val ID_ARG: String = "appointmentId"
        override val path: String = "appointment-detail/{$ID_ARG}"

        fun create(appointmentId: String): String = "appointment-detail/$appointmentId"
    }

    data object BookAppointment : Route {
        const val STEP_ARG: String = "step"
        override val path: String = "book-appointment/{$STEP_ARG}"

        fun create(step: Int): String = "book-appointment/$step"
    }

    data object Services : Route {
        override val path: String = "services"
    }

    data object ServiceDetail : Route {
        const val ID_ARG: String = "serviceId"
        override val path: String = "service-detail/{$ID_ARG}"

        fun create(serviceId: String): String = "service-detail/$serviceId"
    }

    data object Profile : Route {
        override val path: String = "profile"
    }
}
