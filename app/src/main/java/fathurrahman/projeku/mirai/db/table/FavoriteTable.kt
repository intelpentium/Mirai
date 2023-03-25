package fathurrahman.projeku.mirai.db.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val status: String,
)