package aquacrew.aquabutton.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import aquacrew.aquabutton.AquaApp
import aquacrew.aquabutton.model.VoiceCategory
import aquacrew.aquabutton.model.VoiceItem
import aquacrew.aquabutton.model.internal.VoicesData
import aquacrew.aquabutton.util.FileUtils
import aquacrew.aquabutton.util.HttpUtils
import okhttp3.Request
import okio.IOException
import java.io.File

object AquaAssetsApi : AquaApp.Component {

    private const val GITHUB_API_HOST = "https://raw.githubusercontent.com/zyzsdy/aqua-button"

    private const val VOICES_URL = "$GITHUB_API_HOST/master/src/voices.json"
    private const val VOICE_FILE_URL = "$GITHUB_API_HOST/master/public/voices/%s"

    const val REQ_TAG_DOWNLOAD_VOICE = "tag_download_voice"

    private val voicesCacheFile: File by lazy {
        val file = context.cacheDir.resolve("voices")
        if (!file.isDirectory) {
            file.mkdirs()
        }
        file
    }

    suspend fun getVoices(): List<VoiceCategory> = withContext(IO) {
        val request = Request.Builder().url(VOICES_URL).build()
        HttpUtils.requestAsJson<VoicesData>(request).voices
    }

    fun pathToCacheName(path: String): String {
        return path.replace("/", "_")
    }

    fun pathToCacheFile(path: String): File {
        return voicesCacheFile.resolve(pathToCacheName(path))
    }

    suspend fun getVoiceCacheSize(): Long {
        return FileUtils.calculateFileSize(voicesCacheFile)
    }

    suspend fun getVoiceCacheSizeText(): String {
        return FileUtils.calculateFileSizeToText(context, voicesCacheFile)
    }

    suspend fun clearVoiceCache() = withContext(IO) {
        voicesCacheFile.listFiles()?.forEach {
            try {
                it.deleteRecursively()
            } catch (ignored: Exception) {

            }
        }
    }

    suspend fun downloadVoice(voice: VoiceItem) {
        withContext(IO) {
            val path = voice.path

            val request = Request.Builder()
                .url(VOICE_FILE_URL.format(path))
                .tag(REQ_TAG_DOWNLOAD_VOICE)
                .build()

            val response = withContext(Dispatchers.IO) {
                HttpUtils.client.newCall(request).execute()
            }
            if (response.code == 200) {
                val body = response.body ?: throw IOException("Empty http body")
                val cacheFile = pathToCacheFile(path)
                if (cacheFile.exists()) {
                    cacheFile.delete()
                }
                val cacheTempFile = File(cacheFile.absolutePath + "_temp")
                cacheTempFile.createNewFile()
                withContext(Dispatchers.IO) {
                    body.byteStream().use { input ->
                        cacheTempFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                }
                cacheTempFile.renameTo(cacheFile)
            } else {
                throw IOException("Cannot get voice data.")
            }
        }
    }

    fun cancelDownloadingVoice() {
        HttpUtils.cancelRequest(REQ_TAG_DOWNLOAD_VOICE)
    }

    suspend fun getVoice(voice: VoiceItem): File = withContext(IO) {
        val cacheFile = pathToCacheFile(voice.path)
        if (!cacheFile.exists()) {
            downloadVoice(voice)
        }
        cacheFile
    }

}