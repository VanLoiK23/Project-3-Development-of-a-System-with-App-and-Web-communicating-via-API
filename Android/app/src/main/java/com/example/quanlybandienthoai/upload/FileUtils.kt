package com.example.appnotegiuaki.upload

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream


object FileUtils {
    fun uriToFile(context: Context, uri: Uri,nameImage:String): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "$nameImage.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file
    }
}
