package ir.dunijet.studentManager.recycler

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Student(

    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val firstName: String,
    val lastName:String,
    val course: String,
    val score: String
) :Parcelable