package com.artemwhite.user.domain.models

class PasswordHash(val value: String) {
    init {
        require(value.isNotBlank()) { "password hash must not be blank" }
    }

    override fun equals(other: Any?): Boolean = other is PasswordHash && other.value == value

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = "PasswordHash(***)"
}
