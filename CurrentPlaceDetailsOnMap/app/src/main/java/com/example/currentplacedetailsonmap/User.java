package com.example.currentplacedetailsonmap;

public class User {
    String lotTypeFilter, specialtyFilter, username, password, priceboundFilter;

    public User(String username) {
        this.username = username;
    }
    public User(String username, String password, String priceboundFilter, String lotTypeFilter, String specialtyFilter) {
        this.username = username;
        this.priceboundFilter = priceboundFilter;
        this.lotTypeFilter = lotTypeFilter;
        this.specialtyFilter = specialtyFilter;
        this.password = password;
    }

    public String getPriceboundFilter() {
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

    public void setPriceboundFilter(String priceboundFilter) {
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
