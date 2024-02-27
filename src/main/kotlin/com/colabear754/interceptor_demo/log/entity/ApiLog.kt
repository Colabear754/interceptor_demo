package com.colabear754.interceptor_demo.log.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
class ApiLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val seq: Long? = null,
    @Column(unique = true)
    val requestId: UUID,
    val serverIp: String,
    @Column(length = 4096)
    val requestUrl: String,
    val requestMethod: String,
    var responseStatus: Int? = null,
    val clientIp: String,
    @Column(length = 4096)
    val request: String,
    @Column(length = 4096)
    var response: String? = null,
    val requestTime: LocalDateTime = LocalDateTime.now(),
    var responseTime: LocalDateTime? = null
)