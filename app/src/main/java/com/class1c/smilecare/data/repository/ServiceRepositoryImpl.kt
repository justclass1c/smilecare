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
                modifiedDate = service.modifiedDate
            )
        )
    }

    override suspend fun modifyService(service: Service) {
        TODO("Not yet implemented")
    }

    override suspend fun listServices(service: Service): Service {
        TODO("Not yet implemented")
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