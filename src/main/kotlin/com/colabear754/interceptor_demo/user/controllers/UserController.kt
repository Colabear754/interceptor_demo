package com.colabear754.interceptor_demo.user.controllers

import com.colabear754.interceptor_demo.user.dto.UserInfoResponse
import com.colabear754.interceptor_demo.user.dto.UserRegistRequest
import com.colabear754.interceptor_demo.user.entity.UserInfo
import com.colabear754.interceptor_demo.user.repository.UserInfoRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val userInfoRepository: UserInfoRepository) {
    @GetMapping("/sign-in")
    fun signIn(name: String, password: String) = ResponseEntity.ok(UserInfoResponse.from(userInfoRepository.findByNameAndPassword(name, password) ?: throw IllegalArgumentException("사용자 정보를 찾을 수 없습니다.")))

    @PostMapping("/sign-up")
    fun signUp(@RequestBody request: UserRegistRequest) = try {
        ResponseEntity.ok(UserInfoResponse.from(userInfoRepository.saveAndFlush(UserInfo.from(request))))
    } catch (e: DataIntegrityViolationException) {
        throw IllegalArgumentException("이미 사용중인 이메일입니다.")
    }
}
