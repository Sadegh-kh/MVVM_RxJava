package ir.dunijet.studentManager.addStudent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.JsonObject
import ir.dunijet.studentManager.databinding.ActivityMain2Binding
import ir.dunijet.studentManager.mainScreen.BASE_URL
import ir.dunijet.studentManager.net.ApiService
import ir.dunijet.studentManager.recycler.Student
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    lateinit var apiService: ApiService
    var isInserting = true
    var id =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain2)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.edtFirstName.requestFocus()

        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        val testMode = intent.getParcelableExtra<Student>("student")
        isInserting = (testMode == null)

        if (!isInserting) {

            binding.btnDone.text = "update"

            val dataFromIntent = intent.getParcelableExtra<Student>("student")!!
            id=dataFromIntent.id!!
            binding.edtScore.setText(dataFromIntent.score.toString())
            binding.edtCourse.setText(dataFromIntent.course)
            binding.edtFirstName.setText(dataFromIntent.firstName)
            binding.edtLastName.setText(dataFromIntent.lastName)

        }

        binding.btnDone.setOnClickListener {

            if (isInserting) {
                addNewStudent()
            } else {
                updateStudent(id)
            }

        }


    }

    private fun updateStudent(id: Int) {
        val firstName = binding.edtFirstName.text.toString()
        val lastName = binding.edtLastName.text.toString()
        val score = binding.edtScore.text.toString()
        val course = binding.edtCourse.text.toString()

        if (
            firstName.isNotEmpty() &&
            lastName.isNotEmpty() &&
            course.isNotEmpty() &&
            score.isNotEmpty()
        ) {

            val jsonStudentObject= JsonObject()
            jsonStudentObject.addProperty("id",id)
            jsonStudentObject.addProperty("firstName",firstName)
            jsonStudentObject.addProperty("lastName",lastName)
            jsonStudentObject.addProperty("course",course)
            jsonStudentObject.addProperty("score",score)


            apiService
                .updateStudent(id, jsonStudentObject)
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {

                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                    }
                })

            Toast.makeText(this, "update finished!", Toast.LENGTH_SHORT).show()
            onBackPressed()


        } else {
            Toast.makeText(this, "لطفا اطلاعات را کامل وارد کنید", Toast.LENGTH_SHORT).show()
        }

    }

    private fun addNewStudent() {

        val firstName = binding.edtFirstName.text.toString()
        val lastName = binding.edtLastName.text.toString()
        val score = binding.edtScore.text.toString()
        val course = binding.edtCourse.text.toString()
        val id = intent.getIntExtra("listSize",0)

        if (
            firstName.isNotEmpty() &&
            lastName.isNotEmpty() &&
            course.isNotEmpty() &&
            score.isNotEmpty()
        ) {

            val jsonStudentObject= JsonObject()
            jsonStudentObject.addProperty("id",id)
            jsonStudentObject.addProperty("firstName",firstName)
            jsonStudentObject.addProperty("lastName",lastName)
            jsonStudentObject.addProperty("course",course)
            jsonStudentObject.addProperty("score",score)

            apiService.insertStudent(jsonStudentObject).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {

                }

                override fun onFailure(call: Call<String>, t: Throwable) {}
            })

            Toast.makeText(this, "insert finished!", Toast.LENGTH_SHORT).show()
            onBackPressed()

        } else {
            Toast.makeText(this, "لطفا اطلاعات را کامل وارد کنید", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return true
    }

}