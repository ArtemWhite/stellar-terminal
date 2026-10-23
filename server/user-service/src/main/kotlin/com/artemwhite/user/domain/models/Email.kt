package com.artemwhite.user.domain.models

class Email(val value: String) {
    init {
        require(EMAIL_REGEX.matches(value)) { "invalid email address: $value" }
    }

    override fun equals(other: Any?): Boolean = other is Email && other.value == value

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value

    companion object {
        private val EMAIL_REGEX = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    }
}
