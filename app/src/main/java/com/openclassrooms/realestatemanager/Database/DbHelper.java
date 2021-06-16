package com.openclassrooms.realestatemanager.Database;

import android.content.Context;

import androidx.room.Room;

public class DbHelper {

    private static DbHelper mInstance;
    private Context mCtx;
    //our app database object
    private AppDatabase appDatabase;

    private DbHelper(Context mCtx) {
        this.mCtx = mCtx;

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "EstateDb").build();
    }

    public static synchronized DbHelper getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DbHelper(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
