package com.colabear754.interceptor_demo.user.repository

import com.colabear754.interceptor_demo.user.entity.UserInfo
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserInfoRepository : JpaRepository<UserInfo, UUID> {
    fun findByNameAndPassword(name: String, password: String): UserInfo?
}