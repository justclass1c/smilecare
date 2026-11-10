package com.class1c.smilecare.di

import com.class1c.smilecare.domain.model.user.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSession @Inject constructor() {
    // everyone can read this
    var currentUser: User? = null
}