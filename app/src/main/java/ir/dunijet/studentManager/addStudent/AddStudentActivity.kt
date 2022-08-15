package ir.dunijet.studentManager.addStudent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.JsonObject
import io.reactivex.CompletableObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ir.dunijet.studentManager.databinding.ActivityMain2Binding
import ir.dunijet.studentManager.model.Student
import ir.dunijet.studentManager.util.Constants
import ir.dunijet.studentManager.util.asyncRequest
import ir.dunijet.studentManager.util.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStudentActivity : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    lateinit var addStudentViewModel:AddStudentViewModel
    private var compositeDisposable=CompositeDisposable()
    private var isInserting = true
    var id =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain2)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        addStudentViewModel=AddStudentViewModel()

        initUi()

    }

    private fun initUi() {
        binding.edtFirstName.requestFocus()

        val testMode = intent.getParcelableExtra<Student>(Constants.STUDENT_UPDATE_KEY)
        isInserting = (testMode == null)

        if (!isInserting) {
            completeTextViewsForUpdate()
        }

        binding.btnDone.setOnClickListener {

            if (isInserting) {
                addNewStudent()
            } else {
                updateStudent(id)
            }

        }

    }

    private fun completeTextViewsForUpdate() {

        binding.btnDone.text = "update"

        val dataFromIntent = intent.getParcelableExtra<Student>(Constants.STUDENT_UPDATE_KEY)!!
        id=dataFromIntent.id!!
        binding.edtScore.setText(dataFromIntent.score)
        binding.edtCourse.setText(dataFromIntent.course)
        binding.edtFirstName.setText(dataFromIntent.firstName)
        binding.edtLastName.setText(dataFromIntent.lastName)

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

            addStudentViewModel.updateStudent(Student(id,firstName, lastName, course, score))
                .asyncRequest()
                .subscribe(object :CompletableObserver{
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onComplete() {
                        showToast("update finished!")
                        onBackPressed()
                    }

                    override fun onError(e: Throwable) {
                        showToast("Failed to update because ${e.message}")
                    }
                })


        } else {
            showToast("لطفا اطلاعات را کامل وارد کنید")
        }

    }

    private fun addNewStudent() {

        val firstName = binding.edtFirstName.text.toString()
        val lastName = binding.edtLastName.text.toString()
        val score = binding.edtScore.text.toString()
        val course = binding.edtCourse.text.toString()
        val id = intent.getIntExtra(Constants.STUDENT_INSERT_KEY,0)

        if (
            firstName.isNotEmpty() &&
            lastName.isNotEmpty() &&
            course.isNotEmpty() &&
            score.isNotEmpty()
        ) {
            addStudentViewModel.insertStudent(Student(id, firstName, lastName, course, score))
                .asyncRequest()
                .subscribe(object :CompletableObserver{
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onComplete() {
                        showToast("insert finished!")
                        onBackPressed()
                    }

                    override fun onError(e: Throwable) {
                        showToast("Failed to Insert new student because ${e.message}")
                    }
                })

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

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

}