package com.xdev.expy.data.source.remote.response

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReminderResponse(
    var id: Int = 0,
    var timestamp: Timestamp = Timestamp(0, 0)
) : Parcelable