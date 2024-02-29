package com.colabear754.interceptor_demo.log.filter

import com.colabear754.interceptor_demo.log.wrapper.MultipleReadableRequestWrapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingResponseWrapper

@Component
class LoggingFilter : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val requestWrapper = MultipleReadableRequestWrapper(request)
        val responseWrapper = ContentCachingResponseWrapper(response)
        filterChain.doFilter(requestWrapper, responseWrapper)
        responseWrapper.copyBodyToResponse()
    }
}