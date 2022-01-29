package com.midterm.findrentals;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class User {
    protected String uid;
    protected String name;
    protected String email;
    protected String tel;
    protected String favorites;
    //Constructor
    public User(String uid, String name, String email, String tel, String rentals)
    {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.tel = tel;
        favorites = rentals;
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
    public String getFavorites()
    {
        return favorites;
    }
    public ArrayList<String> favoritesAsArray()
    {
        ArrayList<String> res = new ArrayList<>(Arrays.asList(favorites.split(";")));
        return res;
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
    public void putFavoritesAsArray(String[] favorites)
    {
        this.favorites = "";
        for (String favorite : favorites)
        {
            this.favorites += favorite + ";";
        }
        this.favorites = this.favorites.substring(0, this.favorites.length() - 1);
    }
    public void setFavorites(String favorites)
    {
        this.favorites = favorites;
    }
}
