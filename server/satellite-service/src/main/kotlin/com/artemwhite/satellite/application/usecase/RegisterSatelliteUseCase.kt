package com.artemwhite.satellite.application.usecase

import com.artemwhite.satellite.application.command.RegisterSatelliteCommand
import com.artemwhite.satellite.domain.models.Satellite
import com.artemwhite.satellite.domain.ports.outbound.SatelliteRepository
import org.springframework.stereotype.Service

@Service
class RegisterSatelliteUseCase(
    private val repository: SatelliteRepository,
) {
    fun execute(command: RegisterSatelliteCommand): Satellite =
        repository.findSatellite(command.objectId)
            ?: repository.saveSatellite(Satellite.create(command.objectId, command.noradId))
}
