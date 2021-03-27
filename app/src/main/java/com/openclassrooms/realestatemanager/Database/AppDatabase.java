package com.openclassrooms.realestatemanager.Database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Estate.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EstateDao estateDao();
}
