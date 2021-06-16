package com.openclassrooms.realestatemanager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.Database.AppDatabase;
import com.openclassrooms.realestatemanager.provider.EstateContentProvider;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class EstateContentProviderTest {

    //Delete app before running this test, to clean the db, so the test can pass.

    // DATA SET FOR TEST
    private static long ID = 1;
    // FOR DATA
    private ContentResolver mContentResolver;

    @Before
    public void setUp() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        mContentResolver = InstrumentationRegistry.getContext().getContentResolver();
    }

    @Test
    public void getItemsWhenNoItemInserted() {
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(EstateContentProvider.URI_ITEM, ID), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }

    @Test
    public void insertAndGetItem() {
        // BEFORE : Adding demo item
        final Uri userUri = mContentResolver.insert(EstateContentProvider.URI_ITEM, generateItem());
        // TEST
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(EstateContentProvider.URI_ITEM, ID), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(1));
        assertThat(cursor.moveToFirst(), is(true));
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("description")), is("toto"));
    }

    // ---

    private ContentValues generateItem() {
        final ContentValues values = new ContentValues();

        values.put("description", "toto");

        return values;
    }

}
