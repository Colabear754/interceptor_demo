package com.colabear754.interceptor_demo.log.repository

import com.colabear754.interceptor_demo.log.entity.ApiLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface ApiLogRepository : JpaRepository<ApiLog, Long> {
    @Transactional
    @Modifying
    @Query("""
        UPDATE ApiLog a
        SET a.responseStatus = :status, a.response = :response, a.responseTime = CURRENT_TIMESTAMP
        WHERE a.requestId = :requestId
    """)
    fun updateResponse(requestId: UUID, status: Int, response: String)
}