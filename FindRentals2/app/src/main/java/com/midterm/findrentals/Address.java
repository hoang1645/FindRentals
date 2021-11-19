package com.midterm.findrentals;

public class Address {
    private int number;
    private String streetName;
    private String city;
    private String district;
    private String ward;
    private Coordinate coordinate;

    public Address(int number, String streetName, String city, String district, String ward, Coordinate coordinate) {
        this.number = number;
        this.streetName = streetName;
        this.city = city;
        this.district = district;
        this.ward = ward;
        this.coordinate = coordinate;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public Address(int number, String streetName, String city, Coordinate cor) {
        this.number = number;
        this.streetName = streetName;
        this.city = city;
        this.coordinate = cor;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
