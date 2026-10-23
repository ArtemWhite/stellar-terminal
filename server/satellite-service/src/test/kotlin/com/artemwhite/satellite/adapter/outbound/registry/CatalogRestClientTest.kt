package com.artemwhite.satellite.adapter.outbound.registry

import com.artemwhite.shared.model.enums.ObjectCategory
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import tools.jackson.databind.json.JsonMapper

class CatalogRestClientTest {

    private val client = CatalogRestClient("http://unused", JsonMapper.builder().build())

    @Test
    fun `parses an object reference from catalog JSON`() {
        val body = """{"id":"00000000-0000-0000-0000-000000000001","name":"ISS","category":"SATELLITE"}"""

        val ref = client.parseObjectRef(body)

        assertThat(ref.objectId.toString()).isEqualTo("00000000-0000-0000-0000-000000000001")
        assertThat(ref.name).isEqualTo("ISS")
        assertThat(ref.category).isEqualTo(ObjectCategory.SATELLITE)
    }

    @Test
    fun `rejects JSON missing a required field`() {
        assertThatThrownBy { client.parseObjectRef("""{"name":"ISS","category":"SATELLITE"}""") }
            .isInstanceOf(IllegalStateException::class.java)
    }
}
