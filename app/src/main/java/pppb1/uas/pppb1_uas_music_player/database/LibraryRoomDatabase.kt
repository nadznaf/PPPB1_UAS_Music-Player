package pppb1.uas.pppb1_uas_music_player.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pppb1.uas.pppb1_uas_music_player.model.Library

@Database(entities = [Library::class], version = 1, exportSchema = false)
abstract class LibraryRoomDatabase : RoomDatabase() {
    abstract fun libraryDao(): LibraryDao

    companion object {
        @Volatile
        private var INSTANCE: LibraryRoomDatabase? = null

        fun getDatabase(context: Context): LibraryRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(LibraryRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        LibraryRoomDatabase::class.java,
                        "library_database"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}