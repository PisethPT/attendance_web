package org.itstep.attendance_web.controllers.api

import org.itstep.attendance_web.entities.Role
import org.itstep.attendance_web.entities.User
import org.itstep.attendance_web.entities.UserRepository
import org.itstep.attendance_web.services.JwtService
import org.itstep.attendance_web.services.UserInfoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import javax.naming.AuthenticationException

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userInfoService: UserInfoService,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder,
    @Autowired private var userRepository: UserRepository
) {

    @PostMapping("/register")
    fun register(@RequestBody newUser: User): ResponseEntity<Any> {
        newUser.password = passwordEncoder.encode(newUser.password)
        newUser.role = Role.STUDENT // default student only
        userInfoService.addUser(newUser)
        return ResponseEntity.ok(mapOf("message" to "Student registered successfully!"))
    }

    @PostMapping("/forgetPassword")
    fun forgetPassword(@RequestBody existingUser: User): ResponseEntity<Any> {
        val user = userRepository.findByEmail(existingUser.email)
            ?: return ResponseEntity.badRequest().body(mapOf("message" to "No account found with this email"))

        if (existingUser.password.isBlank()) {
            return ResponseEntity.badRequest().body(mapOf("message" to "New password is required"))
        }

        user.password = passwordEncoder.encode(existingUser.password)
        userRepository.save(user)

        return ResponseEntity.ok(mapOf("message" to "Password reset successfully"))
    }

    @PostMapping("/login")
    fun login(@RequestBody auth: Map<String, String>): ResponseEntity<Any> {
        val email = auth["email"] ?: return ResponseEntity.badRequest().body("Missing email")
        val password = auth["password"] ?: return ResponseEntity.badRequest().body("Missing password")

        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(email, password)
            )
        } catch (ex: AuthenticationException) {
            return ResponseEntity.status(401).body(mapOf("error" to "Invalid credentials"))
        }

        val userDetails = userInfoService.loadUserByUsername(email)
        val token = jwtService.generateToken(userDetails)

        return ResponseEntity.ok(
            mapOf(
                "token" to token,
                "role" to userDetails.authorities.map { it.authority }
            )
        )
    }



}
