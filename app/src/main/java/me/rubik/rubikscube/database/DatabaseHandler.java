package me.rubik.rubikscube.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Times.class}, version = 1)
public abstract class DatabaseHandler extends RoomDatabase {
    private static DatabaseHandler db;

    public static void init(DatabaseHandler database) {
        db = database;
    }

    public abstract TimesDao timesDao();

    public static TimesDao getDatabase() {
        return db.timesDao();
    }

}
