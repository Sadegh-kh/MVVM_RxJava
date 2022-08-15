package ir.dunijet.studentManager.mainScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ir.dunijet.studentManager.addStudent.AddStudentActivity
import ir.dunijet.studentManager.databinding.ActivityMainBinding
import ir.dunijet.studentManager.model.Student
import ir.dunijet.studentManager.recycler.StudentAdapter
import ir.dunijet.studentManager.util.Constants
import ir.dunijet.studentManager.util.asyncRequest
import ir.dunijet.studentManager.util.showToast


class MainActivity : AppCompatActivity(), StudentAdapter.StudentEvent {
    lateinit var binding: ActivityMainBinding
    lateinit var myAdapter: StudentAdapter
    private var compositeDisposable = CompositeDisposable()
    lateinit var mainViewModel: MainViewModel
    private var listSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)
        mainViewModel = MainViewModel()

        initUi()
    }

    private fun initUi() {

        binding.btnAddStudent.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            intent.putExtra(Constants.STUDENT_INSERT_KEY, listSize + 1)
            startActivity(intent)
        }

        //Progress Bar=>
        val disposableProgressBar=mainViewModel.progressBarSubject.subscribe{
            if (it){
                binding.progressLoadStudentList.visibility=View.VISIBLE
            }else{
                binding.progressLoadStudentList.visibility=View.GONE
            }
        }
        compositeDisposable.add(disposableProgressBar)
    }

    override fun onResume() {
        super.onResume()

        mainViewModel
            .getAllStudent()
            .asyncRequest()
            .subscribe(object : SingleObserver<List<Student>> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: List<Student>) {
                    setDataToRecycler(t)
                }

                override fun onError(e: Throwable) {
                    Log.v("observerError", e.message!!)
                }
            })
    }
    fun setDataToRecycler(data: List<Student>) {
        val myData = ArrayList(data)
        listSize=data.size
        myAdapter = StudentAdapter(myData, this)
        binding.recyclerMain.adapter = myAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun onItemClicked(student: Student, position: Int) {
        updateDataInServer(student, position)
    }
    private fun updateDataInServer(student: Student, position: Int) {

        val intent = Intent(this, AddStudentActivity::class.java)
        intent.putExtra(Constants.STUDENT_UPDATE_KEY, student)
        startActivity(intent)

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

        myAdapter.removeItem(student, position)

        mainViewModel
            .deleteStudent(student.id!!)
            .asyncRequest()
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onComplete() {
                    showToast("Delete item success ")
                }

                override fun onError(e: Throwable) {
                    showToast("i can't delete this item because : ${e.message}")
                }
            })


    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }


}