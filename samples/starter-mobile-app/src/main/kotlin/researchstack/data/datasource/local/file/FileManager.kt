package researchstack.data.datasource.local.file

import android.content.Context
import java.io.File
import java.io.InputStream
import java.nio.file.Files

class FileManager(private val context: Context) {
    fun saveFile(fileName: String, inputStream: InputStream) {
        val file = File(context.filesDir, fileName)
        if (file.exists()) return
        Files.copy(inputStream, file.toPath())
    }

    fun deleteFile(fileName: String) {
        val file = File(context.filesDir, fileName)
        file.delete()
    }
}
