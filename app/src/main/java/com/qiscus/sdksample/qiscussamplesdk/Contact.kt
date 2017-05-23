package com.qiscus.sdksample.qiscussamplesdk

import android.os.Parcel
import android.os.Parcelable

class Contact : Parcelable {
    var email: String? = null
    var name: String? = null

    constructor(email: String, name: String) {
        this.email = email
        this.name = name
    }

    protected constructor(`in`: Parcel) {
        email = `in`.readString()
        name = `in`.readString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val contact = other as Contact?

        return if (email != null) email == contact!!.email else contact!!.email == null

    }

    override fun hashCode(): Int {
        return if (email != null) email!!.hashCode() else 0
    }

    override fun toString(): String {
        return "Contact{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}'
    }

    override fun describeContents(): Int {
        return hashCode()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(email)
        dest.writeString(name)
    }

    companion object {

        val CREATOR: Parcelable.Creator<Contact> = object : Parcelable.Creator<Contact> {
            override fun createFromParcel(`in`: Parcel): Contact {
                return Contact(`in`)
            }

            override fun newArray(size: Int): Array<Contact?> {
                return arrayOfNulls(size)
            }
        }
    }
}
