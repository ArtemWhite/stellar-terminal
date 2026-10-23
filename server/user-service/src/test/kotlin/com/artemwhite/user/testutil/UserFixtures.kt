package com.artemwhite.user.testutil

import com.artemwhite.shared.model.ObjectRef
import com.artemwhite.shared.model.enums.ObjectCategory
import com.artemwhite.user.domain.models.Collection
import com.artemwhite.user.domain.models.Email
import com.artemwhite.user.domain.models.Favorite
import com.artemwhite.user.domain.models.Notification
import com.artemwhite.user.domain.models.PasswordHash
import com.artemwhite.user.domain.models.Role
import com.artemwhite.user.domain.models.User
import com.artemwhite.user.domain.models.VisualizationPreset
import java.time.Instant
import java.util.UUID

object UserFixtures {

    val NOW: Instant = Instant.parse("2026-01-01T00:00:00Z")
    val LATER: Instant = Instant.parse("2026-01-01T00:05:00Z")

    val OBJECT_ID: UUID = UUID.fromString("00000000-0000-0000-0000-000000000001")

    fun email(value: String = "test@example.com"): Email = Email(value)

    fun user(
        id: UUID = UUID.randomUUID(),
        nickname: String = "tester",
        email: Email = email(),
        role: Role = Role.REGISTERED,
        now: Instant = NOW,
    ): User = User.create(
        id = id,
        nickname = nickname,
        email = email,
        passwordHash = PasswordHash("hashed:password123"),
        role = role,
        now = now,
    )

    fun favorite(userId: UUID, objectId: UUID = OBJECT_ID, addedAt: Instant = NOW): Favorite =
        Favorite.create(userId, objectId, addedAt)

    fun notification(
        id: UUID = UUID.randomUUID(),
        userId: UUID,
        text: String = "Object $OBJECT_ID has decayed from orbit",
        read: Boolean = false,
        createdAt: Instant = NOW,
    ): Notification = Notification(id = id, userId = userId, text = text, read = read, createdAt = createdAt)

    fun collection(
        id: UUID = UUID.randomUUID(),
        userId: UUID,
        name: String = "My collection",
        objectIds: List<UUID> = emptyList(),
    ): Collection = Collection.create(id, userId, name, objectIds)

    fun preset(
        id: UUID = UUID.randomUUID(),
        userId: UUID,
        colors: String = "#ff0000",
        scale: Double = 1.5,
    ): VisualizationPreset = VisualizationPreset.create(id, userId, colors, scale)

    fun objectRef(
        objectId: UUID = OBJECT_ID,
        name: String = "ISS",
        category: ObjectCategory = ObjectCategory.SATELLITE,
    ): ObjectRef = ObjectRef(objectId = objectId, name = name, category = category)
}
