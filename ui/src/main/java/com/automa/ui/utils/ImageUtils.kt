package com.automa.ui.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

object ImageUtils {
    fun getPath(context: Context, uri: Uri?): String {
        val inStream: InputStream? =
            uri?.let { context.contentResolver.openInputStream(it) }
        val fileName = "${System.currentTimeMillis()}.jpeg"
        val filePath = context.filesDir
        val file = File("$filePath/$fileName")
        val outStream: OutputStream = FileOutputStream(file)
        val buf = ByteArray(1024)
        var len: Int
        inStream?.let {
            while (inStream.read(buf).also { len = it } > 0) {
                outStream.write(buf, 0, len)
            }
            outStream.close()
            inStream.close()
        }

        val compressedFile = runBlocking {
             Compressor.compress(context, file) {
                 quality(50)
                 format(Bitmap.CompressFormat.JPEG)
                 size(1_000_000)
            }
        }
        return compressedFile.absolutePath
    }
}