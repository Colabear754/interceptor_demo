package com.colabear754.interceptor_demo

import com.colabear754.interceptor_demo.log.repository.ApiLogRepository
import com.colabear754.interceptor_demo.user.repository.UserInfoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@SpringBootTest
@AutoConfigureMockMvc
class LoggingTest @Autowired constructor(
    private val apiLogRepository: ApiLogRepository,
    private val userInfoRepository: UserInfoRepository,
    private val mock: MockMvc
) {
    @BeforeEach
    fun reset() {
        apiLogRepository.deleteAllInBatch()
        userInfoRepository.deleteAllInBatch()
    }

    @Test
    fun `요청 성공 로그`() {
        // given
        // when
        mock.perform(
            post("/sign-up")
                .contentType("application/json")
                .content("{\"name\":\"colabear754\",\"email\":\"demo@example.com\",\"password\":\"1234\"}")
        )
        // then
        val log = apiLogRepository.findAll().last()
        assertThat(log.requestUrl).isEqualTo("http://localhost/sign-up")
        assertThat(log.request).isEqualTo("{\"name\":\"colabear754\",\"email\":\"demo@example.com\",\"password\":\"1234\"}")
        assertThat(log.responseStatus).isEqualTo(200)
        assertThat(log.response).isEqualTo("{\"name\":\"colabear754\",\"email\":\"demo@example.com\"}")
        println(log)
    }

    @Test
    fun `요청 실패 로그(Bad Request)`() {
        // given
        // when
        mock.perform(
            post("/sign-up")
                .contentType("application/json")
                .content("{\"name\":\"colabear754\",\"email\":\"demo@example.com\",\"password\":\"1234\"}")
        )
        mock.perform(
            post("/sign-up")
                .contentType("application/json")
                .content("{\"name\":\"colabear754\",\"email\":\"demo@example.com\",\"password\":\"1234\"}")
        )
        // then
        val log = apiLogRepository.findAll().last()
        assertThat(log.requestUrl).isEqualTo("http://localhost/sign-up")
        assertThat(log.request).isEqualTo("{\"name\":\"colabear754\",\"email\":\"demo@example.com\",\"password\":\"1234\"}")
        assertThat(log.responseStatus).isEqualTo(400)
        assertThat(log.response).isEqualTo("이미 사용중인 이메일입니다.")
    }
}