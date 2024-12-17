package pppb1.uas.pppb1_uas_music_player.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import pppb1.uas.pppb1_uas_music_player.model.Favorite

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favorite : Favorite)
    @Delete
    fun delete(favorite: Favorite)
    @Query("SELECT * from favorite_db")
    fun getAllFavorite (): LiveData<List<Favorite>>
}