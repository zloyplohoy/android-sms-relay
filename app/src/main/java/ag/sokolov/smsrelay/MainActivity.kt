package ag.sokolov.smsrelay

import ag.sokolov.smsrelay.ui.SMSRelayApp
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder().detectAll().penaltyLog().build()
        )
        StrictMode.setVmPolicy(
            VmPolicy.Builder().detectAll().penaltyLog().build()
        )
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            SMSRelayApp()
        }
    }
}
