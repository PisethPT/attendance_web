package org.itstep.attendance_web.repositories

import org.itstep.attendance_web.entities.AttendanceDetail
import org.itstep.attendance_web.entities.StudentInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AttendanceDetailRepository : JpaRepository<AttendanceDetail, Long> {

    fun findByAttendance_AttendanceId(attendanceId: Long): List<AttendanceDetail>
    fun findByStudent(student: StudentInfo): List<AttendanceDetail>
    fun findByAttendance_AttendanceIdAndStudent_StudentId(attendanceId: Long, studentId: Long): AttendanceDetail?

    @Query("""
        SELECT ad FROM AttendanceDetail ad 
        WHERE ad.student.studentId = :studentId 
        AND ad.attendance.subject.subjectId = :subjectId
        ORDER BY ad.date ASC
    """)
    fun findStudentAttendanceForSubject(
        @Param("studentId") studentId: Long,
        @Param("subjectId") subjectId: Long
    ): List<AttendanceDetail>

    // 🧩 Optional: Count attendance summary for a student
    @Query("""
        SELECT COUNT(ad) FROM AttendanceDetail ad 
        WHERE ad.student.studentId = :studentId 
        AND ad.status = :status
    """)
    fun countByStudentAndStatus(
        @Param("studentId") studentId: Long,
        @Param("status") status: String
    ): Long
}
