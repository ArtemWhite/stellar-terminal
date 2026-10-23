package com.artemwhite.satellite.adapter.outbound.calculation

import com.artemwhite.satellite.domain.exceptions.InvalidTleException
import com.artemwhite.satellite.domain.models.ActiveTle
import com.artemwhite.satellite.domain.models.TleRecord
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

@Component
class TleParser {

    data class Elements(
        val noradId: Int,
        val epoch: Instant,
        val inclinationRad: Double,
        val raanRad: Double,
        val argPerigeeRad: Double,
        val meanAnomalyRad: Double,
        val meanMotionRevPerDay: Double,
    )

    fun toActiveTle(line1: String, line2: String, fetchedAt: Instant): ActiveTle {
        val elements = parseElements(line1, line2)
        return ActiveTle(
            noradId = elements.noradId,
            tle = TleRecord(line1 = line1, line2 = line2, epoch = elements.epoch, fetchedAt = fetchedAt),
        )
    }

    fun parseElements(line1: String, line2: String): Elements {
        val l1 = line1.trim()
        val l2 = line2.trim()
        if (l1.length < 69 || l2.length < 69) {
            throw InvalidTleException("TLE lines must be at least 69 characters")
        }
        if (l1[0] != '1' || l2[0] != '2') {
            throw InvalidTleException("TLE line numbers must be '1' and '2'")
        }

        val noradId = l1.substring(2, 7).trim().toIntOrNull()
            ?: throw InvalidTleException("invalid NORAD id in TLE line 1")
        val meanMotion = parseField(l2, 52, 63, "mean motion")
        if (meanMotion <= 0.0) {
            throw InvalidTleException("mean motion must be positive, got $meanMotion")
        }

        return Elements(
            noradId = noradId,
            epoch = parseEpoch(l1),
            inclinationRad = Math.toRadians(parseField(l2, 8, 16, "inclination")),
            raanRad = Math.toRadians(parseField(l2, 17, 25, "right ascension")),
            argPerigeeRad = Math.toRadians(parseField(l2, 34, 42, "argument of perigee")),
            meanAnomalyRad = Math.toRadians(parseField(l2, 43, 51, "mean anomaly")),
            meanMotionRevPerDay = meanMotion,
        )
    }

    private fun parseField(line: String, from: Int, until: Int, name: String): Double =
        line.substring(from, until).trim().toDoubleOrNull()
            ?: throw InvalidTleException("invalid $name in TLE line 2")

    private fun parseEpoch(line: String): Instant {
        val epochYear = line.substring(18, 20).trim().toIntOrNull()
            ?: throw InvalidTleException("invalid epoch year in TLE line 1")
        val epochDay = line.substring(20, 32).trim().toDoubleOrNull()
            ?: throw InvalidTleException("invalid epoch day in TLE line 1")
        val fullYear = if (epochYear < 57) 2000 + epochYear else 1900 + epochYear
        val startOfYear = ZonedDateTime.of(fullYear, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC).toInstant()
        val secondsSinceNewYear = (epochDay - 1.0) * 86_400.0
        val wholeSeconds = secondsSinceNewYear.toLong()
        val nanos = ((secondsSinceNewYear - wholeSeconds) * 1_000_000_000.0).toLong()
        return startOfYear.plusSeconds(wholeSeconds).plusNanos(nanos)
    }
}
