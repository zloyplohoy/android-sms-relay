package ag.sokolov.smsrelay.domain.use_case.is_telegram_installed_use_case

import android.content.Context
import android.content.pm.PackageManager
import javax.inject.Inject

class IsTelegramInstalledUseCase @Inject constructor(
    private val context: Context
) {
    operator fun invoke(): Boolean = try {
        context.packageManager.getPackageInfo(
            "org.telegram.messenger", PackageManager.GET_ACTIVITIES
        )
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}
