package com.colabear754.interceptor_demo.user.dto

import com.colabear754.interceptor_demo.user.entity.UserInfo

data class UserInfoResponse(
    val name: String,
    val email: String
) {
    companion object {
        fun from(userInfo: UserInfo) = UserInfoResponse(
            name = userInfo.name,
            email = userInfo.email
        )
    }
}