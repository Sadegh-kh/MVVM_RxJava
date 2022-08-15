package ir.dunijet.studentManager.util

import com.google.gson.JsonObject
import ir.dunijet.studentManager.model.Student

fun studentToJsonObject(student:Student):JsonObject{

    val jsonStudentObject= JsonObject()
    jsonStudentObject.addProperty("id",student.id)
    jsonStudentObject.addProperty("firstName",student.firstName)
    jsonStudentObject.addProperty("lastName",student.lastName)
    jsonStudentObject.addProperty("course",student.course)
    jsonStudentObject.addProperty("score",student.score)

    return jsonStudentObject
}