package com.soundhub.data.local_database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soundhub.data.local_database.dao.CountryDao
import com.soundhub.data.local_database.dao.LastFmDao
import com.soundhub.data.local_database.dao.PostDao
import com.soundhub.data.local_database.dao.UserDao
import com.soundhub.data.model.Album
import com.soundhub.data.model.Artist
import com.soundhub.data.model.Country
import com.soundhub.data.model.Genre
import com.soundhub.data.model.LastFmUser
import com.soundhub.data.model.User
import com.soundhub.domain.model.Post

@Database(
	entities = [
		User::class,
		Genre::class,
		Artist::class,
		Album::class,
		Country::class,
		LastFmUser::class,
		Post::class
	], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
	abstract fun userDao(): UserDao
	abstract fun countryDao(): CountryDao
	abstract fun lastFmDao(): LastFmDao
	abstract fun postDao(): PostDao
}