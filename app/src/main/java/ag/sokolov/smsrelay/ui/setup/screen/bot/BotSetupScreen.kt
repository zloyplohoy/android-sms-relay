package ag.sokolov.smsrelay.ui.setup.screen.bot

import ag.sokolov.smsrelay.R
import ag.sokolov.smsrelay.ui.setup.SetupScreen
import ag.sokolov.smsrelay.ui.setup.common.element.contact_list_item.ContactListItem
import ag.sokolov.smsrelay.ui.setup.common.element.ordered_list.orderedList
import ag.sokolov.smsrelay.ui.setup.common.element.setup_step_section.SetupStepSection
import ag.sokolov.smsrelay.ui.setup.common.element.setup_step_title.SetupStepTitle
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
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
        onTokenValueChange = viewModel::onTokenValueChange,
        onTokenReset = viewModel::onTokenReset
    )
}

@Composable
internal fun BotSetupScreen(
    state: BotSetupState,
    onContinue: () -> Unit,
    onTokenValueChange: (value: String) -> Unit,
    onTokenReset: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        SetupStepTitle(stringResource(R.string.bot_setup_step_title))
        SetupStepSection(name = "Bot setup instructions") {
            Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                BotSetupDescription()
                TokenInputBlock(
                    state = state, onValueChange = onTokenValueChange, onReset = onTokenReset
                )
            }
        }
        AnimatedVisibility(
            visible = state is BotSetupState.Configured, enter = fadeIn(), exit = fadeOut()
        ) {
            SetupStepSection(name = "Bot configuration") {
                BotDetails(state = state)
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

    val isTokenInputEnabled = state is BotSetupState.NotConfigured || state is BotSetupState.Error
    val tokenValue =
        if (state is BotSetupState.Configured) stringResource(R.string.sample_telegram_bot_api_token) else token
    val isTokenInputError = state is BotSetupState.Error
    val tokenInputErrorMessage = (state as? BotSetupState.Error)?.errorMessage
    val isLoading = state is BotSetupState.Loading
    val isResetButtonEnabled = state !is BotSetupState.Loading

    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(value = tokenValue,
            onValueChange = { value ->
                token = value
                onValueChange(value)
            },
            enabled = isTokenInputEnabled,
            singleLine = true,
            placeholder = { Text(text = stringResource(R.string.bot_api_token_input_placeholder)) },
            trailingIcon = when {
                isLoading -> {
                    {
                        CircularProgressIndicator(
                            strokeCap = StrokeCap.Round,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                isTokenInputError -> {
                    {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "An error occurred"
                        )
                    }
                }
                else -> null
            },
            isError = isTokenInputError,
            supportingText = { tokenInputErrorMessage?.let { Text(text = it) } },
            visualTransformation = PasswordVisualTransformation(),
            textStyle = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(percent = 50)
        )
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
            enabled = isResetButtonEnabled
        ) {
            Icon(
                imageVector = Icons.Outlined.Clear, contentDescription = "Reset token"
            )
        }
    }
}

@Composable
fun BotDetails(state: BotSetupState) {
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
                    onTokenValueChange = {},
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
                    onTokenValueChange = {},
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
                ), onContinue = {}, onTokenValueChange = {}, onTokenReset = {})
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
                    onTokenValueChange = {},
                    onTokenReset = {})
            }
        }
    }
}
