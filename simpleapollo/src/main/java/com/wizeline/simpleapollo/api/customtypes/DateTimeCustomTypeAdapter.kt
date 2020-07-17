package com.wizeline.simpleapollo.api.customtypes

import com.apollographql.apollo.api.CustomTypeAdapter
import com.apollographql.apollo.api.CustomTypeValue
import com.wizeline.simpleapollo.utils.extensions.toDate
import com.wizeline.simpleapollo.utils.extensions.toSimpleApolloString
import java.lang.RuntimeException
import java.util.*

class DateTimeCustomTypeAdapter : CustomTypeAdapter<Date?> {
    override fun decode(value: CustomTypeValue<*>): Date? =
        try {
            value.value?.toString()?.toDate()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    override fun encode(value: Date?): CustomTypeValue<*> =
        try {
            CustomTypeValue.GraphQLString(
                value?.toSimpleApolloString() ?: ""
            )
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
}
