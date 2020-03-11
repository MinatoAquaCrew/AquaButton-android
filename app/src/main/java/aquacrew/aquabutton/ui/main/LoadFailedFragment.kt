package aquacrew.aquabutton.ui.main

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_main_load_failed.*
import aquacrew.aquabutton.R
import aquacrew.aquabutton.ui.common.BaseFragment
import aquacrew.aquabutton.ui.main.event.MainUiEventCallback
import moe.feng.common.eventshelper.EventsHelper
import moe.feng.common.eventshelper.of

class LoadFailedFragment : BaseFragment(R.layout.fragment_main_load_failed) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        retryButton.setOnClickListener {
            EventsHelper.getInstance(it.context)
                .of<MainUiEventCallback>()
                .requestReload()
        }
    }

}