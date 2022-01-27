package com.midterm.findrentals;

public class User {
    private String uid;
    private String name;
    private String email;
    private String tel;
    private String rentals;
    //Constructor
    public User(String uid, String name, String email, String tel, String rentals)
    {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.tel = tel;
        this.rentals = rentals;
    }
    //Experimental constructor
    public User() {
        new User("", "", "", "", "");
    }
    //Get-setters
    public String getUid() {
        return uid;
    }
    public String getName()
    {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getTel() { return tel; }
    public String[] getRentals()
    {
        return this.rentals.split(";");
    }
    public void setUid(String uid)
    {
        this.uid = uid;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public void setRentals(String[] rentals)
    {
        String rentalString = "";
        for (String rental: rentals)
        {
            rentalString += rental;
            rentalString += ";";
        }
        this.rentals = rentalString.substring(0, rentalString.length() - 1);
    }
}
