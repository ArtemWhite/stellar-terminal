package com.artemwhite.catalog.adapter.inbound.web

import com.artemwhite.catalog.adapter.inbound.web.dto.CreateObjectRequest
import com.artemwhite.catalog.adapter.inbound.web.mappers.toCommand
import com.artemwhite.catalog.adapter.inbound.web.mappers.toResponse
import com.artemwhite.catalog.domain.models.PageResult
import com.artemwhite.catalog.testutil.SpaceObjectFixtures.satellite
import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.shared.model.enums.ObjectStatus
import com.artemwhite.shared.model.enums.SourceType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SpaceObjectWebMapperTest {

    @Test
    fun `maps a request DTO to a command`() {
        val request = CreateObjectRequest(
            name = "ISS",
            category = ObjectCategory.SATELLITE,
            subcategory = null,
            description = "International Space Station",
            sourceId = "25544",
            sourceType = SourceType.CELESTRAK,
        )

        val command = request.toCommand()

        assertThat(command.name).isEqualTo("ISS")
        assertThat(command.category).isEqualTo(ObjectCategory.SATELLITE)
        assertThat(command.sourceType).isEqualTo(SourceType.CELESTRAK)
    }

    @Test
    fun `maps a domain object to a response`() {
        val obj = satellite()

        val response = obj.toResponse()

        assertThat(response.id).isEqualTo(obj.id)
        assertThat(response.name).isEqualTo(obj.name)
        assertThat(response.status).isEqualTo(ObjectStatus.ACTIVE)
    }

    @Test
    fun `maps a page result of domain objects`() {
        val obj = satellite()

        val result = PageResult(listOf(obj), 0, 20, 1).toResponse()

        assertThat(result.total).isEqualTo(1)
        assertThat(result.items).singleElement().extracting { it.id }.isEqualTo(obj.id)
    }
}
