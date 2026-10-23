package com.artemwhite.satellite.application.usecase

import com.artemwhite.satellite.domain.exceptions.SatelliteNotFoundException
import com.artemwhite.satellite.domain.models.OrbitSnapshot
import com.artemwhite.satellite.domain.models.PageResult
import com.artemwhite.satellite.domain.ports.outbound.SatelliteRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetTimelineUseCase(
    private val repository: SatelliteRepository,
) {
    fun execute(objectId: UUID, page: Int = 0, size: Int = 20): PageResult<OrbitSnapshot> {
        if (repository.findSatellite(objectId) == null) {
            throw SatelliteNotFoundException(objectId)
        }
        return repository.findSnapshots(objectId, page, size)
    }
}
