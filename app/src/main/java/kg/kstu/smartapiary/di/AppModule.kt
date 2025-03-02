package kg.kstu.smartapiary.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kg.kstu.smartapiary.domain.repository.ApiaryRepository
import kg.kstu.smartapiary.domain.repository.FirebaseUserRepository
import kg.kstu.smartapiary.domain.room.AppDatabase
import kg.kstu.smartapiary.domain.room.UserDao
import kg.kstu.smartapiary.domain.room.UserRepository

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getDatabase(context)

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao, auth: FirebaseAuth): UserRepository =
        FirebaseUserRepository(userDao, auth)

    @Provides
    @Singleton
    fun provideApiaryRepository(): ApiaryRepository = ApiaryRepository()
}

