package ag.sokolov.smsrelay.ui.setup.screen.bot

import ag.sokolov.smsrelay.R
import ag.sokolov.smsrelay.ui.setup.SetupScreen
import ag.sokolov.smsrelay.ui.setup.common.element.contact_list_item.ContactListItem
import ag.sokolov.smsrelay.ui.setup.common.element.ordered_list.orderedList
import ag.sokolov.smsrelay.ui.setup.common.element.setup_step_section.SetupStepSection
import ag.sokolov.smsrelay.ui.setup.common.element.setup_step_title.SetupStepTitle
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BotSetupScreen(
    onContinue: () -> Unit,
    viewModel: BotSetupViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BotSetupScreen(
        state = state,
        onContinue = onContinue,
        onTokenValueChanged = viewModel::onTokenValueChanged,
        onTokenReset = viewModel::onTokenReset
    )
}

@Composable
internal fun BotSetupScreen(
    state: BotSetupState,
    onContinue: () -> Unit,
    onTokenValueChanged: (value: String) -> Unit,
    onTokenReset: () -> Unit
) {
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
                    state = state, onValueChange = onTokenValueChanged, onReset = onTokenReset
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        AnimatedVisibility(
            visible = state is BotSetupState.Configured, enter = fadeIn(), exit = fadeOut()
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(
                    enabled = state is BotSetupState.Configured,
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
    state: BotSetupState,
    onValueChange: (value: String) -> Unit,
    onReset: () -> Unit
) {
    var token by rememberSaveable { mutableStateOf("") }

    val isTokenInputEnabled by remember(state) {
        mutableStateOf(state is BotSetupState.NotConfigured || state is BotSetupState.Error)
    }
//    val isTokenInputEnabled = state is BotSetupState.NotConfigured || state is BotSetupState.Error

    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            var isInitialTransitionState by remember { mutableStateOf(true) }
            var tokenInputFieldAnimationFinished by remember(state) { mutableStateOf(false) }
            var botDetailsAnimationFinished by remember(state) { mutableStateOf(false) }

            val isTokenTextFieldExpanded =
                state !is BotSetupState.Configured && (botDetailsAnimationFinished || isInitialTransitionState)

            val isBotDetailsDisplayed =
                state is BotSetupState.Configured && tokenInputFieldAnimationFinished

            val isBotDetailsDisplayedTransitionState =
                remember(state) { MutableTransitionState(isBotDetailsDisplayed) }

            isBotDetailsDisplayedTransitionState.targetState = isBotDetailsDisplayed

            LaunchedEffect(isBotDetailsDisplayedTransitionState) {
                snapshotFlow { isBotDetailsDisplayedTransitionState.isIdle }.collect { isIdle ->
                    botDetailsAnimationFinished = isIdle
                }
            }

            val tokenTextFieldWidth by animateDpAsState(label = "Token input width",
                targetValue = if (isTokenTextFieldExpanded) maxWidth else OutlinedTextFieldDefaults.MinHeight,
                animationSpec = tween(5000),
                finishedListener = {
                    tokenInputFieldAnimationFinished = true
                    isInitialTransitionState = false
                })

            OutlinedTextField(value = when (state) {
                is BotSetupState.Configured -> ""
                is BotSetupState.Loading -> stringResource(R.string.sample_telegram_bot_api_token)
                else -> token
            },
//                value = token,
                onValueChange = { value ->
                    token = value
                    onValueChange(value)
                },
                enabled = isTokenInputEnabled,
                singleLine = true,
                placeholder = {
                    this@Row.AnimatedVisibility(
                        visible = state !is BotSetupState.Configured && (tokenInputFieldAnimationFinished || isInitialTransitionState),
                        enter = fadeIn(),
                        exit = ExitTransition.None
                    ) {
                        Text(
                            text = stringResource(R.string.bot_api_token_input_placeholder),
                            maxLines = 1
                        )
                    }
                },
                trailingIcon = when (state) {
                    is BotSetupState.Loading -> {
                        {
                            CircularProgressIndicator(
                                strokeCap = StrokeCap.Round,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    is BotSetupState.Error -> {
                        {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "An error occurred"
                            )
                        }
                    }
                    else -> null
                },
                isError = state is BotSetupState.Error,
                supportingText = { (state as? BotSetupState.Error)?.let { Text(text = it.errorMessage) } },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .width(tokenTextFieldWidth)
                    .zIndex(1f)
                    .onFocusChanged {
                        Log.d("TAG", "isFocused: ${it.isFocused}")
                    },
                shape = RoundedCornerShape(percent = 50)
            )
            this@Row.AnimatedVisibility(
                visibleState = isBotDetailsDisplayedTransitionState,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.zIndex(2f)
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
            enabled = state !is BotSetupState.Loading
        ) {
            Icon(
                imageVector = Icons.Outlined.Clear, contentDescription = "Reset token"
            )
        }
    }
}

@Composable
fun BotDetails(
    state: BotSetupState
) {
    var latestConfiguredState by remember { mutableStateOf<BotSetupState.Configured?>(null) }

    if (state is BotSetupState.Configured) latestConfiguredState = state

    latestConfiguredState?.let { configuredState ->
        ContactListItem(
            title = configuredState.botName, description = "@${configuredState.botUsername}"
        )
    }
}

@PreviewLightDark
@Composable
fun PreviewBotSetupScreenNotConfigured() {
    SMSRelayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SetupScreen(setupProgress = 0.33f) {
                BotSetupScreen(state = BotSetupState.NotConfigured,
                    onContinue = {},
                    onTokenValueChanged = {},
                    onTokenReset = {})
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewBotSetupScreenLoading() {
    SMSRelayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SetupScreen(setupProgress = 0.33f) {
                BotSetupScreen(state = BotSetupState.Loading,
                    onContinue = {},
                    onTokenValueChanged = {},
                    onTokenReset = {})
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewBotSetupScreenConfigured() {
    SMSRelayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SetupScreen(setupProgress = 0.33f) {
                BotSetupScreen(state = BotSetupState.Configured(
                    botName = "Awesome Telegram bot", botUsername = "awesome_telegram_bot"
                ), onContinue = {}, onTokenValueChanged = {}, onTokenReset = {})
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewBotSetupScreenError() {
    SMSRelayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SetupScreen(setupProgress = 0.33f) {
                BotSetupScreen(state = BotSetupState.Error(errorMessage = "Invalid token"),
                    onContinue = {},
                    onTokenValueChanged = {},
                    onTokenReset = {})
            }
        }
    }
}
