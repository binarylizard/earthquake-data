package com.wildlabs.earthquaky.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "properties")
public class Properties {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    public double mag;
    public String place;
    public long time;
//    public Object updated;
//    public Object tz;
    public String url;
    public String detail;
    public int felt;
    public double cdi;
    public double mmi;
    public String alert;
    public String status;
    public int tsunami;
    public int sig;
    public String net;
    public String code;


    @NonNull
    public String ids;
    public String sources;
    public String types;
    public int nst;
    public double dmin;
    public double rms;
    public double gap;
    public String magType;
    public String type;
    public String title;
}
