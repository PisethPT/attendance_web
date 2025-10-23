package org.itstep.attendance_web.entities

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.data.jpa.repository.JpaRepository

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var name: String = "",

    @Column(unique = true, nullable = false)
    var email: String = "",

    @Column(nullable = false)
    var password: String = "",

    @Enumerated(EnumType.STRING)
    var role: Role = Role.STUDENT
)

enum class Role {
    ADMIN,
    TEACHER,
    STUDENT
}

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
}
