package com.artemwhite.user.adapter.outbound.registry

import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.user.testutil.UserFixtures.OBJECT_ID
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import tools.jackson.databind.json.JsonMapper

class CatalogRestClientTest {

    private val client = CatalogRestClient("http://localhost:8080", JsonMapper.builder().build())

    @Test
    fun `parses a catalog object reference`() {
        val body = """
            {
              "id": "$OBJECT_ID",
              "name": "ISS",
              "category": "SATELLITE"
            }
        """.trimIndent()

        val ref = client.parseObjectRef(body)

        assertThat(ref.objectId).isEqualTo(OBJECT_ID)
        assertThat(ref.name).isEqualTo("ISS")
        assertThat(ref.category).isEqualTo(ObjectCategory.SATELLITE)
    }

    @Test
    fun `rejects a response missing the id`() {
        assertThatThrownBy { client.parseObjectRef("""{"name":"ISS","category":"SATELLITE"}""") }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("id")
    }

    @Test
    fun `rejects a response missing the name`() {
        assertThatThrownBy { client.parseObjectRef("""{"id":"$OBJECT_ID","category":"SATELLITE"}""") }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("name")
    }
}
