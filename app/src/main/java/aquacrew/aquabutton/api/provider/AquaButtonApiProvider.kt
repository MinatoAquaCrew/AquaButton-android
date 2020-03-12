package aquacrew.aquabutton.api.provider

import aquacrew.aquabutton.model.VoiceCategory
import aquacrew.aquabutton.model.VoiceFileResponse
import aquacrew.aquabutton.model.VoiceItem
import aquacrew.aquabutton.model.internal.VoicesData
import aquacrew.aquabutton.util.HttpUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import java.io.IOException

class AquaButtonApiProvider : IAssetsApiProvider {

    companion object {

        private const val GITHUB_API_HOST = "https://raw.githubusercontent.com/zyzsdy/aqua-button"

        private const val VOICES_URL = "$GITHUB_API_HOST/master/src/voices.json"
        private const val VOICE_FILE_URL = "$GITHUB_API_HOST/master/public/voices/%s"

        private const val REQ_TAG_DOWNLOAD_VOICE = "tag_download_voice"

    }

    override suspend fun getVoices(): List<VoiceCategory> {
        val request = Request.Builder().url(VOICES_URL).build()
        return HttpUtils.requestAsJson<VoicesData>(request).voices
    }

    override suspend fun getVoiceFileResponse(voice: VoiceItem): VoiceFileResponse {
        val path = voice.path

        val request = Request.Builder()
            .url(VOICE_FILE_URL.format(path))
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

}