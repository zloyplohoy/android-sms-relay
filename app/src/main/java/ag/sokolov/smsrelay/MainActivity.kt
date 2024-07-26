package ag.sokolov.smsrelay

import ag.sokolov.smsrelay.ui.SMSRelayApp
import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        if (BuildConfig.DEBUG) {
            setStrictMode()
        }

        super.onCreate(savedInstanceState)

        setContent {
            SMSRelayApp()
        }
    }

    private fun setStrictMode() {
        val strictModeThreadPolicy =
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()

        val strictModeVmPolicy =
            StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()

        StrictMode.setThreadPolicy(strictModeThreadPolicy)
        StrictMode.setVmPolicy(strictModeVmPolicy)
    }
}
