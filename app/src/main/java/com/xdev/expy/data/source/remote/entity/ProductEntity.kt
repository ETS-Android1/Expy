package com.xdev.expy.data.source.remote.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductEntity (
    var id: String = "",
    var userId: String = "",
    var name: String = "",
    var expiryDate: String = "",
    @field:JvmField var opened: Boolean = false,
    var openedDate: String = "",
    var pao: Int = 0,
    var reminders: List<ReminderEntity> = ArrayList()
) : Parcelable {
    fun isOpened(): Boolean = opened
    fun setOpened(opened: Boolean){
        this.opened = opened
    }
}