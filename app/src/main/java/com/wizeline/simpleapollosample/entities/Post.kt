package com.wizeline.simpleapollosample.entities

import android.os.Parcel
import android.os.Parcelable
import com.wizeline.simpleapollo.api.constants.DateTimePatterns
import com.wizeline.simpleapollo.utils.extensions.toDate
import com.wizeline.simpleapollo.utils.extensions.toSimpleApolloString
import java.util.*

data class Post(
    val id: String = "",
    val title: String = "",
    val text: String = "",
    val updatedAt: Date? = null,
    val user: User? = null,
    val comments: List<Comment>? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString()?.toDate(DateTimePatterns.RFC822_MILLIS_UTC.pattern, true),
        parcel.readParcelable(User::class.java.classLoader),
        parcel.createTypedArrayList(Comment)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(text)
        parcel.writeString(updatedAt?.toSimpleApolloString(DateTimePatterns.RFC822_MILLIS_UTC.pattern, true))
        parcel.writeParcelable(user, flags)
        parcel.writeTypedList(comments)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post = Post(parcel)

        override fun newArray(size: Int): Array<Post?> = arrayOfNulls(size)
    }
}
