package ir.dunijet.studentManager.net

import com.google.gson.JsonObject
import io.reactivex.Single
import ir.dunijet.studentManager.recycler.Student
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @GET("/student")
    fun getAllStudent():Single<List<Student>>

    @POST("/student")
    fun insertStudent(@Body body:JsonObject):Call<String>

    @PUT("/student/update{Id}")
    fun updateStudent(@Path("Id")id:Int,@Body jsonObject: JsonObject):Call<String>

    @DELETE("/student/delete{Id}")
    fun deleteStudent(@Path("Id")id:Int):Call<String>
}