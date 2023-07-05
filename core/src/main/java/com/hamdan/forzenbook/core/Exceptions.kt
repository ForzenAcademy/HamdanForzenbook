package com.hamdan.forzenbook.core

class NullTokenException(message: String = "Token is null") : Exception(message)
class FailTokenRetrievalException(message: String = "Failed to retrieve token") : Exception(message)
class AccountException(message: String) : Exception(message)
class InputException(message: String = "Invalid input") : Exception(message)
class NetworkRetrievalException(message: String = "Issue retrieving data from network") : Exception(message)
class PostException(message: String) : Exception(message)
class InvalidTokenException(message: String = "Invalid token") : Exception(message)
class StateException(message:String = "Illegal state") : Exception(message)
