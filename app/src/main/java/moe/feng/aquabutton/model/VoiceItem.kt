package moe.feng.aquabutton.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VoiceItem(
    val name: String,
    val path: String,
    val description: TextTranslation
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        return (other as? VoiceItem)?.let {
            it.name == this.name && it.path == this.path
        } == true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + path.hashCode()
        return result
    }

}