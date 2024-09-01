package ag.sokolov.smsrelay.data.database.converter

import androidx.room.TypeConverter
import java.time.Instant


internal class InstantConverter {
    @TypeConverter
    fun epochMilliToInstant(value: Long?): Instant? =
        value?.let(Instant::ofEpochMilli)

    @TypeConverter
    fun instantToEpochMilli(instant: Instant?): Long? =
        instant?.toEpochMilli()
}
