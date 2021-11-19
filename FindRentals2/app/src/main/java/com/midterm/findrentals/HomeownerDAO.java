package com.midterm.findrentals;

import androidx.room.*;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface HomeownerDAO {
    //Data manipulation queries
    @Insert
    public void insert(Homeowner ho);
    @Update
    public void update(Homeowner... hos);

    //Get all from db
    @Query("select * from homeowner order by name asc")
    public List<Homeowner> getAllAscending();
    @Query("select * from homeowner order by name desc")
    public List<Homeowner> getAllDescending();

    //Find by ID
    @Query("select * from homeowner where homeowner_id = :id")
    public List<Homeowner> getID(int id);

}
