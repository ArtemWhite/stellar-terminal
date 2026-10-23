package com.artemwhite.user.application.usecase

import com.artemwhite.user.testutil.FakeFavoriteRepository
import com.artemwhite.user.testutil.FakeObjectRegistryClient
import com.artemwhite.user.testutil.UserFixtures.NOW
import com.artemwhite.user.testutil.UserFixtures.LATER
import com.artemwhite.user.testutil.UserFixtures.OBJECT_ID
import com.artemwhite.user.testutil.UserFixtures.favorite
import com.artemwhite.user.testutil.UserFixtures.objectRef
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class ListFavoritesUseCaseTest {

    private val repository = FakeFavoriteRepository()
    private val registry = FakeObjectRegistryClient()
    private val useCase = ListFavoritesUseCase(repository, registry)

    @Test
    fun `resolves every favorite into its object reference`() {
        val userId = UUID.randomUUID()
        registry.objects[OBJECT_ID] = objectRef(name = "ISS")
        repository.save(favorite(userId, OBJECT_ID, NOW))

        val result = useCase.execute(userId, 0, 20)

        assertThat(result.items).hasSize(1)
        assertThat(result.items[0].objectRef.name).isEqualTo("ISS")
        assertThat(result.items[0].favorite.objectId).isEqualTo(OBJECT_ID)
        assertThat(result.total).isEqualTo(1)
    }

    @Test
    fun `paginates favorites`() {
        val userId = UUID.randomUUID()
        repeat(3) { i ->
            val objectId = UUID.randomUUID()
            registry.objects[objectId] = objectRef(objectId)
            repository.save(favorite(userId, objectId, NOW.plusSeconds(i.toLong())))
        }

        val first = useCase.execute(userId, 0, 2)
        val second = useCase.execute(userId, 1, 2)

        assertThat(first.items).hasSize(2)
        assertThat(second.items).hasSize(1)
        assertThat(first.total).isEqualTo(3)
    }
}
