package moe.feng.aquabutton.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.View
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
    }

}