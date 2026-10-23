package com.artemwhite.satellite.adapter.inbound.web

import com.artemwhite.satellite.domain.exceptions.InvalidTleException
import com.artemwhite.satellite.domain.exceptions.NotASatelliteException
import com.artemwhite.satellite.domain.exceptions.ObjectNotFoundException
import com.artemwhite.satellite.domain.exceptions.SatelliteNotFoundException
import com.artemwhite.shared.model.enums.ObjectCategory
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class GlobalExceptionHandlerTest {

    private val handler = GlobalExceptionHandler()

    @Test
    fun `maps ObjectNotFoundException to 404`() {
        val id = UUID.randomUUID()

        val response = handler.handleObjectNotFound(ObjectNotFoundException(id))

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body!!.type).isEqualTo("object-not-found")
        assertThat(response.body!!.status).isEqualTo(404)
        assertThat(response.body!!.title).isEqualTo("Not Found")
        assertThat(response.body!!.detail).isEqualTo("Space object $id not found in catalog")
    }

    @Test
    fun `maps SatelliteNotFoundException to 404`() {
        val id = UUID.randomUUID()

        val response = handler.handleSatelliteNotFound(SatelliteNotFoundException(id))

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body!!.type).isEqualTo("satellite-not-found")
        assertThat(response.body!!.status).isEqualTo(404)
        assertThat(response.body!!.detail).isEqualTo("No orbital data for satellite $id")
    }

    @Test
    fun `maps NotASatelliteException to 400`() {
        val id = UUID.randomUUID()

        val response = handler.handleNotASatellite(NotASatelliteException(id, ObjectCategory.STAR))

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(response.body!!.type).isEqualTo("not-a-satellite")
        assertThat(response.body!!.detail).contains("STAR")
    }

    @Test
    fun `maps InvalidTleException to 422`() {
        val response = handler.handleInvalidTle(InvalidTleException("bad TLE"))

        assertThat(response.statusCode).isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT)
        assertThat(response.body!!.type).isEqualTo("invalid-tle")
        assertThat(response.body!!.status).isEqualTo(422)
        assertThat(response.body!!.detail).isEqualTo("bad TLE")
    }
}
