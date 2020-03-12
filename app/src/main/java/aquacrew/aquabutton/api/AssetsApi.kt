package aquacrew.aquabutton.api

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import aquacrew.aquabutton.AquaApp
import aquacrew.aquabutton.api.provider.IAssetsApiProvider
import aquacrew.aquabutton.model.VoiceCategory
import aquacrew.aquabutton.model.VoiceItem
import aquacrew.aquabutton.util.FileUtils
import java.io.File

object AssetsApi : AquaApp.Component {

    private lateinit var apiProvider: IAssetsApiProvider

    private val voicesCacheFile: File by lazy {
        val file = context.cacheDir.resolve("voices")
        if (!file.isDirectory) {
            file.mkdirs()
        }
        file
    }

    /**
     * Call this method to install assets api provider before using AssetsApi
     *
     * @param apiProvider Implemented assets api provider
     */
    fun installProvider(apiProvider: IAssetsApiProvider) {
        this.apiProvider = apiProvider
    }

    suspend fun getVoices(): List<VoiceCategory> = withContext(IO) { apiProvider.getVoices() }

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
            val (inputStream, path) = apiProvider.getVoiceFileResponse(voice)

            val cacheFile = pathToCacheFile(path)
            if (cacheFile.exists()) {
                cacheFile.delete()
            }
            val cacheTempFile = File(cacheFile.absolutePath + "_temp")
            cacheTempFile.createNewFile()
            withContext(IO) {
                inputStream.use { input ->
                    cacheTempFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
            cacheTempFile.renameTo(cacheFile)
        }
    }

    fun cancelDownloadingVoice() {
        apiProvider.cancelVoiceFileRequest()
    }

    suspend fun getVoice(voice: VoiceItem): File = withContext(IO) {
        val cacheFile = pathToCacheFile(voice.path)
        if (!cacheFile.exists()) {
            downloadVoice(voice)
        }
        cacheFile
    }

}