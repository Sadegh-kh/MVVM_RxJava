package ir.dunijet.studentManager.addStudent

import ir.dunijet.studentManager.model.MainRepository
import ir.dunijet.studentManager.model.Student

class AddStudentViewModel {

    private val mainRepository=MainRepository()

    fun insertStudent(student: Student){
        mainRepository.insertStudent(student)
    }
    fun updateStudent(student: Student){
        mainRepository.updateStudent(student)
    }
}