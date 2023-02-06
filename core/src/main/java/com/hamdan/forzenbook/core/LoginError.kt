package com.hamdan.forzenbook.core

sealed interface LoginError : Error {
    override fun isValid(): Boolean = false

    sealed interface NameError {
        object None : LoginError
        object Valid : LoginError {
            override fun isValid(): Boolean = true
        }
        object Length : LoginError
        object InvalidCharacters : LoginError
    }

    sealed interface BirthDateError {
        object None : LoginError
        object Valid : LoginError {
            override fun isValid(): Boolean = true
        }

        object TooYoung : LoginError
    }

    sealed interface EmailError {
        object None : LoginError
        object Valid : LoginError {
            override fun isValid(): Boolean = true
        }

        object Length : LoginError
        object InvalidFormat : LoginError
    }

    sealed interface LocationError {
        object None : LoginError
        object Valid : LoginError {
            override fun isValid(): Boolean = true
        }

        object Length : LoginError
    }

    sealed interface CodeError {
        object None : LoginError
        object Valid : LoginError {
            override fun isValid(): Boolean = true
        }

        object Length : LoginError
    }
}
