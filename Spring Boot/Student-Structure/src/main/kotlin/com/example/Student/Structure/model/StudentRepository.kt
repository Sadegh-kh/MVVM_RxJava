package com.example.Student.Structure.model

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentRepository: CrudRepository<Student,Int> {
}