package pppb1.uas.pppb1_uas_music_player.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_db")
data class Favorite(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "song_name")
    val song_name: String,
    @ColumnInfo(name = "artist")
    val artist: String,
    @ColumnInfo(name = "album_name")
    val album_name: String,
    @ColumnInfo(name = "release")
    val release: String
)