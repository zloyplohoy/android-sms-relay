package ag.sokolov.smsrelay.data.database.dao

import ag.sokolov.smsrelay.data.database.model.RecipientEntity
import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipientDao {
    @Query(
        value = """
            SELECT *
            FROM recipients
            ORDER BY added_at ASC
        """
    )
    fun getRecipients(): Flow<List<RecipientEntity>>
}
