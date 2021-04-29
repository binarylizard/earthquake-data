package com.wildlabs.earthquaky.roomdatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.wildlabs.earthquaky.models.EarthquakeData;
import com.wildlabs.earthquaky.models.Properties;

@Database(entities = Properties.class, version = 1)
@TypeConverters(EarthquakeData.FeatureConverter.class)
public abstract class EarthquakeDB extends RoomDatabase {

    public abstract EarthquakeDao earthquakeDao();

    public static volatile EarthquakeDB earthquakeDBInstance;

    public static EarthquakeDB getDatabase(final Context context){
        if(earthquakeDBInstance == null){
            synchronized (EarthquakeDB.class){
                if(earthquakeDBInstance == null){
                    earthquakeDBInstance = Room.databaseBuilder(context.getApplicationContext(),
                            EarthquakeDB.class, "earthquake"
                            ).build();
                }
            }
        }
        return earthquakeDBInstance;
    }


}
