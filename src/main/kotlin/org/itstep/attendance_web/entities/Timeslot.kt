package org.itstep.attendance_web.entities

import jakarta.persistence.*

@Entity
@Table(name = "timeslots")
data class Timeslot(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val timeSlotId: Long? = null,

    var timeSlotStart: String,
    var timeSlotEnd: String
)
