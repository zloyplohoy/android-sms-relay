package ag.sokolov.smsrelay.ui.setup

import ag.sokolov.smsrelay.ui.setup.navigation.SetupDestination
import ag.sokolov.smsrelay.ui.setup.navigation.finishSetup
import ag.sokolov.smsrelay.ui.setup.navigation.setupScreenContent
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
object SetupScreen

fun NavGraphBuilder.setupScreen(navController: NavController) =
    composable<SetupScreen> { SetupScreen(navController) }

@Composable
fun SetupScreen(
    navController: NavController
) {
    val setupNavController = rememberNavController()
    val currentSetupRoute: String? =
        setupNavController.currentBackStackEntryAsState().value?.destination?.route

    val setupProgress = rememberSaveable(currentSetupRoute) { getSetupProgress(currentSetupRoute) }
    val setupProgressAnimated: Float by animateFloatAsState(
        label = "Setup progress",
        targetValue = setupProgress,
        animationSpec = tween(),
    )

    SetupScreen(setupProgress = setupProgressAnimated) {
        SetupNavHost(
            setupNavController = setupNavController, onFinished = navController::finishSetup
        )
    }
}

@Composable
fun SetupScreen(
    setupProgress: Float,
    content: @Composable (() -> Unit) = {}
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
    ) {
        SetupProgressIndicator(progress = setupProgress)
        content()
    }
}

@Composable
fun SetupProgressIndicator(
    progress: Float
) {
    LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        strokeCap = StrokeCap.Round
    )
}

@Composable
fun SetupNavHost(
    setupNavController: NavHostController,
    onFinished: () -> Unit
) {
    NavHost(modifier = Modifier.fillMaxSize(),
        navController = setupNavController,
        startDestination = SetupDestination.ROOT,
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) + fadeIn()
        },
        popEnterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right) + fadeIn()
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left) + fadeOut()
        },
        popExitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) + fadeOut()
        }) {
        setupScreenContent(setupNavController, onFinished = onFinished)
    }
}

fun getSetupProgress(currentSetupRoute: String?): Float {
    return when (currentSetupRoute) {
        SetupDestination.BOT::class.java.canonicalName?.toString() -> 0f
        SetupDestination.RECIPIENT::class.java.canonicalName?.toString() -> 0.33f
        SetupDestination.PERMISSIONS::class.java.canonicalName?.toString() -> 0.66f
        SetupDestination.END::class.java.canonicalName?.toString() -> 1f
        else -> 0f
    }
}

@Preview
@Composable
fun PreviewSetupScreen() {
    SMSRelayTheme {
        Surface(Modifier.fillMaxSize()) {
            SetupScreen(setupProgress = 0.5f) {
                Surface(Modifier.fillMaxSize(), color = Color.Red) {}
            }
        }
    }
}
