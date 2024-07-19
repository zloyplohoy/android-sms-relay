package ag.sokolov.smsrelay.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class SMSRelayAppState(
    val navController: NavHostController
)

@Composable
fun rememberSMSRelayAppState(
    navController: NavHostController= rememberNavController()
): SMSRelayAppState {
    return remember(navController) {
        SMSRelayAppState(
            navController = navController
        )
    }
}
