package com.example.currentplacedetailsonmap;

public class User {
    int priceboundFilter;
    String lotTypeFilter, specialtyFilter, username, password;

    public User(String username) {
        this.username = username;
    }
    public User(String username, String password, int priceboundFilter, String lotTypeFilter, String specialtyFilter) {
        this.username = username;
        this.priceboundFilter = priceboundFilter;
        this.lotTypeFilter = lotTypeFilter;
        this.specialtyFilter = specialtyFilter;
        this.password = password;
    }

    public int getPriceboundFilter() {
        return priceboundFilter;
    }

    public String getLotTypeFilter() {
        return lotTypeFilter;
    }

    public String getSpecialtyFilter() {
        return specialtyFilter;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword(){return password;}

    public void setLotTypeFilter(String lotTypeFilter) {
        this.lotTypeFilter = lotTypeFilter;
    }

    public void setPriceboundFilter(int priceboundFilter) {
        this.priceboundFilter = priceboundFilter;
    }

    public void setSpecialtyFilter(String specialtyFilter) {
        this.specialtyFilter = specialtyFilter;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) { this.password = password; }
}
