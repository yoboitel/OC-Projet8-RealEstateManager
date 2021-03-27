package com.openclassrooms.realestatemanager.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Estate {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "type")
    public String type;
    @ColumnInfo(name = "price")
    public String price;

    public Estate(String type, String price) {
        this.type = type;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
