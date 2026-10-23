package com.artemwhite.catalog.adapter.inbound.web

import com.artemwhite.catalog.adapter.inbound.web.dto.CategorizeRequest
import com.artemwhite.catalog.adapter.inbound.web.dto.CreateObjectRequest
import com.artemwhite.catalog.adapter.inbound.web.dto.UpdateStatusRequest
import com.artemwhite.catalog.application.usecase.CategorizeObjectUseCase
import com.artemwhite.catalog.application.usecase.CreateObjectUseCase
import com.artemwhite.catalog.application.usecase.DeleteObjectUseCase
import com.artemwhite.catalog.application.usecase.GetObjectUseCase
import com.artemwhite.catalog.application.usecase.SearchObjectsUseCase
import com.artemwhite.catalog.application.usecase.UpdateObjectStatusUseCase
import com.artemwhite.catalog.domain.exceptions.ObjectNotFoundException
import com.artemwhite.catalog.domain.ports.outbound.Clock
import com.artemwhite.catalog.testutil.FakeSpaceObjectRepository
import com.artemwhite.catalog.testutil.SpaceObjectFixtures.NOW
import com.artemwhite.catalog.testutil.SpaceObjectFixtures.satellite
import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.shared.model.enums.ObjectStatus
import com.artemwhite.shared.model.enums.SourceType
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.UUID

class SpaceObjectControllerTest {

    private val repository = FakeSpaceObjectRepository()
    private val clock = Clock { NOW }
    private val controller = SpaceObjectController(
        SearchObjectsUseCase(repository),
        GetObjectUseCase(repository),
        CreateObjectUseCase(repository, clock),
        CategorizeObjectUseCase(repository, clock),
        UpdateObjectStatusUseCase(repository, clock),
        DeleteObjectUseCase(repository),
    )

    @Test
    fun `search returns a page of responses`() {
        repository.save(satellite())
        repository.save(satellite())

        val result = controller.search(null, null, null, 0, 20)

        assertThat(result.total).isEqualTo(2)
        assertThat(result.items).hasSize(2)
        assertThat(result.items.first().category).isEqualTo(ObjectCategory.SATELLITE)
    }

    @Test
    fun `get returns a response`() {
        val obj = satellite()
        repository.save(obj)

        val response = controller.get(obj.id)

        assertThat(response.id).isEqualTo(obj.id)
        assertThat(response.name).isEqualTo("ISS")
    }

    @Test
    fun `get throws for an unknown object`() {
        assertThatThrownBy { controller.get(UUID.randomUUID()) }
            .isInstanceOf(ObjectNotFoundException::class.java)
    }

    @Test
    fun `create maps request and returns an ACTIVE object`() {
        val request = CreateObjectRequest(
            name = "Hubble",
            category = ObjectCategory.SATELLITE,
            subcategory = null,
            description = "Space telescope",
            sourceId = "20580",
            sourceType = SourceType.NASA_HORIZONS,
        )

        val response = controller.create(request)

        assertThat(response.name).isEqualTo("Hubble")
        assertThat(response.status).isEqualTo(ObjectStatus.ACTIVE)
        assertThat(response.sourceType).isEqualTo(SourceType.NASA_HORIZONS)
    }

    @Test
    fun `categorize updates category and clears subcategory`() {
        val obj = satellite()
        repository.save(obj)

        val response = controller.categorize(
            obj.id,
            CategorizeRequest(ObjectCategory.STAR, null),
        )

        assertThat(response.category).isEqualTo(ObjectCategory.STAR)
        assertThat(response.subcategory).isNull()
    }

    @Test
    fun `updateStatus updates the status`() {
        val obj = satellite()
        repository.save(obj)

        val response = controller.updateStatus(
            obj.id,
            UpdateStatusRequest(ObjectStatus.DECAYED),
        )

        assertThat(response.status).isEqualTo(ObjectStatus.DECAYED)
    }

    @Test
    fun `delete removes the object`() {
        val obj = satellite()
        repository.save(obj)

        controller.delete(obj.id)

        assertThat(repository.findById(obj.id)).isNull()
    }
}
