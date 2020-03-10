package moe.feng.aquabutton.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import moe.feng.aquabutton.R
import moe.feng.aquabutton.ui.common.BaseActivity

class PreferenceActivity : BaseActivity(R.layout.activity_preference_common) {

    companion object {

        private const val TAG = "PreferenceActivity"

        private const val EXTRA_FRAGMENT_CLASS = "extra:FRAGMENT_CLASS"

        fun <T : Fragment> start(context: Context, fragmentClass: Class<T>) {
            val intent = Intent(context, PreferenceActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            intent.putExtra(EXTRA_FRAGMENT_CLASS, fragmentClass)
            context.startActivity(intent)
        }

        inline fun <reified T : Fragment> start(context: Context) {
            start(context, T::class.java)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragmentClass = intent?.getSerializableExtra(EXTRA_FRAGMENT_CLASS) as? Class<*>
        val fragmentInstance = fragmentClass?.newInstance() as? Fragment
        if (fragmentInstance == null) {
            Log.e(TAG, "fragment class is null.")
            finish()
            return
        }
        supportFragmentManager.commit {
            replace(R.id.contentFrame, fragmentInstance)
        }
    }

}