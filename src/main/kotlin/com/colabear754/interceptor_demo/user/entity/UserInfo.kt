package com.colabear754.interceptor_demo.user.entity

import com.colabear754.interceptor_demo.user.dto.UserRegistRequest
import jakarta.persistence.*
import java.util.*

@Entity
class UserInfo(
    val name: String,
    @Column(unique = true)
    val email: String,
    val password: String
) {
    companion object {
        fun from(userRegistRequest: UserRegistRequest) = UserInfo(
            name = userRegistRequest.name,
            email = userRegistRequest.email,
            password = userRegistRequest.password
        )
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null
}