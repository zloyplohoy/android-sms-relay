package ag.sokolov.smsrelay.ui.setup.screen.recipient

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.lang.Thread.sleep

class AddRecipientWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        repeat(5) {
            Log.d("TAG", "doWork: Doing work, no shit")
            sleep(10_000)
        }
        return Result.success()
    }
}
