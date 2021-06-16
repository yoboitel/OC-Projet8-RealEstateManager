package com.openclassrooms.realestatemanager.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.openclassrooms.realestatemanager.Utils.Converters;

@Database(entities = {Estate.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract EstateDao estateDao();
}
