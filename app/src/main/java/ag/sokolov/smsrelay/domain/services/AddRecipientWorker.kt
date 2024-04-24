package ag.sokolov.smsrelay.domain.services

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class AddRecipientWorker(
    appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork() : Result {
        return Result.success()
    }
}
