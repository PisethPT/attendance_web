package org.itstep.attendance_web.controllers.api

import org.itstep.attendance_web.entities.User
import org.itstep.attendance_web.entities.UserRepository
import org.itstep.attendance_web.entities.AuthRequest
import org.itstep.attendance_web.entities.Response
import org.itstep.attendance_web.entities.TokenRequest
import org.itstep.attendance_web.entities.TokenRequestRepository
import org.itstep.attendance_web.services.JwtService
import org.itstep.attendance_web.services.UserInfoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/users")
class UserApiController {

    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var service: UserInfoService

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var tokenRequestRepository: TokenRequestRepository

    @GetMapping("/all")
    fun listUsers(): List<User> = userRepository.findAll()

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<User> {
        val user = userRepository.findById(id)
        return if (user.isPresent) ResponseEntity.ok(user.get())
        else ResponseEntity.notFound().build()
    }

    @PostMapping("/addNewUser")
    fun addNewUser(@RequestBody userInfo: User): Response = service.addUser(userInfo)


    @PostMapping("/generateToken")
    fun authenticateAndGetToken(@RequestBody authRequest: AuthRequest): String {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(authRequest.email, authRequest.password)
        )
        if (authentication.isAuthenticated) {
            val userDetails = authentication.principal as UserDetails
            val token = jwtService.generateToken(userDetails)

            // Save token request info
            val tokenRequest = TokenRequest(
                email = authRequest.email,
                requestedAt = LocalDateTime.now()
            )
            tokenRequestRepository.save(tokenRequest)

            return token
        } else {
            throw UsernameNotFoundException("Invalid user request!")
        }
    }

    @GetMapping("/me")
    fun getMe(): ResponseEntity<User> {
        val principal = SecurityContextHolder.getContext().authentication?.principal
        return if (principal is org.springframework.security.core.userdetails.User) {
            val user = userRepository.findByEmail(principal.username)
            ResponseEntity.ok(user!!)
        } else ResponseEntity.status(403).build()
    }


    @GetMapping("/tokenRequests")
    fun getAllTokenRequests(): List<TokenRequest> = tokenRequestRepository.findAll()
}
