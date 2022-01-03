package com.xdev.expy.core.data.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "productEntities")
@Parcelize
data class ProductEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: String = "",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "expiryDate")
    var expiryDate: String = "",

    @ColumnInfo(name = "isOpened")
    @field:JvmField
    var isOpened: Boolean = false,

    @ColumnInfo(name = "openedDate")
    var openedDate: String = "",

    @ColumnInfo(name = "pao")
    var pao: Int = 0,

    @Ignore
    var reminders: List<ReminderEntity> = ArrayList(),

    @ColumnInfo(name = "isFinished")
    @field:JvmField
    var isFinished: Boolean = false,
) : Parcelable {

    @Ignore
    constructor(
        id: String,
        name: String,
        expiryDate: String,
        isOpened: Boolean,
        openedDate: String,
        pao: Int,
        isFinished: Boolean,
    ) : this(
        id = id,
        name = name,
        expiryDate = expiryDate,
        isOpened = isOpened,
        openedDate = openedDate,
        pao = pao,
        reminders = ArrayList(),
        isFinished = isFinished
    )
}