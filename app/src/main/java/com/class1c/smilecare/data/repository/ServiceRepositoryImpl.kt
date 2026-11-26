package com.class1c.smilecare.data.repository

import com.class1c.smilecare.data.remote.row.DentistRow
import com.class1c.smilecare.data.remote.row.NurseRow
import com.class1c.smilecare.data.remote.row.ServiceRow
import com.class1c.smilecare.domain.model.Service
import com.class1c.smilecare.domain.repository.ServiceRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient
): ServiceRepository {
    override suspend fun insertService(service: Service) {
        supabase.from("services").insert(
            ServiceRow(
                serviceID = generateServiceId(),
                serviceName = service.serviceName,
                description = service.description,
                price = service.price,
                modifiedDate = service.modifiedDate,
                isActive = service.isActive,
                duration = service.duration
            )
        )
    }

    override suspend fun modifyService(service: Service) {
        TODO("Not yet implemented")
    }

    override suspend fun listServices(): List<Service> {
        return supabase.from("services").select(
            Columns.list("service_id", "service_name", "description", "price", "modified_date", "is_active", "duration")) {
            order("service_id", Order.ASCENDING)
        }.decodeList<ServiceRow>()
        .map { row ->
            Service(
                serviceID = row.serviceID,
                serviceName = row.serviceName,
                description = row.description,
                price = row.price,
                modifiedDate = row.modifiedDate,
                isActive = row.isActive,
                duration = row.duration
            )
        }
    }

    override suspend fun deleteService(service: Service) {
        TODO("Not yet implemented")
    }

    private suspend fun generateServiceId(): String {
        val lastId = supabase.from("services").select(Columns.list("service_id")) {
                order("service_id", Order.DESCENDING)
                limit(1)
            }.decodeSingleOrNull<ServiceRow>()?.serviceID

        val next = lastId?.removePrefix("S")?.toIntOrNull()?.plus(1) ?: 1
        return "S%03d".format(next)
    }

}