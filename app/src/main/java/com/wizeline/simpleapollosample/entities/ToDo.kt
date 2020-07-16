package com.wizeline.simpleapollosample.entities

import android.os.Parcel
import android.os.Parcelable

data class ToDo(
    val id: String = "",
    val title: String = "",
    val completed: Boolean = false
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeByte(if (completed) 1 else 0)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ToDo> {
        override fun createFromParcel(parcel: Parcel): ToDo = ToDo(parcel)

        override fun newArray(size: Int): Array<ToDo?> = arrayOfNulls(size)
    }
}
