package com.example.studentproject.util

import android.content.Context
import java.io.File
import java.io.FileOutputStream

class FileHelper(val context: Context) {
    val folderName = "myFolder"
    val fileName = "myData.txt"

    private fun getFile(): File {
        val dir = File(context.filesDir, folderName)
        if (!dir.exists()) {
            dir.mkdir()
        }
        return File(dir, fileName)
    }

    fun writeToFile(data: String) {
        try {
            val file = getFile()
            // append = false -> data baru dilanjutkan
            // append = true -> data baru mereplace data lama
            FileOutputStream(file, false).use {
                    output -> output.write(data.toByteArray())
            } // false: boolean append to or false
        }

        catch (e:Exception) {
            e.printStackTrace()
        }
    }

    fun readFromFile(): String {
        try {
            val file = getFile()
            return file.bufferedReader().useLines {
                    lines -> lines.joinToString("\n")
            }
        }

        catch (e:Exception) {
            return e.printStackTrace().toString()
        }
    }

    fun deleteFile(): Boolean {
        return getFile().delete()
    }

    fun getFilePath(): String {
        return getFile().absolutePath
    }
 }