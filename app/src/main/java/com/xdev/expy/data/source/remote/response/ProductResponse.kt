package com.xdev.expy.data.source.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductResponse (
    var id: String = "",
    var name: String = "",
    var expiryDate: String = "",
    @field:JvmField var isOpened: Boolean = false,
    var openedDate: String = "",
    var pao: Int = 0,
    var reminders: List<ReminderResponse> = ArrayList()
) : Parcelable {

    fun getIsOpened(): Boolean = isOpened
}