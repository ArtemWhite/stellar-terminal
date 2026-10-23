package com.artemwhite.user.adapter.outbound.registry

import com.artemwhite.shared.model.ObjectRef
import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.user.domain.exceptions.ObjectNotFoundException
import com.artemwhite.user.domain.ports.outbound.ObjectRegistryClient
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClientResponseException
import tools.jackson.databind.json.JsonMapper
import java.util.UUID

@Component
class CatalogRestClient(
    @Value("\${user.catalog.base-url}") baseUrl: String,
    private val objectMapper: JsonMapper,
) : ObjectRegistryClient {
    private val client: RestClient = RestClient.builder().baseUrl(baseUrl).build()
    private val log = LoggerFactory.getLogger(CatalogRestClient::class.java)

    override fun getObject(objectId: UUID): ObjectRef {
        log.debug("Resolving object {} in catalog", objectId)
        val body = try {
            client.get()
                .uri("/api/v1/objects/{id}", objectId)
                .retrieve()
                .body(String::class.java)
        } catch (e: RestClientResponseException) {
            if (e.statusCode.value() == 404) throw ObjectNotFoundException(objectId)
            throw e
        }
        return parseObjectRef(body ?: throw ObjectNotFoundException(objectId))
    }

    fun parseObjectRef(body: String): ObjectRef {
        val node = objectMapper.readTree(body)
        val id = node.get("id")?.asText()?.let { UUID.fromString(it) }
            ?: throw IllegalStateException("catalog response missing 'id'")
        val name = node.get("name")?.asText()
            ?: throw IllegalStateException("catalog response missing 'name'")
        val category = node.get("category")?.asText()?.let { ObjectCategory.valueOf(it) }
            ?: throw IllegalStateException("catalog response missing 'category'")
        return ObjectRef(objectId = id, name = name, category = category)
    }
}
