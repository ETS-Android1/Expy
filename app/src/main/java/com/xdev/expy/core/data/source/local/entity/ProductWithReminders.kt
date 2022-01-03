package com.xdev.expy.core.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ProductWithReminders(

    @Embedded
    var product: ProductEntity,

    @Relation(parentColumn = "id", entityColumn = "productId")
    var reminders: List<ReminderEntity>,
)