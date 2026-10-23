package com.artemwhite.user.adapter.outbound.persistence.mappers

import com.artemwhite.user.adapter.outbound.persistence.entities.CollectionEntity
import com.artemwhite.user.adapter.outbound.persistence.entities.FavoriteEntity
import com.artemwhite.user.adapter.outbound.persistence.entities.NotificationEntity
import com.artemwhite.user.adapter.outbound.persistence.entities.UserEntity
import com.artemwhite.user.adapter.outbound.persistence.entities.VisualizationPresetEntity
import com.artemwhite.user.domain.models.Collection
import com.artemwhite.user.domain.models.Email
import com.artemwhite.user.domain.models.Favorite
import com.artemwhite.user.domain.models.Notification
import com.artemwhite.user.domain.models.PasswordHash
import com.artemwhite.user.domain.models.User
import com.artemwhite.user.domain.models.VisualizationPreset

fun UserEntity.toDomain(): User = User(
    id = id,
    nickname = nickname,
    email = Email(email),
    passwordHash = PasswordHash(passwordHash),
    role = role,
    createdAt = createdAt,
)

fun User.toEntity(): UserEntity = UserEntity(
    id = id,
    nickname = nickname,
    email = email.value,
    passwordHash = passwordHash.value,
    role = role,
    createdAt = createdAt,
)

fun FavoriteEntity.toDomain(): Favorite = Favorite(
    userId = userId,
    objectId = objectId,
    addedAt = addedAt,
)

fun Favorite.toEntity(): FavoriteEntity = FavoriteEntity(
    userId = userId,
    objectId = objectId,
    addedAt = addedAt,
)

fun NotificationEntity.toDomain(): Notification = Notification(
    id = id,
    userId = userId,
    text = text,
    read = read,
    createdAt = createdAt,
)

fun Notification.toEntity(): NotificationEntity = NotificationEntity(
    id = id,
    userId = userId,
    text = text,
    read = read,
    createdAt = createdAt,
)

fun CollectionEntity.toDomain(): Collection = Collection(
    id = id,
    userId = userId,
    name = name,
    objectIds = objectIds.toList(),
)

fun Collection.toEntity(): CollectionEntity = CollectionEntity(
    id = id,
    userId = userId,
    name = name,
    objectIds = objectIds.toMutableSet(),
)

fun VisualizationPresetEntity.toDomain(): VisualizationPreset = VisualizationPreset(
    id = id,
    userId = userId,
    colors = colors,
    scale = scale,
)

fun VisualizationPreset.toEntity(): VisualizationPresetEntity = VisualizationPresetEntity(
    id = id,
    userId = userId,
    colors = colors,
    scale = scale,
)
