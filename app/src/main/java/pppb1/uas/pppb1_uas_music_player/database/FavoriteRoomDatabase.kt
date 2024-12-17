package pppb1.uas.pppb1_uas_music_player.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pppb1.uas.pppb1_uas_music_player.model.Favorite

@Database(entities = [Favorite::class], version = 1, exportSchema = false)
abstract class FavoriteRoomDatabase : RoomDatabase() {
    abstract fun favoriteDao() : FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteRoomDatabase? = null
        fun getDatabase(context: Context): FavoriteRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(FavoriteRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteRoomDatabase::class.java,
                        "favorite_database"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}