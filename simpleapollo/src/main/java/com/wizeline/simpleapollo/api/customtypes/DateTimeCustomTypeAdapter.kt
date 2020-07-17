package com.wizeline.simpleapollo.api.customtypes

import com.apollographql.apollo.api.CustomTypeAdapter
import com.apollographql.apollo.api.CustomTypeValue
import com.wizeline.simpleapollo.api.constants.DateTimePatterns
import com.wizeline.simpleapollo.utils.extensions.toDate
import com.wizeline.simpleapollo.utils.extensions.toSimpleApolloString
import java.lang.RuntimeException
import java.util.*

class DateTimeCustomTypeAdapter(
    val dateTimePatterns: DateTimePatterns
) : CustomTypeAdapter<Date?> {

    override fun decode(value: CustomTypeValue<*>): Date? =
        try {
            value.value?.toString()?.toDate(dateTimePatterns)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    override fun encode(value: Date?): CustomTypeValue<*> =
        try {
            CustomTypeValue.GraphQLString(
                value?.toSimpleApolloString(dateTimePatterns) ?: ""
            )
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
}
