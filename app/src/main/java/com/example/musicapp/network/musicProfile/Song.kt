package com.example.musicapp.network.musicProfile

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Song(
    val duration: Int,

    @Json(name = "file_name")
    val url: String,

    @Json(name = "id")
    val fileId: Int,

    @Json(name = "md5_file")
    val md5File: String,

    val name: String,
    val order: Int,
    val size: Int
) : Comparable<Song>, Parcelable {

    override fun compareTo(other: Song): Int {
        return this.order - other.order
    }

}
