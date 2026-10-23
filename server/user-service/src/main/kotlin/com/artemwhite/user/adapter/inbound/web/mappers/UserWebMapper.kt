package com.artemwhite.user.adapter.inbound.web.mappers

import com.artemwhite.shared.model.enums.EventType
import com.artemwhite.user.adapter.inbound.web.dto.AuthResponse
import com.artemwhite.user.adapter.inbound.web.dto.CollectionResponse
import com.artemwhite.user.adapter.inbound.web.dto.FavoriteResponse
import com.artemwhite.user.adapter.inbound.web.dto.NotificationResponse
import com.artemwhite.user.adapter.inbound.web.dto.PresetResponse
import com.artemwhite.user.adapter.inbound.web.dto.RegisterUserRequest
import com.artemwhite.user.adapter.inbound.web.dto.SatelliteEventRequest
import com.artemwhite.user.adapter.inbound.web.dto.UserResponse
import com.artemwhite.user.application.command.RegisterUserCommand
import com.artemwhite.user.application.usecase.AuthResult
import com.artemwhite.user.application.usecase.ResolvedFavorite
import com.artemwhite.user.domain.events.DecayedEvent
import com.artemwhite.user.domain.events.PassOverEvent
import com.artemwhite.user.domain.events.SatelliteDomainEvent
import com.artemwhite.user.domain.events.StatusChangedEvent
import com.artemwhite.user.domain.models.Collection
import com.artemwhite.user.domain.models.Email
import com.artemwhite.user.domain.models.Notification
import com.artemwhite.user.domain.models.PageResult
import com.artemwhite.user.domain.models.User
import com.artemwhite.user.domain.models.VisualizationPreset
import kotlin.jvm.JvmName

fun RegisterUserRequest.toCommand(): RegisterUserCommand = RegisterUserCommand(
    nickname = nickname,
    email = Email(email),
    password = password,
)

fun User.toResponse(): UserResponse = UserResponse(
    id = id,
    nickname = nickname,
    email = email.value,
    role = role,
    createdAt = createdAt,
)

fun AuthResult.toResponse(): AuthResponse = AuthResponse(
    user = user.toResponse(),
    token = token,
)

fun Notification.toResponse(): NotificationResponse = NotificationResponse(
    id = id,
    text = text,
    read = read,
    createdAt = createdAt,
)

@JvmName("toNotificationPage")
fun PageResult<Notification>.toResponse(): PageResult<NotificationResponse> = PageResult(
    items = items.map { it.toResponse() },
    page = page,
    size = size,
    total = total,
)

fun ResolvedFavorite.toResponse(): FavoriteResponse = FavoriteResponse(
    objectId = favorite.objectId,
    name = objectRef.name,
    category = objectRef.category,
    addedAt = favorite.addedAt,
)

@JvmName("toFavoritePage")
fun PageResult<ResolvedFavorite>.toResponse(): PageResult<FavoriteResponse> = PageResult(
    items = items.map { it.toResponse() },
    page = page,
    size = size,
    total = total,
)

fun VisualizationPreset.toResponse(): PresetResponse = PresetResponse(
    id = id,
    colors = colors,
    scale = scale,
)

@JvmName("toPresetPage")
fun PageResult<VisualizationPreset>.toResponse(): PageResult<PresetResponse> = PageResult(
    items = items.map { it.toResponse() },
    page = page,
    size = size,
    total = total,
)

fun Collection.toResponse(): CollectionResponse = CollectionResponse(
    id = id,
    name = name,
    objectIds = objectIds,
)

@JvmName("toCollectionPage")
fun PageResult<Collection>.toResponse(): PageResult<CollectionResponse> = PageResult(
    items = items.map { it.toResponse() },
    page = page,
    size = size,
    total = total,
)

fun SatelliteEventRequest.toDomainEvent(): SatelliteDomainEvent = when (type) {
    EventType.STATUS_CHANGED -> StatusChangedEvent(objectId = objectId, status = status, occurredAt = occurredAt)
    EventType.DECAYED -> DecayedEvent(objectId = objectId, occurredAt = occurredAt)
    EventType.PASS_OVER -> PassOverEvent(objectId = objectId, occurredAt = occurredAt)
}
