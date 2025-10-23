package org.itstep.attendance_web.controllers.api

import java.time.LocalDate

data class AttendanceRequest(
    val studentId: Long,
    val subjectId: Long,
    val timeslotId: Long,
    val date: LocalDate,
    val status: String
)
