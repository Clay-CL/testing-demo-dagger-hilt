package com.clay.dagger2testingdemo.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class Word(
    val word: String,
    val meaning: String,
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null
)
