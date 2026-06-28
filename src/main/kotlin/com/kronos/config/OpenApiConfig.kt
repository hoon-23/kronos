package com.kronos.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openAPI(): OpenAPI = OpenAPI()
        .info(
            Info()
                .title("Kronos API")
                .description("자연어로 일정을 등록하는 AI 연동 일정 관리 서비스")
                .version("v1.0.0")
        )
}
