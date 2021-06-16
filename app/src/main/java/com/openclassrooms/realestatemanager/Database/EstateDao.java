package com.openclassrooms.realestatemanager.Database;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface EstateDao {

    @Insert
    long insert(Estate estate);

    @Update
    int update(Estate estate);

    @Query("SELECT * FROM estate")
    List<Estate> getEstates();

    @Query("SELECT * FROM estate WHERE id = :id")
    Estate getEstateById(long id);

    //For content provider
    @Query("SELECT * FROM estate WHERE id = :id")
    Cursor getEstatesWithCursor(long id);

    //Search query
    @Query("SELECT * FROM estate WHERE type LIKE :type AND surface BETWEEN :minSurface AND :maxSurface" +
            " AND price BETWEEN :minPrice AND :maxPrice" +
            " AND rooms BETWEEN :minRoom AND :maxRoom " +
            " AND status LIKE :status" +
            " AND dateAvailable BETWEEN :dateMin AND :dateMax"
    )
    List<Estate> searchEstates(String type,
                               Integer minSurface, Integer maxSurface,
                               Integer minPrice, Integer maxPrice,
                               Integer minRoom, Integer maxRoom, String status, Date dateMin, Date dateMax);
}
