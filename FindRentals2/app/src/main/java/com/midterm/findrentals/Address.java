package com.midterm.findrentals;

public class Address {
    private String addressString;
    private Coordinate coordinate;

    public Address(String addressString, Coordinate coordinate) {
        this.addressString = addressString;
        this.coordinate = coordinate;
    }

    public String getAddressString() {
        return addressString;
    }

    public void setAddressString(String addressString) {
        this.addressString = addressString;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}
