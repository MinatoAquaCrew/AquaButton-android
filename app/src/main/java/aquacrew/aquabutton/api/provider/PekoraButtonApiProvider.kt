package aquacrew.aquabutton.api.provider

import aquacrew.aquabutton.model.*
import aquacrew.aquabutton.util.HttpUtils
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import java.io.IOException

class PekoraButtonApiProvider : IAssetsApiProvider {

    companion object {

        private const val GITHUB_API_HOST = "https://raw.githubusercontent.com"

        private const val REQ_TAG_DOWNLOAD_VOICE = "tag_download_voice"

    }

    private val repoOwner = "Coceki"
    private val repoName = "peko-button"

    val repoUrl: String get() = "$GITHUB_API_HOST/$repoOwner/$repoName"
    val voicesUrl: String get() = "$repoUrl/master/src/assets/voices.json"
    val voiceFileUrl: String get() = "$repoUrl/master/public/voices/%s"

    override suspend fun getVoices(): List<VoiceCategory> {
        val request = Request.Builder().url(voicesUrl).build()

        return HttpUtils.requestAsJson<PekoraButtonVoicesData>(request).groups.map {
            VoiceCategory(
                it.name,
                textTranslationOf(
                    "zh" to it.description["Chinese"],
                    "jp" to it.description["Japanese"]
                ),
                it.voiceList.map { pekoraVoice ->
                    VoiceItem(
                        pekoraVoice.name,
                        pekoraVoice.path,
                        textTranslationOf(
                            "zh" to pekoraVoice.description["Chinese"],
                            "jp" to pekoraVoice.description["Japanese"]
                        )
                    )
                }
            )
        }
    }

    override suspend fun getVoiceFileResponse(voice: VoiceItem): VoiceFileResponse {
        val path = voice.path

        val request = Request.Builder()
            .url(voiceFileUrl.format(path))
            .tag(REQ_TAG_DOWNLOAD_VOICE)
            .build()

        val response = withContext(Dispatchers.IO) { HttpUtils.client.newCall(request).execute() }
        if (response.code == 200) {
            return VoiceFileResponse(
                response.body?.byteStream() ?: throw IOException("This response has no body."),
                path
            )
        } else {
            throw IOException("Response code is not okay (${response.code})")
        }
    }

    override fun cancelVoiceFileRequest() {
        HttpUtils.cancelRequest(REQ_TAG_DOWNLOAD_VOICE)
    }

    data class PekoraButtonVoicesData(val groups: List<PekoraButtonVoiceCategory>)

    data class PekoraButtonVoiceCategory(
        val name: String,
        @SerializedName("translation") val description: Map<String, String>,
        @SerializedName("voicelist") val voiceList: List<PekoraButtonVoiceItem>
    )

    data class PekoraButtonVoiceItem(
        val name: String,
        val path: String,
        @SerializedName("translation") val description: Map<String, String>
    )

}