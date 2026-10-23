package com.artemwhite.satellite.domain.exceptions

import java.util.UUID

class SatelliteNotFoundException(val objectId: UUID) :
    DomainException("No orbital data for satellite $objectId")
