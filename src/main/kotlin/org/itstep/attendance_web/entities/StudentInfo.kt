package org.itstep.attendance_web.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
@Table(name = "students")
data class StudentInfo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val studentId: Long? = null,

    val studentNameEn: String,
    val studentNameKh: String,
    val gender: Boolean,
    val imagePath: String? = null,

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonBackReference
    val group: StudentGroup? = null,

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: User
)
