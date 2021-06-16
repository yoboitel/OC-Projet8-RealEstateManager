package com.openclassrooms.realestatemanager.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.Database.DbHelper;
import com.openclassrooms.realestatemanager.Database.Estate;

public class EstateContentProvider extends ContentProvider {

    // FOR DATA
    public static final String AUTHORITY = "com.openclassrooms.realestatemanager.provider";
    public static final String TABLE_NAME = Estate.class.getSimpleName();
    public static final Uri URI_ITEM = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        if (getContext() != null) {
            long id = ContentUris.parseId(uri);
            final Cursor cursor = DbHelper.getInstance(getContext()).getAppDatabase().estateDao().getEstatesWithCursor(id);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }

        throw new IllegalArgumentException("Failed to query row for uri " + uri);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "vnd.android.cursor.item/" + AUTHORITY + "." + TABLE_NAME;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        if (getContext() != null) {
            final long id = DbHelper.getInstance(getContext()).getAppDatabase().estateDao().insert((Estate.fromContentValues(contentValues)));
            if (id != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            }
        }
        throw new IllegalArgumentException("Failed to insert row into " + uri);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        if (getContext() != null) {
            final int count = DbHelper.getInstance(getContext()).getAppDatabase().estateDao().update((Estate.fromContentValues(contentValues)));
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
        throw new IllegalArgumentException("Failed to update row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        throw new IllegalArgumentException("You can't delete anything");
    }
}
