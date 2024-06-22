package ag.sokolov.smsrelay.ui.statistics

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object StatisticsScreen

fun NavGraphBuilder.statisticsScreen(navController: NavController) =
    composable<StatisticsScreen> { StatisticsScreen(navController) }

@Composable
fun StatisticsScreen(navController: NavController) {
    StatisticsScreen()
}

@Composable
internal fun StatisticsScreen() {
    Surface {
        Text(text = "Statistics")
    }
}

fun NavController.navigateToStatistics() =
    navigate(StatisticsScreen)
