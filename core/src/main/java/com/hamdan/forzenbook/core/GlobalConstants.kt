package com.hamdan.forzenbook.core

object GlobalConstants {
    const val EMAIL_LENGTH_LIMIT = 64
    const val BASE_URL = "https://forzen.dev/api/"
    const val TEMPORARY_FILENAME = "TempFileForDeletion"
    const val TOKEN_PREFERENCE_LOCATION = "FORZENBOOK_TOKEN_PREFERENCE_FILE"
    const val TOKEN_KEY = "loginToken"
    // this is currently the only variable used for thresholds to refresh data in the local data base
    // may want to have more for the different local databases
    const val DAY_IN_MILLIS = 86400000
    const val NAVIGATION_USERID = "userId"
    const val NAVIGATION_QUERY = "queryString"
    const val NAVIGATION_ERROR = "error"
    const val ONE_LINE = 1
    const val PAGED_POSTS_SIZE = 7
    private const val PAGED_POSTS_SET_SIZE = 2
    const val POSTS_MAX_SIZE = PAGED_POSTS_SIZE * PAGED_POSTS_SET_SIZE
    const val QUICK_ANIMATION_DURATION_MS = 75
}
