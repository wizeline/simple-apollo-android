package com.wizeline.simpleapollosample.entities

import android.os.Parcel
import android.os.Parcelable
import com.wizeline.simpleapollo.api.constants.DateTimePatterns
import com.wizeline.simpleapollo.utils.extensions.toDate
import com.wizeline.simpleapollo.utils.extensions.toSimpleApolloString
import java.util.*

data class Comment(
    val id: String = "",
    val text: String = "",
    val updatedAt: Date? = null,
    val user: User? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString()?.toDate(DateTimePatterns.RFC822_MILLIS_UTC.pattern, true),
        parcel.readParcelable(User::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(text)
        parcel.writeString(updatedAt?.toSimpleApolloString(DateTimePatterns.RFC822_MILLIS_UTC.pattern, true))
        parcel.writeParcelable(user, flags)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Comment> {
        override fun createFromParcel(parcel: Parcel): Comment = Comment(parcel)

        override fun newArray(size: Int): Array<Comment?> = arrayOfNulls(size)
    }
}
