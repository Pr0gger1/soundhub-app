package com.soundhub.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.soundhub.data.model.User
import com.soundhub.utils.converters.DateConverter
import com.soundhub.utils.converters.StringListConverter

@Database(
    entities = [User::class],
    version = 9,
    exportSchema = false
)
@TypeConverters(StringListConverter::class, DateConverter::class)
abstract class UserDatabase: RoomDatabase() {
    abstract val dao: UserDao
}