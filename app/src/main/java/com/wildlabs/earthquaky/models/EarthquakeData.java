package com.wildlabs.earthquaky.models;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class EarthquakeData {

    public int id;

    public List<Feature> features;

    public class Feature {

        public String type;


        public Properties properties;
//        public Geometry geometry;

        public String id;

    }

    public class FeatureConverter{

        @TypeConverter
        public List<String> fromString(String string){
//            TypeToken tt = TypeToken.get()
            return new Gson().fromJson(string, new TypeToken<List<String>>(){}.getType());
        }

        @TypeConverter
        public String fromArrayList(List<EarthquakeData.Feature> featureList){
            String str = new Gson().toJson(featureList);
            return str;
        }

    }


}

