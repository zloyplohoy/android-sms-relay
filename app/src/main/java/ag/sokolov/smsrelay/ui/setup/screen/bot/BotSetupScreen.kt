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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

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
    var isContinueButtonVisible by remember { mutableStateOf(state is BotSetupState.Configured) }

    LaunchedEffect(state) {
        when (state) {
            is BotSetupState.Configured -> {}
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
    onReset: () -> Unit,
    onCanContinue: () -> Unit
) {
    var token by rememberSaveable { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    val isTokenInputEnabled by remember(state) {
        mutableStateOf(state is BotSetupState.NotConfigured || state is BotSetupState.Error)
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
                is BotSetupState.Configured -> OutlinedTextFieldDefaults.MinHeight.value
                else -> maxWidth.value.absoluteValue
            }

            val initialTokenTextFieldValueOpacity = when (state) {
                is BotSetupState.Configured -> 0f
                else -> 1f
            }

            val initialBotDetailsOpacity = when (state) {
                is BotSetupState.Configured -> 1f
                else -> 0f
            }

            // Actual animation values

            val tokenTextFieldWidth2 = remember { Animatable(initialTokenTextFieldWidth) }
            val tokenTextFieldValueOpacity =
                remember { Animatable(initialTokenTextFieldValueOpacity) }
            val botDetailsOpacity = remember { Animatable(initialBotDetailsOpacity) }

            // Animation transitions

            LaunchedEffect(state) {
                coroutineScope.launch {
                    when (state) {
                        is BotSetupState.NotConfigured -> {
                            botDetailsOpacity.hide()
                            tokenTextFieldWidth2.expand(boxWithConstraintsScope)
                            tokenTextFieldValueOpacity.show()
                        }
                        is BotSetupState.Configured -> {
                            tokenTextFieldWidth2.collapse()
                            botDetailsOpacity.show()
                            onCanContinue()
                        }
                        else -> {}
                    }
                }
            }

            OutlinedTextField(
                value = when (state) {
                    is BotSetupState.Configured -> ""
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
                        state, Modifier.alpha(tokenTextFieldValueOpacity.value)
                    )
                },
                isError = state is BotSetupState.Error,
                supportingText = { TokenTextFieldSupportingText(state) },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(percent = 50),
                modifier = Modifier
                    .width(tokenTextFieldWidth2.value.dp)
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
            enabled = state !is BotSetupState.Loading
        ) {
            Icon(
                imageVector = Icons.Outlined.Clear, contentDescription = "Reset token"
            )
        }
    }
}

@Composable
fun TokenTextFieldPlaceholder(
    state: BotSetupState,
    modifier: Modifier = Modifier
) {
    if (state is BotSetupState.NotConfigured) {
        Text(
            text = stringResource(R.string.bot_api_token_input_placeholder),
            maxLines = 1,
            modifier = modifier
        )
    }
}

@Composable
fun TokenTextFieldSupportingText(state: BotSetupState) {
    (state as? BotSetupState.Error)?.let { Text(text = it.errorMessage) }
}

@Composable
fun TokenTextFieldTrailingIcon(state: BotSetupState) {
    when (state) {
        is BotSetupState.Loading -> {
            CircularProgressIndicator(
                strokeCap = StrokeCap.Round, strokeWidth = 2.dp, modifier = Modifier.size(20.dp)
            )
        }
        is BotSetupState.Error -> {
            Icon(
                imageVector = Icons.Outlined.Info, contentDescription = "An error occurred"
            )
        }
        else -> {}
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
