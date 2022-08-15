package ir.dunijet.studentManager.model
import io.reactivex.Completable
import io.reactivex.Single
import ir.dunijet.studentManager.util.Constants
import ir.dunijet.studentManager.util.studentToJsonObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainRepository {
    private val apiService:ApiService

    init {
        val retrofit= Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        apiService=retrofit.create(ApiService::class.java)
    }

    fun getAllStudents():Single<List<Student>>{
        return apiService.getAllStudent()
    }

    fun insertStudent(student: Student):Completable{
        val studentJson= studentToJsonObject(student)
        return apiService.insertStudent(studentJson).ignoreElement()
    }
    fun updateStudent(student: Student):Completable{
        val studentJson= studentToJsonObject(student)
        return apiService.updateStudent(student.id!!,studentJson).ignoreElement()
    }
    fun deleteStudent(id: Int):Completable{
        return apiService.deleteStudent(id).ignoreElement()
    }
}