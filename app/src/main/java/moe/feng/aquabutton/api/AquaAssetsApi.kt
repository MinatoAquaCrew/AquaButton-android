package moe.feng.aquabutton.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import moe.feng.aquabutton.model.VoiceCategory
import moe.feng.aquabutton.model.internal.VoicesData
import moe.feng.aquabutton.util.HttpUtils
import okhttp3.Request

object AquaAssetsApi {

    private const val GITHUB_API_HOST = "https://raw.githubusercontent.com/zyzsdy/aqua-button"

    private const val VOICES_URL = "$GITHUB_API_HOST/master/src/voices.json"

    suspend fun getVoices(): List<VoiceCategory> = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(VOICES_URL).build()
        HttpUtils.requestAsJson<VoicesData>(request).voices
    }

}