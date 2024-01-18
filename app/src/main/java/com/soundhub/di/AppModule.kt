package com.soundhub.di

import android.app.Application
import androidx.room.Room
import com.soundhub.data.UserDatabase
import com.soundhub.data.UserStore
import com.soundhub.data.repository.AuthRepository
import com.soundhub.data.repository.AuthRepositoryImpl
import com.soundhub.UiEventDispatcher
import com.soundhub.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesUserDatabase(app: Application): UserDatabase {
        return Room.databaseBuilder(
            app,
            UserDatabase::class.java,
            Constants.DB_USERS
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providesUiEventDispatcher(userStore: UserStore): UiEventDispatcher {
        return UiEventDispatcher(userStore)
    }


    @Provides
    @Singleton
    fun provideAuthRepository(db: UserDatabase): AuthRepository {
        return AuthRepositoryImpl(db.dao)
    }

    @Provides
    @Singleton
    fun provideUserDataStore(app: Application): UserStore {
        return UserStore(app)
    }
}