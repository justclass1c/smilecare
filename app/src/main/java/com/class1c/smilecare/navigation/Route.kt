package com.class1c.smilecare.navigation

/**
 * Every destination in the app, in one place.
 * Add new screens here so route strings are never duplicated.
 */
sealed interface Route {
    val path: String

    data object Splash : Route {
        override val path: String = "splash"
    }

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

    data object CompleteAppointment : Route {
        override val path: String = "complete-appointment"
    }

    data object FindNextAvailable : Route {
        override val path: String = "find-next-available"
    }

    data object Reminders : Route {
        override val path: String = "reminders"
    }

    data object ReminderPreview : Route {
        override val path: String = "reminder-preview"
    }

    data object RescheduleAppointment : Route {
        const val ID_ARG: String = "appointmentId"
        override val path: String = "reschedule-appointment/{$ID_ARG}"

        fun create(appointmentId: String): String =
            "reschedule-appointment/$appointmentId"
    }

    data object CancelAppointment : Route {
        const val ID_ARG: String = "appointmentId"
        override val path: String = "cancel-appointment/{$ID_ARG}"

        fun create(appointmentId: String): String = "cancel-appointment/$appointmentId"
    }

    data object Schedule : Route {
        override val path: String = "schedule"
    }

    data object Patients : Route {
        override val path: String = "patients"
    }

    data object PatientDetail : Route {
        const val ID_ARG: String = "patientId"
        override val path: String = "patient-detail/{$ID_ARG}"

        fun create(patientId: String): String = "patient-detail/$patientId"
    }

    data object Payments : Route {
        override val path: String = "payments"
    }

    data object Services : Route {
        override val path: String = "services"
    }

    data object ServiceDetail : Route {
        const val ID_ARG: String = "serviceId"
        override val path: String = "service-detail/{$ID_ARG}"

        fun create(serviceId: String): String = "service-detail/$serviceId"
    }

    data object ServiceForm : Route {
        const val ID_ARG: String = "serviceId"
        override val path: String = "service-form/{$ID_ARG}"

        fun create(serviceId: String = "new"): String = "service-form/$serviceId"
    }

    data object DeleteService : Route {
        const val ID_ARG: String = "serviceId"
        override val path: String = "delete-service/{$ID_ARG}"

        fun create(serviceId: String): String = "delete-service/$serviceId"
    }

    data object StaffAccounts : Route {
        override val path: String = "staff-accounts"
    }

    data object StaffAccountDetail : Route {
        const val ID_ARG: String = "staffId"
        override val path: String = "staff-account-detail/{$ID_ARG}"

        fun create(staffId: String): String = "staff-account-detail/$staffId"
    }

    data object StaffAccountForm : Route {
        const val ID_ARG: String = "staffId"
        override val path: String = "staff-account-form/{$ID_ARG}"

        fun create(staffId: String = "new"): String = "staff-account-form/$staffId"
    }

    data object DeleteStaffAccount : Route {
        const val ID_ARG: String = "staffId"
        override val path: String = "delete-staff-account/{$ID_ARG}"

        fun create(staffId: String): String = "delete-staff-account/$staffId"
    }

    data object Inventory : Route {
        override val path: String = "inventory"
    }

    data object Equipment : Route {
        override val path: String = "equipment"
    }

    data object ManageClinic : Route {
        override val path: String = "manage-clinic"
    }

    data object LuckyDraw : Route {
        override val path: String = "lucky-draw"
    }

    data object Profile : Route {
        override val path: String = "profile"
    }

    data object EditProfile : Route {
        override val path: String = "edit-profile"
    }

    data object DeleteAccount : Route {
        override val path: String = "delete-account"
    }
}
