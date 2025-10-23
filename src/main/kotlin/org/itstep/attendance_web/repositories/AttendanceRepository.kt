package org.itstep.attendance_web.repositories

import org.itstep.attendance_web.entities.Attendance
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface AttendanceRepository : JpaRepository<Attendance, Long> {
    fun findBySubject_SubjectIdAndTimeslot_TimeSlotIdAndDate(subjectId: Long, timeSlotId: Long, date: LocalDate): Attendance?
    fun findBySubject_SubjectIdAndDate(subjectId: Long, date: LocalDate): List<Attendance>
}
