package aquacrew.aquabutton.api.provider

import aquacrew.aquabutton.model.VoiceCategory
import aquacrew.aquabutton.model.VoiceFileResponse
import aquacrew.aquabutton.model.VoiceItem

/**
 * VTuber Voices API interface
 */
interface IAssetsApiProvider {

    /**
     * Get all category list of VTuber's voices
     *
     * @return All category list
     */
    suspend fun getVoices(): List<VoiceCategory>

    /**
     * Get response of specified voice file content
     *
     * @param voice Specified voice item
     * @return Response of voice file content
     */
    suspend fun getVoiceFileResponse(voice: VoiceItem): VoiceFileResponse

    /**
     * Cancel requests of voice file content
     */
    fun cancelVoiceFileRequest()

}