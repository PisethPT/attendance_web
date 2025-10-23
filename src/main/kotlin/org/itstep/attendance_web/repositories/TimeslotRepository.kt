package org.itstep.attendance_web.repositories

import org.itstep.attendance_web.entities.Timeslot
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TimeslotRepository : JpaRepository<Timeslot, Long>
