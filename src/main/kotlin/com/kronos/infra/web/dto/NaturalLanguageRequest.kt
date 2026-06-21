package com.kronos.infra.web.dto

import jakarta.validation.constraints.NotBlank

data class NaturalLanguageRequest(
    @field:NotBlank val text: String,
)
