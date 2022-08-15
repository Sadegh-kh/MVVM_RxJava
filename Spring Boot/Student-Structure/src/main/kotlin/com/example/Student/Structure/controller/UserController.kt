package com.example.Student.Structure.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class UserController {

    @RequestMapping("/login",method = [RequestMethod.GET])
    fun openLoginPage(): String {
        return "login"
    }

}