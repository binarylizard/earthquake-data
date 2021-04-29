package com.wildlabs.earthquaky.roomdatabase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.wildlabs.earthquaky.models.Properties;

import java.util.List;

@Dao
public interface EarthquakeDao {

    @Insert
    public void insert(Properties properties);

    @Query("select * from properties")
    public LiveData<List<Properties>> getAllProperties();

}
