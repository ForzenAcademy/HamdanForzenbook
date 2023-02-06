package com.hamdan.forzenbook.core

sealed interface LoginError : Error {
    override fun isValid(): Boolean = false

    sealed interface NameError {
        object Valid : LoginError {
            override fun isValid(): Boolean = true
        }

        object Length : LoginError
        object InvalidCharacters : LoginError
    }

    sealed interface BirthDateError {
        object Valid : LoginError {
            override fun isValid(): Boolean = true
        }

        object TooYoung : LoginError
    }

    sealed interface EmailError {
        object Valid : LoginError {
            override fun isValid(): Boolean = true
        }

        object Length : LoginError
        object InvalidFormat : LoginError
    }

    sealed interface LocationError {
        object Valid : LoginError {
            override fun isValid(): Boolean = true
        }

        object Length : LoginError
    }

    sealed interface CodeError {
        object Valid : LoginError {
            override fun isValid(): Boolean = true
        }

        object Length : LoginError
    }
}
