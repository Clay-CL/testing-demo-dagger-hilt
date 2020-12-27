package com.clay.dagger2testingdemo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.clay.dagger2testingdemo.data.local.entities.Word

@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class WordDatabase: RoomDatabase() {

    abstract fun wordDao(): WordDao

}