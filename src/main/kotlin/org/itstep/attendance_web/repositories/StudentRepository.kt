package org.itstep.attendance_web.repositories

import org.itstep.attendance_web.entities.StudentInfo
import org.itstep.attendance_web.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentRepository : JpaRepository<StudentInfo, Long>{
    fun findByUserEmail(email: String): StudentInfo?
    fun findByUser(user: User): StudentInfo?
    fun findByGroup_GroupId(groupId: Long): List<StudentInfo>
}
