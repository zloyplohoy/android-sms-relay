package ag.sokolov.smsrelay.ui.setup.screen.bot

import ag.sokolov.smsrelay.R
import ag.sokolov.smsrelay.ui.setup.BotState
import ag.sokolov.smsrelay.ui.setup.SetupScreen
import ag.sokolov.smsrelay.ui.setup.SetupViewModel
import ag.sokolov.smsrelay.ui.setup.common.element.contact_list_item.ContactListItem
import ag.sokolov.smsrelay.ui.setup.common.element.ordered_list.orderedList
import ag.sokolov.smsrelay.ui.setup.common.element.setup_step_section.SetupStepSection
import ag.sokolov.smsrelay.ui.setup.common.element.setup_step_title.SetupStepTitle
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseInOutCirc
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.math.absoluteValue

@Serializable
object BotSetupScreen

fun NavGraphBuilder.botSetupScreen(
    viewModel: SetupViewModel,
    onContinue: () -> Unit,
) =
    composable<BotSetupScreen> {
        val state by viewModel.stateFlow.collectAsStateWithLifecycle()

        BotSetupScreen(
            state = state.botState,
            onContinue = onContinue,
            onTokenValueChanged = viewModel::onTokenValueChanged,
            onTokenReset = viewModel::onTokenReset
        )
    }

