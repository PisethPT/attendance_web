package org.itstep.attendance_web.entities

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "attendance_detail")
data class AttendanceDetail(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val attendanceDetailId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "attendance_id", nullable = false)
    val attendance: Attendance,

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    val student: StudentInfo,

    var status: String = "ABSENT",

    val date: LocalDate = LocalDate.now()
)
