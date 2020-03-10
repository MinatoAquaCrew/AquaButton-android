package moe.feng.aquabutton.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.afollestad.inlineactivityresult.coroutines.startActivityAwaitResult
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import moe.feng.aquabutton.ui.common.BaseActivity
import java.io.File
import java.io.IOException

object FileUtils {

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