package com.wizeline.simpleapollo.api.customtypes

import com.apollographql.apollo.api.CustomTypeAdapter
import com.apollographql.apollo.api.CustomTypeValue
import com.wizeline.simpleapollo.utils.extensions.toJson
import org.json.JSONObject
import java.lang.RuntimeException

class JSONStringCustomTypeAdapter : CustomTypeAdapter<JSONObject> {

    override fun decode(value: CustomTypeValue<*>): JSONObject =
        try {
            value.value?.toString()?.toJson() ?: JSONObject()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    override fun encode(value: JSONObject): CustomTypeValue<*> =
        try {
            CustomTypeValue.GraphQLString(
                value.toString()
            )
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
}
