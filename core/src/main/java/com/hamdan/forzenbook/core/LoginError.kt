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

fun LoginError.stringForm(): String {
    val name = "Name"
    val email = "Email"
    val location = "Location"
    val date = "Birthday"
    val code = "Code"
    val length = "Length"
    val valid = "Valid"
    val char = "Invalid Characters"
    val format = "Invalid Format"
    val young = "Too Young"
    val none = "None"
    return when (this) {
        is LoginError.NameError.None -> "$name, $none"
        is LoginError.NameError.Valid -> "$name, $valid"
        is LoginError.NameError.Length -> "$name, $length"
        is LoginError.NameError.InvalidCharacters -> "$name, $char"
        is LoginError.EmailError.Length -> "$email, $length"
        is LoginError.EmailError.Valid -> "$email, $valid"
        is LoginError.EmailError.None -> "$email,$none"
        is LoginError.EmailError.InvalidFormat -> "$email,$format"
        is LoginError.CodeError.Length -> "$code,$length"
        is LoginError.CodeError.Valid -> "$code,$valid"
        is LoginError.CodeError.None -> "$code,$none"
        is LoginError.LocationError.Length -> "$location,$length"
        is LoginError.LocationError.None -> "$location,$none"
        is LoginError.LocationError.Valid -> "$location,$valid"
        is LoginError.BirthDateError.None -> "$date,$none"
        is LoginError.BirthDateError.Valid -> "$date,$valid"
        is LoginError.BirthDateError.TooYoung -> "$date,$young"
    }
}
