package ir.dunijet.studentManager.addStudent

import io.reactivex.Completable
import ir.dunijet.studentManager.model.MainRepository
import ir.dunijet.studentManager.model.Student

class AddStudentViewModel {

    private val mainRepository=MainRepository()

    fun insertStudent(student: Student):Completable{
        return mainRepository.insertStudent(student)
    }
    fun updateStudent(student: Student):Completable{
        return mainRepository.updateStudent(student)
    }
}