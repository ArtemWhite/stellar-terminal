package com.artemwhite.user.adapter.outbound.persistence.mappers

import com.artemwhite.user.testutil.UserFixtures.OBJECT_ID
import com.artemwhite.user.testutil.UserFixtures.collection
import com.artemwhite.user.testutil.UserFixtures.favorite
import com.artemwhite.user.testutil.UserFixtures.notification
import com.artemwhite.user.testutil.UserFixtures.preset
import com.artemwhite.user.testutil.UserFixtures.user
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class UserPersistenceMapperTest {

    @Test
    fun `round-trips a user`() {
        val domain = user()

        val restored = domain.toEntity().toDomain()

        assertThat(restored.id).isEqualTo(domain.id)
        assertThat(restored.nickname).isEqualTo(domain.nickname)
        assertThat(restored.email).isEqualTo(domain.email)
        assertThat(restored.passwordHash.value).isEqualTo(domain.passwordHash.value)
        assertThat(restored.role).isEqualTo(domain.role)
        assertThat(restored.createdAt).isEqualTo(domain.createdAt)
    }

    @Test
    fun `round-trips a favorite`() {
        val domain = favorite(UUID.randomUUID(), OBJECT_ID)

        val restored = domain.toEntity().toDomain()

        assertThat(restored.userId).isEqualTo(domain.userId)
        assertThat(restored.objectId).isEqualTo(domain.objectId)
        assertThat(restored.addedAt).isEqualTo(domain.addedAt)
    }

    @Test
    fun `round-trips a notification`() {
        val domain = notification(userId = UUID.randomUUID(), read = true)

        val restored = domain.toEntity().toDomain()

        assertThat(restored.id).isEqualTo(domain.id)
        assertThat(restored.text).isEqualTo(domain.text)
        assertThat(restored.read).isEqualTo(domain.read)
        assertThat(restored.createdAt).isEqualTo(domain.createdAt)
    }

    @Test
    fun `round-trips a collection preserving its objects`() {
        val domain = collection(userId = UUID.randomUUID(), objectIds = listOf(OBJECT_ID, UUID.randomUUID()))

        val restored = domain.toEntity().toDomain()

        assertThat(restored.id).isEqualTo(domain.id)
        assertThat(restored.name).isEqualTo(domain.name)
        assertThat(restored.objectIds).containsExactlyElementsOf(domain.objectIds)
    }

    @Test
    fun `round-trips a preset`() {
        val domain = preset(userId = UUID.randomUUID())

        val restored = domain.toEntity().toDomain()

        assertThat(restored.id).isEqualTo(domain.id)
        assertThat(restored.colors).isEqualTo(domain.colors)
        assertThat(restored.scale).isEqualTo(domain.scale)
    }
}
