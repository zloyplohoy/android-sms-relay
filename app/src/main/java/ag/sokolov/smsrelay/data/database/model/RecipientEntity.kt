package ag.sokolov.smsrelay.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "recipients")
data class RecipientEntity(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String?,
    val username: String?,
    @ColumnInfo(name = "added_at")
    val addedAt: Instant = Instant.now()
)
