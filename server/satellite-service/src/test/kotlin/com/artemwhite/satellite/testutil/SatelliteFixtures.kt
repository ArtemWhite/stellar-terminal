package com.artemwhite.satellite.testutil

import com.artemwhite.satellite.domain.models.ActiveTle
import com.artemwhite.satellite.domain.models.Position
import com.artemwhite.satellite.domain.models.Satellite
import com.artemwhite.satellite.domain.models.TleRecord
import com.artemwhite.shared.model.ObjectRef
import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.shared.model.enums.ObjectStatus
import java.time.Instant
import java.util.Locale
import java.util.UUID

object SatelliteFixtures {

    val NOW: Instant = Instant.parse("2026-01-01T00:00:00Z")
    val LATER: Instant = Instant.parse("2026-01-01T00:05:00Z")

    val ISS_OBJECT_ID: UUID = UUID.fromString("00000000-0000-0000-0000-000000000001")
    const val NORAD_ISS: Int = 25544

    /** Epoch encoded in the default TLE lines: epoch year 24, day-of-year 100. */
    val EPOCH: Instant = Instant.parse("2024-04-09T00:00:00Z")

    val SAMPLE_POSITION: Position = Position(1000.0, 2000.0, 3000.0, 1.0, 2.0, 3.0)

    fun satellite(
        objectId: UUID = ISS_OBJECT_ID,
        noradId: Int = NORAD_ISS,
        status: ObjectStatus = ObjectStatus.ACTIVE,
    ): Satellite = Satellite(objectId = objectId, noradId = noradId, status = status)

    fun objectRef(
        objectId: UUID = ISS_OBJECT_ID,
        category: ObjectCategory = ObjectCategory.SATELLITE,
    ): ObjectRef = ObjectRef(objectId = objectId, name = "ISS", category = category)

    fun activeTle(noradId: Int = NORAD_ISS, fetchedAt: Instant = NOW): ActiveTle =
        ActiveTle(noradId = noradId, tle = tle(noradId, fetchedAt))

    fun tle(noradId: Int = NORAD_ISS, fetchedAt: Instant = NOW): TleRecord = TleRecord(
        line1 = line1(noradId),
        line2 = line2(noradId),
        epoch = EPOCH,
        fetchedAt = fetchedAt,
    )

    val ISS_TLE: TleRecord = tle()

    /** Builds a valid 69-character TLE line 1 with each field at its fixed column. */
    fun line1(noradId: Int = NORAD_ISS, epochYear: Int = 24, epochDay: Double = 100.0): String {
        val chars = CharArray(69) { ' ' }
        chars[0] = '1'
        noradId.toString().padStart(5, '0').forEachIndexed { i, c -> chars[2 + i] = c }
        epochYear.toString().padStart(2, '0').takeLast(2).forEachIndexed { i, c -> chars[18 + i] = c }
        String.format(Locale.US, "%012.8f", epochDay).forEachIndexed { i, c -> chars[20 + i] = c }
        chars[68] = '0'
        return String(chars)
    }

    /** Builds a valid 69-character TLE line 2 with each orbital element at its fixed column. */
    fun line2(
        noradId: Int = NORAD_ISS,
        inclination: Double = 51.6416,
        raan: Double = 35.1234,
        argPerigee: Double = 45.6789,
        meanAnomaly: Double = 314.3210,
        meanMotion: Double = 15.5,
    ): String {
        val chars = CharArray(69) { ' ' }
        chars[0] = '2'
        noradId.toString().padStart(5, '0').forEachIndexed { i, c -> chars[2 + i] = c }
        String.format(Locale.US, "%8.4f", inclination).forEachIndexed { i, c -> chars[8 + i] = c }
        String.format(Locale.US, "%8.4f", raan).forEachIndexed { i, c -> chars[17 + i] = c }
        String.format(Locale.US, "%8.4f", argPerigee).forEachIndexed { i, c -> chars[34 + i] = c }
        String.format(Locale.US, "%8.4f", meanAnomaly).forEachIndexed { i, c -> chars[43 + i] = c }
        String.format(Locale.US, "%11.8f", meanMotion).forEachIndexed { i, c -> chars[52 + i] = c }
        chars[68] = '0'
        return String(chars)
    }
}
