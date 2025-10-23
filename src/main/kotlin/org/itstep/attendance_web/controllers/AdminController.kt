package org.itstep.attendance_web.controllers.api

import org.itstep.attendance_web.entities.*
import org.itstep.attendance_web.repositories.*
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
class AdminController(
    private val studentRepository: StudentRepository,
    private val groupRepository: StudentGroupRepository,
    private val subjectRepository: SubjectRepository,
    private val timeslotRepository: TimeslotRepository
) {

    // student endpoints
    @GetMapping("/students")
    fun getAllStudents(): ResponseEntity<Any> =
        ResponseEntity.ok(studentRepository.findAll())

    @PostMapping("/students")
    fun createStudent(@RequestBody student: StudentInfo): ResponseEntity<Any> =
        ResponseEntity.ok(studentRepository.save(student))

    @PutMapping("/students/{id}")
    fun updateStudent(@PathVariable id: Long, @RequestBody updated: StudentInfo): ResponseEntity<Any> {
        val student = studentRepository.findById(id)
        return if (student.isPresent) {
            val s = student.get().copy(
                studentNameEn = updated.studentNameEn,
                studentNameKh = updated.studentNameKh,
                gender = updated.gender,
                imagePath = updated.imagePath,
                group = updated.group
            )
            ResponseEntity.ok(studentRepository.save(s))
        } else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/students/{id}")
    fun deleteStudent(@PathVariable id: Long): ResponseEntity<Any> {
        if (!studentRepository.existsById(id)) return ResponseEntity.notFound().build()
        studentRepository.deleteById(id)
        return ResponseEntity.ok(mapOf("message" to "Student deleted successfully"))
    }


    // group endpoints
    @GetMapping("/groups")
    fun getAllGroups(): ResponseEntity<Any> =
        ResponseEntity.ok(groupRepository.findAll())

    @PostMapping("/groups")
    fun createGroup(@RequestBody group: StudentGroup): ResponseEntity<Any> =
        ResponseEntity.ok(groupRepository.save(group))

    @PutMapping("/groups/{id}")
    fun updateGroup(@PathVariable id: Long, @RequestBody updated: StudentGroup): ResponseEntity<Any> {
        val group = groupRepository.findById(id)
        return if (group.isPresent) {
            val g = group.get().copy(
                groupName = updated.groupName,
                groupShort = updated.groupShort
            )
            ResponseEntity.ok(groupRepository.save(g))
        } else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/groups/{id}")
    fun deleteGroup(@PathVariable id: Long): ResponseEntity<Any> {
        if (!groupRepository.existsById(id)) return ResponseEntity.notFound().build()
        groupRepository.deleteById(id)
        return ResponseEntity.ok(mapOf("message" to "Group deleted successfully"))
    }


    // subject endpoints
    @GetMapping("/subjects")
    fun getAllSubjects(): ResponseEntity<Any> =
        ResponseEntity.ok(subjectRepository.findAll())

    @PostMapping("/subjects")
    fun createSubject(@RequestBody subject: StudentSubject): ResponseEntity<Any> =
        ResponseEntity.ok(subjectRepository.save(subject))

    @PutMapping("/subjects/{id}")
    fun updateSubject(@PathVariable id: Long, @RequestBody updated: StudentSubject): ResponseEntity<Any> {
        val subject = subjectRepository.findById(id)
        return if (subject.isPresent) {
            val s = subject.get().copy(
                subjectNameEn = updated.subjectNameEn,
                subjectNameKh = updated.subjectNameKh
            )
            ResponseEntity.ok(subjectRepository.save(s))
        } else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/subjects/{id}")
    fun deleteSubject(@PathVariable id: Long): ResponseEntity<Any> {
        if (!subjectRepository.existsById(id)) return ResponseEntity.notFound().build()
        subjectRepository.deleteById(id)
        return ResponseEntity.ok(mapOf("message" to "Subject deleted successfully"))
    }


    // timeslot endpoints
    @GetMapping("/timeslots")
    fun getAllTimeslots(): ResponseEntity<Any> =
        ResponseEntity.ok(timeslotRepository.findAll())

    @PostMapping("/timeslots")
    fun createTimeslot(@RequestBody timeslot: Timeslot): ResponseEntity<Any> =
        ResponseEntity.ok(timeslotRepository.save(timeslot))

    @PutMapping("/timeslots/{id}")
    fun updateTimeslot(@PathVariable id: Long, @RequestBody updated: Timeslot): ResponseEntity<Any> {
        val slot = timeslotRepository.findById(id)
        return if (slot.isPresent) {
            val t = slot.get().copy(
                timeSlotStart = updated.timeSlotStart,
                timeSlotEnd = updated.timeSlotEnd
            )
            ResponseEntity.ok(timeslotRepository.save(t))
        } else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/timeslots/{id}")
    fun deleteTimeslot(@PathVariable id: Long): ResponseEntity<Any> {
        if (!timeslotRepository.existsById(id)) return ResponseEntity.notFound().build()
        timeslotRepository.deleteById(id)
        return ResponseEntity.ok(mapOf("message" to "Timeslot deleted successfully"))
    }
}
