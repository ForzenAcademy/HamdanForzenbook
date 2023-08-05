package com.hamdan.forzenbook.core

interface Error {
    fun isValid(): Boolean = false
}

sealed interface EntryError : Error {
    override fun isValid(): Boolean = false

    sealed interface NameError {
        object None : EntryError
        object Valid : EntryError {
            override fun isValid(): Boolean = true
        }

        object Length : EntryError
        object InvalidCharacters : EntryError
    }

    sealed interface BirthDateError {
        object None : EntryError
        object Valid : EntryError {
            override fun isValid(): Boolean = true
        }

        object TooYoung : EntryError
    }

    sealed interface EmailError {
        object None : EntryError
        object Valid : EntryError {
            override fun isValid(): Boolean = true
        }

        object Length : EntryError
        object InvalidFormat : EntryError
    }

    sealed interface LocationError {
        object None : EntryError
        object Valid : EntryError {
            override fun isValid(): Boolean = true
        }

        object Length : EntryError
    }

    sealed interface CodeError {
        object None : EntryError
        object Valid : EntryError {
            override fun isValid(): Boolean = true
        }

        object Length : EntryError
    }
}

// Note this is purely for debugging not meant for user to see
fun EntryError.stringForm(): String {
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
        is EntryError.NameError.None -> "$name, $none"
        is EntryError.NameError.Valid -> "$name, $valid"
        is EntryError.NameError.Length -> "$name, $length"
        is EntryError.NameError.InvalidCharacters -> "$name, $char"
        is EntryError.EmailError.Length -> "$email, $length"
        is EntryError.EmailError.Valid -> "$email, $valid"
        is EntryError.EmailError.None -> "$email,$none"
        is EntryError.EmailError.InvalidFormat -> "$email,$format"
        is EntryError.CodeError.Length -> "$code,$length"
        is EntryError.CodeError.Valid -> "$code,$valid"
        is EntryError.CodeError.None -> "$code,$none"
        is EntryError.LocationError.Length -> "$location,$length"
        is EntryError.LocationError.None -> "$location,$none"
        is EntryError.LocationError.Valid -> "$location,$valid"
        is EntryError.BirthDateError.None -> "$date,$none"
        is EntryError.BirthDateError.Valid -> "$date,$valid"
        is EntryError.BirthDateError.TooYoung -> "$date,$young"
    }
}
