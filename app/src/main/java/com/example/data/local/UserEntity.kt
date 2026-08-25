package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.User

@Entity(tableName = "cached_users")
data class UserEntity(
    @PrimaryKey val id: String,
    val phoneNumber: String,
    val fullName: String,
    val email: String,
    val role: String,
    val profileImage: String?,
    val rating: Double
) {
    fun toDomain(): User = User(
        id = id,
        phoneNumber = phoneNumber,
        fullName = fullName,
        email = email,
        role = role,
        profileImage = profileImage,
        rating = rating
    )

    companion object {
        fun fromDomain(user: User): UserEntity = UserEntity(
            id = user.id,
            phoneNumber = user.phoneNumber,
            fullName = user.fullName,
            email = user.email,
            role = user.role,
            profileImage = user.profileImage,
            rating = user.rating
        )
    }
}
