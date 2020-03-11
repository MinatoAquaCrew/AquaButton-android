package moe.feng.aquabutton.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.updatePadding
import com.drakeet.multitype.MultiTypeAdapter
import kotlinx.android.synthetic.main.common_list.*
import moe.feng.aquabutton.R
import moe.feng.aquabutton.model.apache2
import moe.feng.aquabutton.model.licenses
import moe.feng.aquabutton.model.mit
import moe.feng.aquabutton.ui.common.BaseFragment
import moe.feng.aquabutton.ui.settings.list.LicensesItemBinder

class OSSLicensesFragment : BaseFragment(R.layout.common_list) {

    companion object {

        val LICENSES = licenses {
            apache2 {
                name = "Android Jetpack libraries"
                author = "Google"
                url = "https://android.googlesource.com/platform/frameworks" +
                        "/support/+/androidx-master-dev"
            }
            apache2 {
                name = "Kotlinx Coroutines"
                author = "Jetbrains"
                githubUrl("Kotlin/kotlinx.coroutines")
            }
            apache2 {
                name = "Material Components for Android"
                author = "Google"
                githubUrl("material-components/material-components-android")
            }
            mit {
                name = "EventsHelper"
                author = "Siubeng (fython)"
                githubUrl("fython/EventsHelper")
            }
            apache2 {
                name = "MultiType"
                author = "Drakeet"
                githubUrl("drakeet/MultiType")
            }
            apache2 {
                name = "OkHttp"
                author = "Square"
                githubUrl("square/okhttp")
            }
            apache2 {
                name = "Gson"
                author = "Google"
                githubUrl("google/gson")
            }
            license {
                name = "Material Design Icons (Community)"
                author = "Austin Andrews & Other contributors"
                license = "SIL Open Font License 1.1"
                githubUrl("Templarian/MaterialDesign")
            }
            apache2 {
                name = "Material Design Icons (Google)"
                author = "Google"
                githubUrl("google/material-design-icons")
            }
            apache2 {
                name = "Inline Activity Result"
                author = "Aidan Follestad"
                githubUrl("afollestad/inline-activity-result")
            }
            apache2 {
                name = "RecyclerView FastScroll"
                author = "Tim Malseed"
                githubUrl("timusus/RecyclerView-FastScrol")
            }
            apache2 {
                name = "Launcher3"
                author = "Android Open Source Project"
                url = "https://android.googlesource.com/platform/packages/apps/Launcher3"
            }
            apache2 {
                name = "ShapeShifter"
                author = "Alex Lockwood"
                githubUrl("alexjlockwood/ShapeShifter")
            }
            apache2 {
                name = "Roboto Slab"
                author = "Christian Robertson"
                githubUrl("googlefonts/robotoslab")
            }
            license {
                name = "Source Han Serif"
                author = "Adobe"
                license = "SIL Open Font License 1.1"
                githubUrl("adobe-fonts/source-han-serif")
            }
            sortByName()
        }

    }

    private val adapter: MultiTypeAdapter = MultiTypeAdapter(LICENSES).also {
        it.register(LicensesItemBinder())
    }

    override fun getActivityTitle(context: Context): String? {
        return context.getString(R.string.licenses_open_source_licenses)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.adapter = adapter

        val padding = resources.getDimensionPixelSize(R.dimen.padding_8dp)
        recyclerView.updatePadding(top = padding, bottom = padding)
    }

}