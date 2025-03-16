package kg.kstu.smartapiary.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kg.kstu.smartapiary.domain.repository.ApiaryRepository
import kg.kstu.smartapiary.domain.repository.FirebaseUserRepository
import kg.kstu.smartapiary.domain.room.AppDatabase
import kg.kstu.smartapiary.domain.room.UserDao
import kg.kstu.smartapiary.domain.room.UserRepository
import javax.inject.Singleton

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
    @Singleton
    fun provideUserRepository(userDao: UserDao, auth: FirebaseAuth, firebaseDatabase: FirebaseDatabase ): UserRepository =
        FirebaseUserRepository(userDao, auth, firebaseDatabase)

    @Provides
    @Singleton
    fun provideApiaryRepository(): ApiaryRepository = ApiaryRepository()
}

