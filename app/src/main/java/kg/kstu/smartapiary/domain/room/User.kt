package kg.kstu.smartapiary.domain.room

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