@Composable
internal fun BotSetupScreen(
    state: BotState,
    onContinue: () -> Unit,
    onTokenValueChanged: (value: String) -> Unit,
    onTokenReset: () -> Unit
) {
    var isContinueButtonVisible by remember { mutableStateOf(state is BotState.Configured) }

    LaunchedEffect(state) {
        when (state) {
            is BotState.Configured -> {}
            else -> {
                isContinueButtonVisible = false
            }
        }
    }

    fun showContinueButton() {
        Log.d("TAG", "showContinueButton: called")
        isContinueButtonVisible = true
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .verticalScroll(rememberScrollState()), // look at this
    ) {
        SetupStepTitle(stringResource(R.string.bot_setup_step_title))
        SetupStepSection(name = "Bot setup instructions") {
            Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                BotSetupDescription()
                TokenInputBlock(
                    state = state,
                    onValueChange = onTokenValueChanged,
                    onReset = onTokenReset,
                    onCanContinue = ::showContinueButton
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        AnimatedVisibility(
            visible = isContinueButtonVisible, enter = fadeIn(), exit = fadeOut()
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(
                    enabled = state is BotState.Configured,
                    onClick = onContinue,
                    modifier = Modifier.height(OutlinedTextFieldDefaults.MinHeight)
                ) {
                    Text(text = "Next", modifier = Modifier.padding(horizontal = 8.dp))
                }
            }
        }
    }
}

@Composable
fun BotSetupDescription() {
    val setupSteps = listOf(
        R.string.bot_setup_description_1,
        R.string.bot_setup_description_2,
        R.string.bot_setup_description_3,
        R.string.bot_setup_description_4,
        R.string.bot_setup_description_5,
        R.string.bot_setup_description_6,
        R.string.bot_setup_description_7,
        R.string.bot_setup_description_8
    )
    Text(
        text = orderedList(items = setupSteps.map { stringResource(id = it) }),
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(start = 8.dp)
    )
}

@Composable
fun TokenInputBlock(
    state: BotState,
    onValueChange: (value: String) -> Unit,
    onReset: () -> Unit,
    onCanContinue: () -> Unit
) {
    var token by rememberSaveable { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    val isTokenInputEnabled by remember(state) {
        mutableStateOf(state is BotState.NotConfigured || state is BotState.Error)
    }

    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        BoxWithConstraints(modifier = Modifier.weight(1f)) {

            val boxWithConstraintsScope = this

            // Initial animation values

            val initialTokenTextFieldWidth = when (state) {
                is BotState.Configured -> OutlinedTextFieldDefaults.MinHeight.value
                else -> maxWidth.value.absoluteValue
            }

            val initialTokenTextFieldPlaceholderOpacity = when (state) {
                is BotState.NotConfigured -> 1f
                else -> 0f
            }

            val initialBotDetailsOpacity = when (state) {
                is BotState.Configured -> 1f
                else -> 0f
            }

            // Actual animation values

            val tokenTextFieldWidth = remember { Animatable(initialTokenTextFieldWidth) }
            val tokenTextFieldPlaceholderOpacity =
                remember { Animatable(initialTokenTextFieldPlaceholderOpacity) }
            val botDetailsOpacity = remember { Animatable(initialBotDetailsOpacity) }

            // Animation transitions

            LaunchedEffect(state) {
                coroutineScope.launch {
                    when (state) {
                        is BotState.NotConfigured -> {
                            botDetailsOpacity.hide()
                            tokenTextFieldWidth.expand(boxWithConstraintsScope)
                            tokenTextFieldPlaceholderOpacity.show()
                        }
                        is BotState.Configured -> {
                            launch { tokenTextFieldPlaceholderOpacity.hide() }
                            launch {
                                tokenTextFieldWidth.collapse()
                                botDetailsOpacity.show()
                                onCanContinue()
                            }
                        }
                        else -> {}
                    }
                }
            }

            OutlinedTextField(
                value = when (state) {
                    is BotState.Configured -> ""
                    else -> token
                },
                onValueChange = { value ->
                    token = value
                    onValueChange(value)
                },
                enabled = isTokenInputEnabled,
                singleLine = true,
                placeholder = {
                    TokenTextFieldPlaceholder(
                        state, Modifier.alpha(tokenTextFieldPlaceholderOpacity.value)
                    )
                },
                isError = state is BotState.Error,
                supportingText = { TokenTextFieldSupportingText(state) },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(percent = 50),
                modifier = Modifier
                    .width(tokenTextFieldWidth.value.dp)
                    .zIndex(1f)
                    .alpha(1f - botDetailsOpacity.value),
            )
            Box(
                modifier = Modifier
                    .zIndex(2f)
                    .alpha(botDetailsOpacity.value)
            ) {
                BotDetails(state = state)
            }
        }
        OutlinedIconButton(
            onClick = {
                if (isTokenInputEnabled) {
                    token = ""
                } else {
                    token = ""
                    onReset()
                }
            },
            modifier = Modifier.size(OutlinedTextFieldDefaults.MinHeight),
            enabled = state !is BotState.Loading
        ) {
            Icon(
                imageVector = Icons.Outlined.Clear, contentDescription = "Reset token"
            )
        }
    }
}

@Composable
fun TokenTextFieldPlaceholder(
    state: BotState,
    modifier: Modifier = Modifier
) {
    if (state is BotState.NotConfigured) {
        Text(
            text = stringResource(R.string.bot_api_token_input_placeholder),
            maxLines = 1,
            modifier = modifier
        )
    }
}

@Composable
fun TokenTextFieldSupportingText(state: BotState) {
    (state as? BotState.Error)?.let { Text(text = it.message) }
}

@Composable
fun BotDetails(
    state: BotState
) {
    var latestConfiguredState by remember { mutableStateOf<BotState.Configured?>(null) }

    if (state is BotState.Configured) latestConfiguredState = state

    latestConfiguredState?.let { configuredState ->
        ContactListItem(
            title = configuredState.name, description = "@${configuredState.username}"
        )
    }
}

@PreviewLightDark
@Composable
fun PreviewBotSetupScreenNotConfigured() {
    SMSRelayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SetupScreen(progress = 0.33f) {
                BotSetupScreen(
                    state = BotState.NotConfigured,
                    onContinue = {},
                    onTokenValueChanged = {},
                    onTokenReset = {}
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewBotSetupScreenLoading() {
    SMSRelayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SetupScreen(progress = 0.33f) {
                BotSetupScreen(
                    state = BotState.Loading,
                    onContinue = {},
                    onTokenValueChanged = {},
                    onTokenReset = {}
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewBotSetupScreenConfigured() {
    SMSRelayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SetupScreen(progress = 0.33f) {
                BotSetupScreen(
                    state = BotState.Configured(
                        name = "Awesome Telegram bot", username = "awesome_telegram_bot"
                    ),
                    onContinue = {},
                    onTokenValueChanged = {},
                    onTokenReset = {}
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewBotSetupScreenError() {
    SMSRelayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SetupScreen(progress = 0.33f) {
                BotSetupScreen(
                    state = BotState.Error(message = "Invalid token"),
                    onContinue = {},
                    onTokenValueChanged = {},
                    onTokenReset = {}
                )
            }
        }
    }
}

suspend fun Animatable<Float, AnimationVector1D>.expand(boxWithConstraintsScope: BoxWithConstraintsScope) {
    this.animateTo(
        boxWithConstraintsScope.maxWidth.value.absoluteValue,
        animationSpec = tween(easing = EaseInOut)
    )
}

suspend fun Animatable<Float, AnimationVector1D>.collapse() {
    this.animateTo(
        OutlinedTextFieldDefaults.MinHeight.value, animationSpec = tween(easing = EaseInOut)
    )
}

suspend fun Animatable<Float, AnimationVector1D>.hide() {
    this.animateTo(0f, animationSpec = tween(easing = EaseInOutCirc))
}

suspend fun Animatable<Float, AnimationVector1D>.show() {
    this.animateTo(1f, animationSpec = tween(easing = EaseInOutCirc))
}
