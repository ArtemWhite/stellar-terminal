package com.artemwhite.user.domain

import com.artemwhite.user.testutil.UserFixtures.NOW
import com.artemwhite.user.testutil.UserFixtures.OBJECT_ID
import com.artemwhite.user.testutil.UserFixtures.favorite
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class FavoriteTest {

    @Test
    fun `records the bookmark timestamp`() {
        val userId = UUID.randomUUID()

        val favorite = favorite(userId, OBJECT_ID, NOW)

        assertThat(favorite.userId).isEqualTo(userId)
        assertThat(favorite.objectId).isEqualTo(OBJECT_ID)
        assertThat(favorite.addedAt).isEqualTo(NOW)
    }
}
