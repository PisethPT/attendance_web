package org.itstep.attendance_web.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
data class TokenRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val email: String,
    val requestedAt: LocalDateTime
)

interface TokenRequestRepository : org.springframework.data.jpa.repository.JpaRepository<TokenRequest, Long>
