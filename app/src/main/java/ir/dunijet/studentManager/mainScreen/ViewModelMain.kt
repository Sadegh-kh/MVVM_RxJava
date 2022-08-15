package ir.dunijet.studentManager.mainScreen

import io.reactivex.Completable
import io.reactivex.Single
import ir.dunijet.studentManager.model.MainRepository
import ir.dunijet.studentManager.model.Student

class ViewModelMain {
    private val mainRepository=MainRepository()

    fun getAllStudent():Single<List<Student>>{
        return mainRepository.getAllStudents()
    }
    fun deleteStudent(id: Int):Completable{
        return mainRepository.deleteStudent(id)
    }
}