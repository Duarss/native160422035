package com.example.studentproject.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.studentproject.model.Student
import com.example.studentproject.util.FileHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListViewModel(application: Application): AndroidViewModel(application) {
    val studentsLD = MutableLiveData<ArrayList<Student>>()
    val loadingLD = MutableLiveData<Boolean>()
    val errorLD = MutableLiveData<Boolean>()
    val TAG = "volleytag"
    private var queue: RequestQueue?= null

    fun refresh() {
        loadingLD.value = true // progress bar start muncul
        errorLD.value = false // tidak ada error

        // Volley ke API
        queue = Volley.newRequestQueue(getApplication())
        val url = "https://www.jsonkeeper.com/b/LLMW"
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            { // Success
                val sType = object : TypeToken<List<Student>>() {}.type
                val result = Gson().fromJson<List<Student>>(it, sType)
                studentsLD.value = result as ArrayList<Student>
                loadingLD.value = false

                // simpan data ke file
                val fileHelper = FileHelper(getApplication())
                val jsonString = Gson().toJson(result)
                fileHelper.writeToFile(jsonString)
                Log.d("print_file", jsonString)

                // baca json string dari file
                val hasil = fileHelper.readFromFile()
                val listStudent = Gson().fromJson<List<Student>>(hasil, sType)
                Log.d("print_file", listStudent.toString())
            },
            { // Failed
                errorLD.value = true
                loadingLD.value = false
            },
        )
        stringRequest.tag = TAG
        queue?.add(stringRequest)

//        // Hard-coded Style
//        studentsLD.value = arrayListOf(
//            Student("16055","Nonie","1998/03/28","5718444778","http://dummyimage.com/75x100.jpg/cc0000/ffffff"),
//            Student("13312","Rich","1994/12/14","3925444073","http://dummyimage.com/75x100.jpg/5fa2dd/ffffff"),
//            Student("11204","Dinny","1994/10/07","6827808747", "http://dummyimage.com/75x100.jpg/5fa2dd/ffffff1")
//        )
//        loadingLD.value = false
//        errorLD.value = false
    }

    fun testSaveFile() {
        val fileHelper = FileHelper(getApplication())
        fileHelper.writeToFile("Hello World")
        val content = fileHelper.readFromFile()
        Log.d("print_file", content)
        Log.d("print_file", fileHelper.getFilePath())
    }

    override fun onCleared() {
        super.onCleared()
        queue?.cancelAll(TAG)
    }

}