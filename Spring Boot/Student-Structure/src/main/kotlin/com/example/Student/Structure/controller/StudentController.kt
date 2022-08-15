package com.example.Student.Structure.controller

import com.example.Student.Structure.model.Student
import com.example.Student.Structure.model.StudentRepository
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class StudentController {

    lateinit var studentRepository: StudentRepository

    @Autowired
    fun setRepository(studentReposit: StudentRepository){
        studentRepository=studentReposit
    }



    @GetMapping("/student")
    fun getAllStudents():ResponseEntity<ArrayList<Student>>{

        val studentListFromDatabase=studentRepository.findAll()
        val allStudentList=ArrayList<Student>()

        studentListFromDatabase.forEach{
            allStudentList.add(it)
        }

        return ResponseEntity.ok(allStudentList)
    }


    @PostMapping("/student")
    fun insertStudent(@RequestBody data:String):ResponseEntity<String>{
            val gson=Gson()
            val newStudent = gson.fromJson<Student>(data,Student::class.java)
            studentRepository.save(newStudent)

        return ResponseEntity.ok("Success")
    }

    @PutMapping("/student/update{id}")
    fun updateStudent(@PathVariable id:Int,
    @RequestBody data: String):ResponseEntity<String>{
        val gson=Gson()
        val editStudent:Student=gson.fromJson(data,Student::class.java)
        studentRepository.save(editStudent)

        return ResponseEntity.ok("Success")

    }

    @DeleteMapping("/student/delete{id}")
    fun updateStudent(@PathVariable id:Int):ResponseEntity<String>{

        studentRepository.deleteById(id)
        return ResponseEntity.ok("Deleted")

    }
}