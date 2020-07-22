package com.wizeline.simpleapollo.utils.extensions

import com.apollographql.apollo.api.Error
import com.wizeline.simpleapollo.exceptions.EmptyResponse
import com.wizeline.simpleapollo.exceptions.ResponseWithErrors
import com.wizeline.simpleapollo.models.Response
import timber.log.Timber

internal fun <T> com.apollographql.apollo.api.Response<T>.processResponse(isDebug: Boolean): Response<T> =
    if (hasErrors().not()) {
        data?.let {
            Response.Success(it)
        } ?: Response.Failure(EmptyResponse()).also {
            if (isDebug) {
                Timber.e(it.error)
            }
        }
    } else {
        if (isDebug) {
            Timber.e(
                errors?.mapNotNull { it.toReadableLog() }?.takeUnless { it.isNullOrEmpty() }?.toString() ?: "Empty errors list"
            )
        }
        Response.Failure(
            ResponseWithErrors(
                errors ?: emptyList<Error>()
            )
        )
    }

fun Error.toReadableLog(): String = "$message on ${customAttributes.toReadableLog(locations)}"

fun Map<String, Any?>.toReadableLog(locations: List<Error.Location>): String {
    var details = ""
    var current = 0
    forEach { attribute ->
        var description = "${attribute.key}: ${attribute.value}"
        locations.getOrNull(current)?.also { location ->
            description += " (${location.line}:${location.column})"
        }
        if (details.isNullOrEmpty()) {
            details += description
        } else {
            details += ", $description"
        }
        current += 1
    }
    return details
}

fun Error.toHumanReadable(): String = message
