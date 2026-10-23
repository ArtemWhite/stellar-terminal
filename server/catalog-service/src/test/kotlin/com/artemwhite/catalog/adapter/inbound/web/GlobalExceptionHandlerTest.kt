package com.artemwhite.catalog.adapter.inbound.web

import com.artemwhite.catalog.domain.exceptions.InvalidCategoryException
import com.artemwhite.catalog.domain.exceptions.ObjectNotFoundException
import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.shared.model.enums.SatelliteSubcategory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.util.UUID

class GlobalExceptionHandlerTest {

    private val handler = GlobalExceptionHandler()

    @Test
    fun `maps ObjectNotFoundException to 404`() {
        val id = UUID.randomUUID()

        val response = handler.handleNotFound(ObjectNotFoundException(id))

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body).isNotNull
        assertThat(response.body!!.type).isEqualTo("object-not-found")
        assertThat(response.body!!.status).isEqualTo(404)
        assertThat(response.body!!.title).isEqualTo("Not Found")
        assertThat(response.body!!.detail).isEqualTo("Space object $id not found")
    }

    @Test
    fun `maps InvalidCategoryException to 400`() {
        val response = handler.handleInvalidCategory(
            InvalidCategoryException(ObjectCategory.PLANET, SatelliteSubcategory.WEATHER),
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(response.body).isNotNull
        assertThat(response.body!!.type).isEqualTo("invalid-category")
        assertThat(response.body!!.status).isEqualTo(400)
        assertThat(response.body!!.title).isEqualTo("Bad Request")
        assertThat(response.body!!.detail).contains("WEATHER")
    }
}
