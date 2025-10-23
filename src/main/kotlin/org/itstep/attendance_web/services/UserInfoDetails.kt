package org.itstep.attendance_web.services

import org.itstep.attendance_web.entities.Role
import org.itstep.attendance_web.entities.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserInfoDetails(private val user: User) : UserDetails {

    override fun getUsername(): String = user.email
    override fun getPassword(): String = user.password

    override fun getAuthorities(): Collection<GrantedAuthority> =
        listOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))

    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true

    // Helper methods
    fun getId(): Long? = user.id
    fun getName(): String = user.name
    fun getRole(): Role = user.role

    override fun toString(): String =
        "UserInfoDetails(id=${user.id}, name=${user.name}, email=${user.email}, role=${user.role})"
}
