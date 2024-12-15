package pppb1.uas.pppb1_uas_music_player.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import pppb1.uas.pppb1_uas_music_player.model.Library

@Dao
interface LibraryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(library: Library)

    @Query("SELECT * FROM library_table")
    fun getAllLibrary(): LiveData<List<Library>>

    @Delete
    fun delete(library: Library)
}