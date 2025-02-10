package kg.kstu.smartapiary.domain

import androidx.room.Dao

import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity
data class User(
    @PrimaryKey val uid: String,
    val email: String
)

@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE uid = :userId")
    suspend fun getUserById(userId: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("DELETE FROM User")
    suspend fun deleteUser()
}

