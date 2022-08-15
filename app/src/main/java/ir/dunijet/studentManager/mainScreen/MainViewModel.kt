package ir.dunijet.studentManager.mainScreen

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.Action
import io.reactivex.subjects.BehaviorSubject
import ir.dunijet.studentManager.model.MainRepository
import ir.dunijet.studentManager.model.Student
import java.util.concurrent.TimeUnit

class MainViewModel {
    private val mainRepository=MainRepository()
    val progressBarSubject=BehaviorSubject.create<Boolean>()
    fun getAllStudent():Single<List<Student>>{
        progressBarSubject.onNext(true)
        return mainRepository.getAllStudents()
            .delay(4,TimeUnit.SECONDS)
            .doFinally { progressBarSubject.onNext(false) }
    }
    fun deleteStudent(id: Int):Completable{
        return mainRepository.deleteStudent(id)
    }
}