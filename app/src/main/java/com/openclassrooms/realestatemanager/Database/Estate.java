package com.openclassrooms.realestatemanager.Database;

import android.content.ContentValues;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;

@Entity
public class Estate {
    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo(name = "type")
    public String type;
    @ColumnInfo(name = "price")
    public Integer price;
    @ColumnInfo(name = "surface")
    public Integer surface;
    @ColumnInfo(name = "rooms")
    public Integer rooms;
    @ColumnInfo(name = "description")
    public String description;
    @ColumnInfo(name = "address")
    public String address;
    @ColumnInfo(name = "schools")
    public Boolean schools;
    @ColumnInfo(name = "shops")
    public Boolean shops;
    @ColumnInfo(name = "parks")
    public Boolean parks;
    @ColumnInfo(name = "hospitals")
    public Boolean hospitals;
    @ColumnInfo(name = "status")
    public String status;
    @ColumnInfo(name = "agent")
    public String agent;
    @ColumnInfo(name = "dateAvailable")
    public Date dateAvailable;
    @ColumnInfo(name = "dateSold")
    public Date dateSold;
    @ColumnInfo(name = "photoUrls")
    public ArrayList<String> photoUrls;
    @ColumnInfo(name = "photoDescriptions")
    public ArrayList<String> photoDescriptions;

    //For content provider
    public static Estate fromContentValues(ContentValues values) {
        final Estate item = new Estate();

        if (values.containsKey("type")) item.setType(values.getAsString("type"));
        if (values.containsKey("price")) item.setPrice(values.getAsInteger("price"));
        if (values.containsKey("surface")) item.setSurface(values.getAsInteger("surface"));
        if (values.containsKey("rooms")) item.setRooms(values.getAsInteger("rooms"));
        if (values.containsKey("description"))
            item.setDescription(values.getAsString("description"));
        if (values.containsKey("address")) item.setDescription(values.getAsString("address"));
        if (values.containsKey("schools")) item.setSchools(values.getAsBoolean("schools"));
        if (values.containsKey("shops")) item.setSchools(values.getAsBoolean("shops"));
        if (values.containsKey("parks")) item.setSchools(values.getAsBoolean("parks"));
        if (values.containsKey("hospitals")) item.setSchools(values.getAsBoolean("hospitals"));
        if (values.containsKey("status")) item.setDescription(values.getAsString("status"));
        if (values.containsKey("agent")) item.setDescription(values.getAsString("agent"));
        if (values.containsKey("dateAvailable"))
            item.setDateAvailable((Date) values.get("dateAvailable"));
        if (values.containsKey("dateSold")) item.setDateSold((Date) values.get("dateSold"));
        if (values.containsKey("photoUrls"))
            item.setPhotoUrls((ArrayList<String>) values.get("photoUrls"));
        if (values.containsKey("photoDescriptions"))
            item.setPhotoDescriptions((ArrayList<String>) values.get("photoDescriptions"));


        return item;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSurface() {
        return surface;
    }

    public void setSurface(Integer surface) {
        this.surface = surface;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getSchools() {
        return schools;
    }

    public void setSchools(Boolean schools) {
        this.schools = schools;
    }

    public Boolean getShops() {
        return shops;
    }

    public void setShops(Boolean shops) {
        this.shops = shops;
    }

    public Boolean getParks() {
        return parks;
    }

    public void setParks(Boolean parks) {
        this.parks = parks;
    }

    public Boolean getHospitals() {
        return hospitals;
    }

    public void setHospitals(Boolean hospitals) {
        this.hospitals = hospitals;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public Date getDateAvailable() {
        return dateAvailable;
    }

    public void setDateAvailable(Date dateAvailable) {
        this.dateAvailable = dateAvailable;
    }

    public Date getDateSold() {
        return dateSold;
    }

    public void setDateSold(Date dateSold) {
        this.dateSold = dateSold;
    }

    public ArrayList<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(ArrayList<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public ArrayList<String> getPhotoDescriptions() {
        return photoDescriptions;
    }

    public void setPhotoDescriptions(ArrayList<String> photoDescriptions) {
        this.photoDescriptions = photoDescriptions;
    }
}
