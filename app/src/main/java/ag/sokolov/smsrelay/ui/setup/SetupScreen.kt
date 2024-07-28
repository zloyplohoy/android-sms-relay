package ag.sokolov.smsrelay.ui.setup

import ag.sokolov.smsrelay.ui.common.AnimatedNavHost
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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
object SetupScreen

fun NavGraphBuilder.setupScreen(onFinished: () -> Unit) =
    composable<SetupScreen> { SetupScreen(onFinished) }

fun NavController.navigateToSetup() =
    navigate(SetupScreen)

@Composable
fun SetupScreen(
    onFinished: () -> Unit
) {
    val viewModel: SetupViewModel = hiltViewModel()
    val state = viewModel.state

    val setupNavController = rememberNavController()
    val currentSetupRoute: String? =
        setupNavController.currentBackStackEntryAsState().value?.destination?.route

    val setupProgress = rememberSaveable(currentSetupRoute) { getSetupProgress(currentSetupRoute) }
    val setupProgressAnimated: Float by animateFloatAsState(
        label = "Setup progress",
        targetValue = setupProgress,
        animationSpec = tween()
    )

    SetupScreen(
        progress = setupProgressAnimated,
        isLoading = state.isLoading
    ) {
        AnimatedNavHost(
            navController = setupNavController,
            startDestination = BotSetupScreen,
            modifier = Modifier.fillMaxSize()
        ) {
            botSetupScreen(
                onContinue = setupNavController::navigateToRecipientSetup,
                viewModel = viewModel
            )
            recipientSetupScreen(
                onContinue = setupNavController::navigateToPermissionsSetup,
                viewModel = viewModel
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
fun PreviewSetupScreenProgress() {
    SMSRelayTheme {
        Surface(Modifier.fillMaxSize()) {
            SetupScreen(progress = 0.5f)
        }
    }
}

@Preview
@Composable
fun PreviewSetupScreenLoading() {
    SMSRelayTheme {
        Surface(Modifier.fillMaxSize()) {
            SetupScreen(progress = 0.5f, isLoading = true)
        }
    }
}
