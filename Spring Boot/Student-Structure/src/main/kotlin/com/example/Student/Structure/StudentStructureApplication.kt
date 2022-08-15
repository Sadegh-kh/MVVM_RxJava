package com.example.Student.Structure

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories
class StudentStructureApplication

fun main(args: Array<String>) {
	runApplication<StudentStructureApplication>(*args)
}
