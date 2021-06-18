package com.xdev.expy.data.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "productEntities")
@Parcelize
data class ProductEntity (
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: String = "",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "expiryDate")
    var expiryDate: String = "",

    @ColumnInfo(name = "opened")
    @field:JvmField
    var isOpened: Boolean = false,

    @ColumnInfo(name = "openedDate")
    var openedDate: String = "",

    @ColumnInfo(name = "pao")
    var pao: Int = 0,

    @Ignore
    var reminders: List<ReminderEntity> = ArrayList()
) : Parcelable {

    @Ignore
    constructor(id: String, name: String, expiryDate: String, isOpened: Boolean, openedDate: String, pao: Int) : this() {
        this.id = id
        this.name = name
        this.expiryDate = expiryDate
        this.isOpened = isOpened
        this.openedDate = openedDate
        this.pao = pao
    }

    fun getIsOpened(): Boolean = isOpened

    fun setIsOpened(isOpened: Boolean){
        this.isOpened = isOpened
    }
}