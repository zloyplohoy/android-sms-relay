package ag.sokolov.smsrelay.ui.setup

import ag.sokolov.smsrelay.ui.setup.screen.bot.BotState

data class SetupState(
    var botState: BotState = BotState.Loading
)
