package com.xdev.expy.data.source.remote.entity

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReminderEntity (
    var timestamp: Timestamp = Timestamp(0, 0)
) : Parcelable