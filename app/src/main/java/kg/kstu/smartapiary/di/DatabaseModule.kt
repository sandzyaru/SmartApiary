package kg.kstu.smartapiary.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kg.kstu.smartapiary.domain.room.AppDatabase
import kg.kstu.smartapiary.domain.room.NoteDao
import kg.kstu.smartapiary.domain.room.UserDao

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideNoteDao(database: AppDatabase): NoteDao {
        return database.noteDao()
    }
}