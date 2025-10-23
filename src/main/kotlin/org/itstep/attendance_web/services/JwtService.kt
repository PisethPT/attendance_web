package org.itstep.attendance_web.services

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService {

    private val SECRET_KEY = "nXz8j2Qk3LrV9bHs7Tp4mZqW6xYvC0aR"

    fun extractUsername(token: String): String =
        extractAllClaims(token).subject

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean =
        extractUsername(token) == userDetails.username && !isTokenExpired(token)

    private fun isTokenExpired(token: String): Boolean =
        extractAllClaims(token).expiration.before(Date())

    fun generateToken(userDetails: UserDetails): String {
        val claims: Map<String, Any> = mapOf("roles" to userDetails.authorities.map { it.authority })
        val now = Date()
        val expiry = Date(now.time + 1000 * 60 * 60) // 1 hour
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDetails.username)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY.toByteArray())
            .compact()
    }

    private fun extractAllClaims(token: String): Claims =
        Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY.toByteArray())
            .build()
            .parseClaimsJws(token)
            .body
}

