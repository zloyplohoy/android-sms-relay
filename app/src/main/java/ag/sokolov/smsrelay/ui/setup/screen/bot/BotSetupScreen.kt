package ag.sokolov.smsrelay.ui.setup.screen.bot

import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
        onContinue = onContinue, state = state, onTokenValueChange = viewModel::onTokenValueChange
    )
}

@Composable
internal fun BotSetupScreen(
    onContinue: () -> Unit,
    state: BotSetupState,
    onTokenValueChange: (value: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(36.dp)
    ) {
        BotSetupHeader()
        BotSetupDescription()
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            BotConfigurationBlockHeader()
            BotConfigurationBlock(state = state, onTokenValueChange = onTokenValueChange)
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(enabled = state is BotSetupState.Configured, onClick = onContinue) {
                Text(text = "Next")
            }
        }
    }

}

@Composable
fun BotSetupHeader() {
    Text(text = "Set up a Telegram bot", style = MaterialTheme.typography.headlineSmall)
}

@Composable
fun BotSetupDescription() {
    Text(
        text = "aldjsfklha skjdhf kjashdf ksadhk hsadkjh fkjasdh fakjsdf kjash kjhasdkj hkajsd hfgaksjd hkjsah kjdshf kjhafkjg kajf kjadfhs kjhaskj haksjh gkjask jghsdfk jgksah kahsk jhsakjg kjas kjhsdf kjhfg kjhakdfsjh ksajjka shkj akja hkas kj ahdsakjh g",
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Justify,
    )
}

@Composable
fun BotConfigurationBlockHeader() {
    Text(style = MaterialTheme.typography.labelMedium, text = "Bot configuration")
}

@Composable
fun BotConfigurationBlock(
    state: BotSetupState,
    onTokenValueChange: (value: String) -> Unit
) {
    if (state is BotSetupState.Configured) {
        BotDetailsBlock(state)
    } else {
        TokenTextField(
            isEnabled = state !is BotSetupState.Loading,
            errorText = if (state is BotSetupState.Error) state.errorMessage else null,
            isLoading = state is BotSetupState.Loading,
            onValueChange = onTokenValueChange
        )
    }
}

@Composable
fun BotDetailsBlock(state: BotSetupState.Configured) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.AccountCircle,
            contentDescription = "Bot icon",
            Modifier.size(36.dp)
        )
        Column {
            Text(text = state.botName, style = MaterialTheme.typography.titleMedium)
            Text(text = "@${state.botUsername}", style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
fun TokenTextField(
    isEnabled: Boolean,
    errorText: String?,
    isLoading: Boolean,
    onValueChange: (value: String) -> Unit
) {
    var token by rememberSaveable { mutableStateOf("") }
    val isError = errorText != null

    OutlinedTextField(modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        enabled = isEnabled,
        value = token,
        onValueChange = {
            token = it
            onValueChange(token)
        },
        placeholder = { Text("Paste bot API token here") },
        trailingIcon = when {
            isLoading -> {
                { InlineCircularProgressIndicator() }
            }
            isError -> {
                { InfoIcon() }
            }
            else -> null
        },
        isError = isError,
        supportingText = { if (isError) Text(text = errorText!!) },
        visualTransformation = PasswordVisualTransformation()
    )
}

@Composable
fun InlineCircularProgressIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.size(20.dp), strokeCap = StrokeCap.Round, strokeWidth = 2.dp
    )
}

@Composable
fun InfoIcon() {
    Icon(imageVector = Icons.Outlined.Info, contentDescription = "An error occurred")
}

@Preview
@Composable
fun PreviewBotSetupScreenNotConfigured() {
    SMSRelayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            BotSetupScreen(onContinue = {},
                state = BotSetupState.NotConfigured,
                onTokenValueChange = {})
        }
    }
}

@Preview
@Composable
fun PreviewBotSetupScreenLoading() {
    SMSRelayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            BotSetupScreen(onContinue = {}, state = BotSetupState.Loading, onTokenValueChange = {})
        }
    }
}

@Preview
@Composable
fun PreviewBotSetupScreenConfigured() {
    SMSRelayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            BotSetupScreen(onContinue = {}, state = BotSetupState.Configured(
                botName = "Awesome Telegram bot", botUsername = "awesome_telegram_bot"
            ), onTokenValueChange = {})
        }
    }
}

@Preview
@Composable
fun PreviewBotSetupScreenError() {
    SMSRelayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            BotSetupScreen(onContinue = {},
                state = BotSetupState.NotConfigured,
                onTokenValueChange = {})
        }
    }
}
