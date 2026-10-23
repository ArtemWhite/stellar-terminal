package com.artemwhite.user.application.usecase

import com.artemwhite.user.domain.exceptions.FavoriteAlreadyExistsException
import com.artemwhite.user.domain.exceptions.ObjectNotFoundException
import com.artemwhite.user.domain.ports.outbound.Clock
import com.artemwhite.user.testutil.FakeFavoriteRepository
import com.artemwhite.user.testutil.FakeObjectRegistryClient
import com.artemwhite.user.testutil.UserFixtures.NOW
import com.artemwhite.user.testutil.UserFixtures.OBJECT_ID
import com.artemwhite.user.testutil.UserFixtures.objectRef
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.UUID

class AddFavoriteUseCaseTest {

    private val repository = FakeFavoriteRepository()
    private val registry = FakeObjectRegistryClient()
    private val clock = Clock { NOW }
    private val useCase = AddFavoriteUseCase(repository, registry, clock)

    @Test
    fun `adds a favorite and resolves the object`() {
        val userId = UUID.randomUUID()
        registry.objects[OBJECT_ID] = objectRef()

        val resolved = useCase.execute(userId, OBJECT_ID)

        assertThat(resolved.favorite.objectId).isEqualTo(OBJECT_ID)
        assertThat(resolved.favorite.addedAt).isEqualTo(NOW)
        assertThat(resolved.objectRef.name).isEqualTo("ISS")
        assertThat(repository.exists(userId, OBJECT_ID)).isTrue()
    }

    @Test
    fun `rejects a duplicate favorite`() {
        val userId = UUID.randomUUID()
        registry.objects[OBJECT_ID] = objectRef()
        repository.save(com.artemwhite.user.domain.models.Favorite.create(userId, OBJECT_ID, NOW))

        assertThatThrownBy { useCase.execute(userId, OBJECT_ID) }
            .isInstanceOf(FavoriteAlreadyExistsException::class.java)
    }

    @Test
    fun `propagates an unknown object`() {
        assertThatThrownBy { useCase.execute(UUID.randomUUID(), UUID.randomUUID()) }
            .isInstanceOf(ObjectNotFoundException::class.java)
    }
}
