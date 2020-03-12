package aquacrew.aquabutton.model

import java.io.InputStream

/**
 * Describe voice file content and provide input stream
 */
data class VoiceFileResponse(
    val inputStream: InputStream,
    val filename: String
)