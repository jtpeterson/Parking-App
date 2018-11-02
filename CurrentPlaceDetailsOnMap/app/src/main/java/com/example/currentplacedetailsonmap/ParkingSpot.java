package com.example.currentplacedetailsonmap;
import java.lang.Math.*;

public class ParkingSpot {
    public String cost, lotType, specialty, id;
    public boolean isAvailable;
    public double latitude, longitude;

    public ParkingSpot() {

    }

    public ParkingSpot(String cost, String lotType, String specialty, boolean isAvailable,
                       double latitude, double longitude) {
        this.cost = cost;
        this.lotType = lotType;
        this.specialty = specialty;
        this.isAvailable = isAvailable;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = "" + Math.abs(latitude) + "" + Math.abs(longitude);
        this.id = id.replaceAll("[^0-9]","");
    }

    public ParkingSpot(boolean isAvailable, double latitude, double longitude) {
        this.isAvailable = isAvailable;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = "" + Math.abs(latitude) + "" + Math.abs(longitude);
        this.id = id.replaceAll("[^0-9]","");
    }

    public String getCost() {
        return cost;
    }

    public String getLotType() {
        return lotType;
    }

    public String getSpecialty() {
        return specialty;
    }

    public boolean getisAvailable() {
        return isAvailable;
    }

    public void setisAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public String getId() {
        return id;
    }
}
