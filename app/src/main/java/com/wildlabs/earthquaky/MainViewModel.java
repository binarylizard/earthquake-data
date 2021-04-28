package com.wildlabs.earthquaky;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.wildlabs.earthquaky.models.EarthquakeData;
import com.wildlabs.earthquaky.models.Properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel

    private ApiInterface apiInterface;
    private MutableLiveData<List<EarthquakeData.Feature>> featuresList;
    private List<EarthquakeData.Feature> arrayList;
    private int dataSize;
    private EarthquakeDB earthquakeDB;
    private EarthquakeDao earthquakeDao;
    private LiveData<List<Properties>> propertiesList;

    public MainViewModel(@NonNull Application application){
        super(application);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        getEqData();
        arrayList = new ArrayList<>();
        featuresList = new MutableLiveData<>();
        earthquakeDB = EarthquakeDB.getDatabase(application);
        earthquakeDao = earthquakeDB.earthquakeDao();
        propertiesList = earthquakeDao.getAllProperties();

    }

    public void insert(Properties properties){
        new InsertAsycTask(earthquakeDao).execute(properties);
    }

    public LiveData<List<Properties>> getPropertiesList(){
        return propertiesList;
    }

    public void sortByTitle(){
        Collections.sort(featuresList.getValue(), new Comparator<EarthquakeData.Feature>() {
            @Override
            public int compare(EarthquakeData.Feature o1, EarthquakeData.Feature o2) {
                return o1.properties.title.compareTo(o2.properties.title);

            }
        });
        featuresList.postValue(featuresList.getValue());
    }

    public void sortByTime() {
        Collections.sort(featuresList.getValue(), new Comparator<EarthquakeData.Feature>() {
            @Override
            public int compare(EarthquakeData.Feature o1, EarthquakeData.Feature o2) {
                return String.valueOf(o1.properties.time).compareTo(String.valueOf(o2.properties.time));
            }
        });
        featuresList.postValue(featuresList.getValue());
    }

    public void getEqData(){

        apiInterface.getTodos().enqueue(new Callback<EarthquakeData>() {
            @Override
            public void onResponse(Call<EarthquakeData> call, Response<EarthquakeData> response) {
                featuresList.postValue(response.body().features);
                dataSize = response.body().features.size();
            }

            @Override
            public void onFailure(Call<EarthquakeData> call, Throwable t) {
                System.out.println("Failure -> " + t.getMessage());
            }
        });
    }

    public int getDataSize(){
        return dataSize;
    }

    public MutableLiveData<List<EarthquakeData.Feature>> returnEqData(){
        return featuresList;
    }


    private class InsertAsycTask extends AsyncTask<Properties, Void, Void> {

        private EarthquakeDao earthquakeDao;

        public InsertAsycTask(EarthquakeDao earthquakeDao){
            this.earthquakeDao = earthquakeDao;
        }

        @Override
        protected Void doInBackground(Properties... properties) {
            earthquakeDao.insert(properties[0]);
            return null;
        }
    }
}