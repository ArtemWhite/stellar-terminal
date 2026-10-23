package com.artemwhite.user.domain

import com.artemwhite.user.domain.models.Collection
import com.artemwhite.user.testutil.UserFixtures.OBJECT_ID
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.UUID

class CollectionTest {

    @Test
    fun `rejects a blank name`() {
        assertThatThrownBy { Collection.create(UUID.randomUUID(), UUID.randomUUID(), "  ") }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `renames through the behaviour method`() {
        val c = Collection.create(UUID.randomUUID(), UUID.randomUUID(), "old")

        c.rename("new")

        assertThat(c.name).isEqualTo("new")
    }

    @Test
    fun `adds an object id without duplicates`() {
        val c = Collection.create(UUID.randomUUID(), UUID.randomUUID(), "mine")

        c.addObject(OBJECT_ID)
        c.addObject(OBJECT_ID)

        assertThat(c.objectIds).containsExactly(OBJECT_ID)
    }

    @Test
    fun `removes an object id`() {
        val c = Collection.create(UUID.randomUUID(), UUID.randomUUID(), "mine", listOf(OBJECT_ID))

        c.removeObject(OBJECT_ID)

        assertThat(c.objectIds).isEmpty()
    }

    @Test
    fun `does not expose its internal list`() {
        val c = Collection.create(UUID.randomUUID(), UUID.randomUUID(), "mine", listOf(OBJECT_ID))
        val first = c.objectIds

        c.addObject(UUID.randomUUID())

        assertThat(first).hasSize(1)
    }
}
