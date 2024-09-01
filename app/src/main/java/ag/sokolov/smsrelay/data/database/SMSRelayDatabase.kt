package ag.sokolov.smsrelay.data.database

import ag.sokolov.smsrelay.data.database.converter.InstantConverter
import ag.sokolov.smsrelay.data.database.dao.RecipientDao
import ag.sokolov.smsrelay.data.database.model.RecipientEntity
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        RecipientEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    InstantConverter::class
)
abstract class SMSRelayDatabase: RoomDatabase() {
    abstract fun recipientDao(): RecipientDao
}
