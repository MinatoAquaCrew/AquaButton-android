package aquacrew.aquabutton.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class VoiceCategory(
    @SerializedName("categoryName") val name: String,
    @SerializedName("categoryDescription") val description: TextTranslation,
    val voiceList: List<VoiceItem>,
    override var selected: Boolean = false
) : Parcelable, ISelectableModel {

    override fun equals(other: Any?): Boolean {
        return (other as? VoiceCategory)?.name == this.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

}