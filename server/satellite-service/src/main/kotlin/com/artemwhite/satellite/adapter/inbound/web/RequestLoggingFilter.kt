package com.artemwhite.satellite.adapter.inbound.web

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

@Component
class RequestLoggingFilter : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(RequestLoggingFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val correlationId = request.getHeader(CORRELATION_HEADER) ?: UUID.randomUUID().toString()
        MDC.put("correlationId", correlationId)
        response.setHeader(CORRELATION_HEADER, correlationId)

        val startNanos = System.nanoTime()
        try {
            filterChain.doFilter(request, response)
        } finally {
            val durationMillis = (System.nanoTime() - startNanos) / 1_000_000
            val target = request.queryString
                ?.let { "${request.requestURI}?$it" }
                ?: request.requestURI
            log.info("{} {} -> {} ({} ms)", request.method, target, response.status, durationMillis)
            MDC.remove("correlationId")
        }
    }

    companion object {
        private const val CORRELATION_HEADER = "X-Request-Id"
    }
}
