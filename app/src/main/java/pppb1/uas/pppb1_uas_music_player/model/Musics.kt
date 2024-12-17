package pppb1.uas.pppb1_uas_music_player.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "musics_db")
data class Musics(
    @SerializedName("_id")
    val id: String ?  = null,
    @SerializedName("song_name")
    val song_name: String,
    @SerializedName("artist")
    val artist: String,
    @SerializedName("album_name")
    val album_name: String,
    @SerializedName("release")
    val release: String
)