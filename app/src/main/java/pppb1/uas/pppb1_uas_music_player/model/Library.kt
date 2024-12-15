package pppb1.uas.pppb1_uas_music_player.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName

@Entity(tableName = "library_table")
data class Library (
    @PrimaryKey
    val id: String,
    @ColumnInfo("song_name")
    val judul: String,
    @ColumnInfo("artist")
    val artis: String,
    @ColumnInfo("album_name")
    val album: String,
    @ColumnInfo("release")
    val rilis: String,
)