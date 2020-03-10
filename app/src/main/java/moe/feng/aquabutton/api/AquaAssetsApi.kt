package moe.feng.aquabutton.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import moe.feng.aquabutton.AquaApp
import moe.feng.aquabutton.model.VoiceCategory
import moe.feng.aquabutton.model.VoiceItem
import moe.feng.aquabutton.model.internal.VoicesData
import moe.feng.aquabutton.util.HttpUtils
import okhttp3.Request
import okio.IOException
import java.io.File

object AquaAssetsApi : AquaApp.Component {

    private const val GITHUB_API_HOST = "https://raw.githubusercontent.com/zyzsdy/aqua-button"

    private const val VOICES_URL = "$GITHUB_API_HOST/master/src/voices.json"
    private const val VOICE_FILE_URL = "$GITHUB_API_HOST/master/public/voices/%s"

    private val voicesCacheFile: File by lazy {
        val file = context.cacheDir.resolve("voices")
        if (!file.isDirectory) {
            file.mkdirs()
        }
        file
    }

    suspend fun getVoices(): List<VoiceCategory> = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(VOICES_URL).build()
        HttpUtils.requestAsJson<VoicesData>(request).voices
    }

    fun pathToCacheName(path: String): String {
        return path.replace("/", "_")
    }

    fun pathToCacheFile(path: String): File {
        return voicesCacheFile.resolve(pathToCacheName(path))
    }

    suspend fun downloadVoice(voice: VoiceItem) {
        withContext(Dispatchers.IO) {
            val path = voice.path

            val request = Request.Builder().url(VOICE_FILE_URL.format(path)).build()

            val response = HttpUtils.client.newCall(request).execute()
            if (response.code == 200) {
                val body = response.body ?: throw IOException("Empty http body")
                val cacheFile = pathToCacheFile(path)
                if (cacheFile.exists()) {
                    cacheFile.delete()
                }
                val cacheTempFile = File(cacheFile.absolutePath + "_temp")
                cacheTempFile.createNewFile()
                body.byteStream().use { input ->
                    cacheTempFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                cacheTempFile.renameTo(cacheFile)
            } else {
                throw IOException("Cannot get voice data.")
            }
        }
    }

    suspend fun getVoice(voice: VoiceItem): File = withContext(Dispatchers.IO) {
        val cacheFile = pathToCacheFile(voice.path)
        if (!cacheFile.exists()) {
            downloadVoice(voice)
        }
        cacheFile
    }

}