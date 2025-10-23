package org.itstep.attendance_web.repositories

import org.itstep.attendance_web.entities.StudentGroup
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GroupRepository : JpaRepository<StudentGroup, Long>
