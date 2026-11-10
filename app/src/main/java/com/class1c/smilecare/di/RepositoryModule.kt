package com.class1c.smilecare.di

import com.class1c.smilecare.data.repository.DentistRepositoryImpl
import com.class1c.smilecare.data.repository.InMemoryAppointmentRepository
import com.class1c.smilecare.data.repository.UserRepositoryImpl
import com.class1c.smilecare.domain.repository.AppointmentRepository
import com.class1c.smilecare.domain.repository.DentistRepository
import com.class1c.smilecare.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

/**
 * Tells Hilt which implementation to use for each repository interface.
 *
 * `@Binds` is the compile-time, zero-boilerplate binding. `@Singleton` keeps one
 * instance for the whole app, which is what preserves the single source of truth.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAppointmentRepository(
        implementation: InMemoryAppointmentRepository,
    ): AppointmentRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        implementation: UserRepositoryImpl
    ): UserRepository
}
