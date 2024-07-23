package ag.sokolov.smsrelay.ui.setup

import ag.sokolov.smsrelay.ui.common.DualPurposeLinearProgressIndicator
import ag.sokolov.smsrelay.ui.setup.screen.bot.BotSetupScreen
import ag.sokolov.smsrelay.ui.setup.screen.bot.botSetupScreen
import ag.sokolov.smsrelay.ui.setup.screen.end.SetupEndScreen
import ag.sokolov.smsrelay.ui.setup.screen.end.navigateToSetupEnd
import ag.sokolov.smsrelay.ui.setup.screen.end.setupEndScreen
import ag.sokolov.smsrelay.ui.setup.screen.permissions.PermissionsSetupScreen
import ag.sokolov.smsrelay.ui.setup.screen.permissions.navigateToPermissionsSetup
import ag.sokolov.smsrelay.ui.setup.screen.permissions.permissionsSetupScreen
import ag.sokolov.smsrelay.ui.setup.screen.recipient.RecipientSetupScreen
import ag.sokolov.smsrelay.ui.setup.screen.recipient.navigateToRecipientSetup
import ag.sokolov.smsrelay.ui.setup.screen.recipient.recipientSetupScreen
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
object SetupScreen

fun NavGraphBuilder.setupScreen(onFinished: () -> Unit) =
    composable<SetupScreen> { SetupScreen(onFinished) }

@Composable
fun SetupScreen(
    onFinished: () -> Unit,
    viewModel: SetupViewModel = hiltViewModel()
) {
    val setupNavController = rememberNavController()
    val currentSetupRoute: String? =
        setupNavController.currentBackStackEntryAsState().value?.destination?.route

    val setupProgress = rememberSaveable(currentSetupRoute) { getSetupProgress(currentSetupRoute) }
    val setupProgressAnimated: Float by animateFloatAsState(
        label = "Setup progress",
        targetValue = setupProgress,
        animationSpec = tween()
    )

    var _isLoading by remember { mutableStateOf(false) }

    fun setLoadingState(isLoading: Boolean) {
        _isLoading = isLoading
    }

    SetupScreen(
        progress = setupProgressAnimated,
        isLoading = _isLoading
    ) {
        SetupNavHost(
            setupNavController = setupNavController
        ) {
            botSetupScreen(
                onContinue = setupNavController::navigateToRecipientSetup,
                setLoadingState = ::setLoadingState,
                viewModel = viewModel
            )
            recipientSetupScreen(
                onContinue = setupNavController::navigateToPermissionsSetup
            )
            permissionsSetupScreen(
                onContinue = setupNavController::navigateToSetupEnd
            )
            setupEndScreen(
                onFinished = onFinished
            )
        }
    }
}

@Composable
internal fun SetupScreen(
    progress: Float,
    isLoading: Boolean = false,
    content: @Composable (() -> Unit) = {}
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
    ) {
        DualPurposeLinearProgressIndicator(
            progress = progress,
            isLoading = isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )
        content()
    }
}

@Composable
fun SetupNavHost(
    setupNavController: NavHostController,
    content: NavGraphBuilder.() -> Unit
) {
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = setupNavController,
        startDestination = BotSetupScreen,
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
        }
    ) {
        content()
    }
}

fun getSetupProgress(currentSetupRoute: String?): Float {
    return when (currentSetupRoute) {
        BotSetupScreen::class.java.canonicalName?.toString() -> 0f
        RecipientSetupScreen::class.java.canonicalName?.toString() -> 0.33f
        PermissionsSetupScreen::class.java.canonicalName?.toString() -> 0.66f
        SetupEndScreen::class.java.canonicalName?.toString() -> 1f
        else -> 0f
    }
}

@Preview
@Composable
fun PreviewSetupScreen() {
    SMSRelayTheme {
        Surface(Modifier.fillMaxSize()) {
            SetupScreen(progress = 0.5f)
        }
    }
}
