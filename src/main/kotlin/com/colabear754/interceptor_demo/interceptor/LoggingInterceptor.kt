package com.colabear754.interceptor_demo.interceptor

import com.colabear754.interceptor_demo.log.entity.ApiLog
import com.colabear754.interceptor_demo.log.repository.ApiLogRepository
import com.colabear754.interceptor_demo.log.wrapper.MultipleReadableRequestWrapper
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.util.ContentCachingResponseWrapper
import java.net.InetAddress
import java.util.*

@Component
class LoggingInterceptor(private val apiLogRepository: ApiLogRepository) : HandlerInterceptor {
    private val objectMapper = ObjectMapper()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod) return true
        val requestWrapper = if (request is MultipleReadableRequestWrapper) request else MultipleReadableRequestWrapper(request)
        val requestParams = request.parameterMap.map { (key, value) -> "$key=${value.contentToString()}" }.joinToString(", ")
        val requestBody = objectMapper.readTree(requestWrapper.contents)
        val requestId = UUID.randomUUID()
        request.setAttribute("requestId", requestId)
        apiLogRepository.save(
            ApiLog(
                requestId = requestId,
                serverIp = InetAddress.getLocalHost().hostAddress,
                requestUrl = request.requestURL.toString(),
                requestMethod = request.method,
                clientIp = request.getHeader("X-Forwarded-For")?.takeIf { it.isNotBlank() } ?: request.remoteAddr,
                request = if (requestParams.isNotBlank()) "$requestParams, $requestBody" else "$requestBody"
            )
        )
        return true
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        if (handler !is HandlerMethod) return
        val responseWrapper = if (response is ContentCachingResponseWrapper) response else ContentCachingResponseWrapper(response)
        val requestId = request.getAttribute("requestId") as UUID
        apiLogRepository.updateResponse(requestId, response.status, responseWrapper.contentAsByteArray.toString(Charsets.UTF_8))
    }
}