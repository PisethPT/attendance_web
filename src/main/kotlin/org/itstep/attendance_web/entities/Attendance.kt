package org.itstep.attendance_web.entities

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "attendance_records")
data class Attendance(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val attendanceId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    val subject: StudentSubject,

    @ManyToOne
    @JoinColumn(name = "timeslot_id", nullable = false)
    val timeslot: Timeslot,

    val date: LocalDate = LocalDate.now()
)
