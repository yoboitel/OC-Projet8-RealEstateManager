package com.openclassrooms.realestatemanager.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EstateDao {

    @Insert
    void insert(Estate estate);

    @Update
    void update(Estate estate);

    @Query("SELECT * FROM estate")
    List<Estate> getEstates();

    @Query("SELECT * FROM estate WHERE id = :id")
    Estate getEstateById(int id);

    //Search query
}
