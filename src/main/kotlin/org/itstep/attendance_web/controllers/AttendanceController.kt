package org.itstep.attendance_web.controllers

import org.itstep.attendance_web.entities.*
import org.itstep.attendance_web.repositories.*
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/attendance")
class AttendanceController(
    private val attendanceRepository: AttendanceRepository,
    private val attendanceDetailRepository: AttendanceDetailRepository,
    private val subjectRepository: SubjectRepository,
    private val timeslotRepository: TimeslotRepository,
    private val studentRepository: StudentRepository
) {

    @PostMapping("/create-or-get")
    @PreAuthorize("hasRole('ADMIN')")
    fun createOrGetAttendance(
        @RequestParam subjectId: Long,
        @RequestParam timeslotId: Long,
        @RequestParam date: String
    ): ResponseEntity<Any> {
        val parsedDate = LocalDate.parse(date)
        val subject = subjectRepository.findById(subjectId)
            .orElseThrow { Exception("Subject not found") }
        val timeslot = timeslotRepository.findById(timeslotId)
            .orElseThrow { Exception("Timeslot not found") }

        val attendance = attendanceRepository
            .findBySubject_SubjectIdAndTimeslot_TimeSlotIdAndDate(subjectId, timeslotId, parsedDate)
            ?: attendanceRepository.save(
                Attendance(subject = subject, timeslot = timeslot, date = parsedDate)
            )

        return ResponseEntity.ok(attendance)
    }


    @PostMapping("/mark")
    @PreAuthorize("hasRole('ADMIN', 'TEACHER')")
    fun markStudentAttendance(
        @RequestParam attendanceId: Long,
        @RequestParam studentId: Long,
        @RequestParam status: String
    ): ResponseEntity<Any> {
        val attendance = attendanceRepository.findById(attendanceId)
            .orElseThrow { Exception("Attendance not found") }

        val student = studentRepository.findById(studentId)
            .orElseThrow { Exception("Student not found") }

        var detail = attendanceDetailRepository.findByAttendance_AttendanceId(attendanceId)
            .find { it.student.studentId == studentId }

        if (detail == null) {
            detail = AttendanceDetail(attendance = attendance, student = student, status = status)
        } else {
            detail.status = status
        }

        attendanceDetailRepository.save(detail)
        return ResponseEntity.ok(mapOf("message" to "Attendance updated", "studentId" to studentId, "status" to status))
    }

    @GetMapping("/group/{groupId}")
    @PreAuthorize("hasRole('ADMIN', 'TEACHER')")
    fun getAttendanceByGroup(
        @PathVariable groupId: Long,
        @RequestParam(required = false) date: String?
    ): ResponseEntity<Any> {
        val students = studentRepository.findByGroup_GroupId(groupId)
        val gr
        if (students.isEmpty()) return ResponseEntity.status(404).body(mapOf("message" to "No students found"))

        val parsedDate = date?.let { LocalDate.parse(it) }

        val records = attendanceRepository.findAll()
            .filter { parsedDate == null || it.date == parsedDate }

        val response = records.map { record ->
            mapOf(
                "attendanceId" to record.attendanceId,
                "date" to record.date,
                "subject" to mapOf(
                    "id" to record.subject.subjectId,
                    "name" to record.subject.subjectNameEn
                ),
                "timeslot" to mapOf(
                    "id" to record.timeslot.timeSlotId,
                    "name" to "${record.timeslot.timeSlotStart} - ${record.timeslot.timeSlotEnd}"
                ),
                "students" to students.map { student ->
                    val detail = attendanceDetailRepository.findByAttendance_AttendanceId(record.attendanceId!!)
                        .find { it.student.studentId == student.studentId }
                    mapOf(
                        "id" to student.studentId,
                        "nameEn" to student.studentNameEn,
                        "status" to (detail?.status ?: "Not Marked")
                    )
                }
            )
        }

        return ResponseEntity.ok(response)
    }

    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    fun getStudentAttendance(
        authentication: Authentication,
        @RequestParam(required = false) subjectId: Long?,
        @RequestParam(required = false) date: String?
    ): ResponseEntity<Any> {
        return try {
            val currentUser = authentication.principal as User

            val student = studentRepository.findByUser(currentUser)
                ?: return ResponseEntity.status(404).body(mapOf("message" to "Student not found"))

            var attendanceList = attendanceDetailRepository.findAll()
                .filter { it.student.studentId == student.studentId }

            if (subjectId != null) {
                attendanceList = attendanceList.filter { it.attendance.subject.subjectId == subjectId }
            }
            if (date != null) {
                attendanceList = attendanceList.filter { it.date.toString() == date }
            }

            val data = attendanceList.map { record ->
                mapOf(
                    "attendanceDetailId" to record.attendanceDetailId,
                    "date" to record.date,
                    "status" to record.status,
                    "subject" to mapOf(
                        "id" to record.attendance.subject.subjectId,
                        "name" to record.attendance.subject.subjectNameEn
                    ),
                    "timeslot" to mapOf(
                        "id" to record.attendance.timeslot.timeSlotId,
                        "time" to "${record.attendance.timeslot.timeSlotStart} - ${record.attendance.timeslot.timeSlotEnd}"
                    ),
                    "group" to mapOf(
                        "id" to record.student.group?.groupId,
                        "name" to record.student.group?.groupName
                    )
                )
            }

            val response = mapOf(
                "student" to mapOf(
                    "id" to student.studentId,
                    "nameEn" to student.studentNameEn,
                    "nameKh" to student.studentNameKh,
                    "group" to student.group?.groupName
                ),
                "totalRecords" to data.size,
                "records" to data
            )

            ResponseEntity.ok(response)

        } catch (ex: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (ex.message ?: "Something went wrong")))
        }
    }

}
