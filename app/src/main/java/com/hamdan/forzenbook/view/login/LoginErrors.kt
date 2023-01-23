package com.hamdan.forzenbook.view.login

interface LoginErrors

// Currently only the name errors are needed as enum class, leaving the rest created
// so the infrastructure is there incase there is decisions for more restrictions

enum class NameErrors : LoginErrors {
    VALID, LENGTH, EMPTY, INVALID_CHARACTERS
}

enum class BirthDateErrors : LoginErrors {
    VALID, EMPTY, TOO_YOUNG
}

enum class EmailErrors : LoginErrors {
    VALID, EMPTY, LENGTH, INVALID_FORMAT
}

enum class LocationErrors : LoginErrors {
    VALID, EMPTY, LENGTH
}

enum class CodeErrors : LoginErrors {
    VALID, EMPTY, LENGTH
}
