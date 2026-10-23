package com.artemwhite.user.domain.exceptions

class DuplicateEmailException(val email: String) : DomainException("Email $email is already registered")
