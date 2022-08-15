package com.example.Student.Structure.model

import org.springframework.beans.factory.annotation.Autowired
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Student(
val firstName:String,
val lastName:String,
val course:String,
val score:String,
@Id
var id:Int?=null
){
    constructor():this("","","","")
}