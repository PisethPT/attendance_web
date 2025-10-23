package org.itstep.attendance_web.entities

import jakarta.persistence.*

@Entity
@Table(name = "subjects")
data class StudentSubject(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val subjectId: Long? = null,

    val subjectNameEn: String,
    val subjectNameKh: String
)
