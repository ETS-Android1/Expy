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
    @field:JvmField var opened: Boolean = false,

    @ColumnInfo(name = "openedDate")
    var openedDate: String = "",

    @ColumnInfo(name = "pao")
    var pao: Int = 0,

    @Ignore
    var reminders: List<ReminderEntity> = ArrayList()
) : Parcelable {

    constructor(id: String, name: String, expiryDate: String, opened: Boolean, openedDate: String, pao: Int) : this() {
        this.id = id
        this.name = name
        this.expiryDate = expiryDate
        this.opened = opened
        this.openedDate = openedDate
        this.pao = pao
    }

    fun isOpened(): Boolean = opened

    fun setOpened(opened: Boolean){
        this.opened = opened
    }
}