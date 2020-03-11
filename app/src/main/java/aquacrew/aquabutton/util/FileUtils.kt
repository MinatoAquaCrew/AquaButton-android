package aquacrew.aquabutton.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.format.Formatter
import com.afollestad.inlineactivityresult.coroutines.startActivityAwaitResult
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import aquacrew.aquabutton.ui.common.BaseActivity
import java.io.File
import java.io.IOException

object FileUtils {

    suspend fun calculateFileSize(file: File): Long = withContext(IO) {
        if (!file.exists()) {
            return@withContext 0L
        } else if (file.isFile) {
            return@withContext file.length()
        }
        val defers = mutableListOf<Deferred<Long>>()
        file.listFiles()?.forEach { child ->
            defers += async { calculateFileSize(child) }
        }
        return@withContext defers.awaitAll().sum()
    }

    suspend fun calculateFileSizeToText(context: Context, file: File): String = withContext(IO) {
        val size = calculateFileSize(file)
        return@withContext Formatter.formatFileSize(context, size)
    }

    suspend fun requestNewDocumentUri(
        activity: BaseActivity,
        mimeType: String,
        fileName: String
    ): Uri? {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = mimeType
            putExtra(Intent.EXTRA_TITLE, fileName)
        }
        return activity.startActivityAwaitResult(intent).takeIf { it.success }?.data?.data
    }

    suspend fun copyFileToUri(context: Context, file: File, uri: Uri) = withContext(IO) {
        val outputStream = context.contentResolver.openOutputStream(uri)
            ?: throw IOException("Failed to open content uri: $uri")
        file.inputStream().use { it.copyTo(outputStream) }
        outputStream.close()
    }

}