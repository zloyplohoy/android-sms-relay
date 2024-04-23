package ag.sokolov.smsrelay.domain.use_cases.is_telegram_installed_use_case

import android.content.Context
import android.content.pm.PackageManager

class IsTelegramInstalledUseCase() {
    operator fun invoke(context: Context): Boolean = try {
        context.packageManager.getPackageInfo("org.telegram.messenger", PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}
