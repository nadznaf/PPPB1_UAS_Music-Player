package pppb1.uas.pppb1_uas_music_player.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity("musics_db")
data class Musics (
    @PrimaryKey
    @SerializedName("id")
    val id: String = "",
    @SerializedName("song_name")
    val judul: String,
    @SerializedName("artist")
    val artis: String,
    @SerializedName("album_name")
    val album: String,
    @SerializedName("release")
    val rilis: String,
    @SerializedName("picture")
    val gambar: Int,
)

