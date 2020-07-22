package com.wizeline.simpleapollo.exceptions

import com.apollographql.apollo.api.Error

class ResponseWithErrors(
    val errors: List<Error>
) : Exception()
