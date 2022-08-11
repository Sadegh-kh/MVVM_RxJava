package ir.dunijet.studentManager.mainScreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ir.dunijet.studentManager.addStudent.MainActivity2
import ir.dunijet.studentManager.databinding.ActivityMainBinding
import ir.dunijet.studentManager.net.ApiService
import ir.dunijet.studentManager.recycler.Student
import ir.dunijet.studentManager.recycler.StudentAdapter
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


const val BASE_URL = "http://192.168.213.1:8080"

class MainActivity : AppCompatActivity(), StudentAdapter.StudentEvent {
    lateinit var binding: ActivityMainBinding
    lateinit var myAdapter: StudentAdapter
    lateinit var apiService: ApiService
    lateinit var disposable: Disposable
    var listSize=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)

        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        binding.btnAddStudent.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            intent.putExtra("listSize",listSize+1)
            startActivity(intent)
        }


    }

    override fun onResume() {
        super.onResume()

        getDataFromApi()
    }

    private fun getDataFromApi() {

        apiService
            .getAllStudent()
            .subscribeOn( Schedulers.io() )
            .observeOn( AndroidSchedulers.mainThread() )
            .subscribe( object :SingleObserver<List<Student>> {

                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onSuccess(t: List<Student>) {
                    setDataToRecycler(t)
                    listSize=t.size

                }

                override fun onError(e: Throwable) {
                    Log.v("testLog" , e.message!!)
                }

            } )

    }

    fun setDataToRecycler(data: List<Student>) {
        val myData = ArrayList(data)
        myAdapter = StudentAdapter(myData, this)
        binding.recyclerMain.adapter = myAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun onItemClicked(student: Student, position: Int) {
        updateDataInServer(student, position)
    }

    override fun onItemLongClicked(student: Student, position: Int) {
        val dialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        dialog.contentText = "Delete this Item?"
        dialog.cancelText = "cancel"
        dialog.confirmText = "confirm"
        dialog.setOnCancelListener {
            dialog.dismiss()
        }
        dialog.setConfirmClickListener {

            deleteDataFromServer(student, position)
            dialog.dismiss()

        }
        dialog.show()
    }

    private fun deleteDataFromServer(student: Student, position: Int) {

        apiService
            .deleteStudent(student.id!!)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {

                }

                override fun onFailure(call: Call<String>, t: Throwable) {

                }
            })

        myAdapter.removeItem(student, position)

    }

    private fun updateDataInServer(student: Student, position: Int) {

        val intent = Intent(this, MainActivity2::class.java)
        intent.putExtra("student", student)
        startActivity(intent)

    }


}