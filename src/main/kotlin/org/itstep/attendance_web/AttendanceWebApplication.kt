package org.itstep.attendance_web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AttendanceWebApplication

fun main(args: Array<String>) {
	runApplication<AttendanceWebApplication>(*args)
}
