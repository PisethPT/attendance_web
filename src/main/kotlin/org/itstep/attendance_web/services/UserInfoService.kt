package org.itstep.attendance_web.services

import jakarta.persistence.EnumType
import org.itstep.attendance_web.entities.Response
import org.itstep.attendance_web.entities.User
import org.itstep.attendance_web.entities.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserInfoService(
    private val repository: UserRepository,
    private val encoder: PasswordEncoder
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user = repository.findByEmail(email)
            ?: throw UsernameNotFoundException("User not found with email: $email")
        val authorities: Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority(user.role.toString()))
        return org.springframework.security.core.userdetails.User(user.email, user.password, authorities)
    }

    fun addUser(userInfo: User): Response {
        userInfo.password = encoder.encode(userInfo.password)
        repository.save(userInfo)
        return Response (
            statusCode = 200,
            message = "User added successfully!"
        )
    }
}
