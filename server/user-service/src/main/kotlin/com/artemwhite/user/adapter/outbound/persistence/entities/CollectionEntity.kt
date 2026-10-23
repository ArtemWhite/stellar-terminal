package com.artemwhite.user.adapter.outbound.persistence.entities

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "collections")
class CollectionEntity(
    @Id
    val id: UUID,

    @Column(name = "user_id", nullable = false)
    var userId: UUID,

    @Column(nullable = false)
    var name: String,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "collection_objects", joinColumns = [JoinColumn(name = "collection_id")])
    @Column(name = "object_id")
    var objectIds: MutableSet<UUID> = linkedSetOf(),
)
