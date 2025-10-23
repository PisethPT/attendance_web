package org.itstep.attendance_web.controllers

import org.itstep.attendance_web.entities.User
import org.itstep.attendance_web.entities.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/users")
class UserController {

    @Autowired
    private lateinit var userRepository: UserRepository

    @GetMapping
    fun listUsers(): ModelAndView {
        val modelAndView = ModelAndView("user/list")
        modelAndView.addObject("users", userRepository.findAll())
        return modelAndView
    }
}