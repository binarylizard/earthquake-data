package com.wildlabs.earthquaky;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.wildlabs.earthquaky.models.EarthquakeData;
import com.wildlabs.earthquaky.models.Properties;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyRepository {

    private static MyRepository instance;

    // Note the use of MutableLiveData, this allows changes to be made
    @NonNull
    private MutableLiveData<List<Properties>> myLiveData = new MutableLiveData<>();

    public static MyRepository getInstance() {
        if(instance == null) {
            synchronized (MyRepository.class) {
                if(instance == null) {
                    instance = new MyRepository();
                }
            }
        }
        return instance;
    }

    // The getter upcasts to LiveData, this ensures that only the repository can cause a change
    @NonNull
    public LiveData<List<Properties>> getMyLiveData() {
        return myLiveData;
    }

    // This method runs some work for 3 seconds. It then posts a status update to the live data.
    // This would effectively be the "doInBackground" method from AsyncTask.
    public void doSomeStuff() {
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
            }

//            myLiveData.postValue("Updated time: "+System.currentTimeMillis());
        }).start();
    }

    public void sortByTitle(){
            Collections.sort(myLiveData.getValue(), new Comparator<Properties>() {
                @Override
                public int compare(Properties o1, Properties o2) {
                    return o1.title.compareTo(o2.title);

                }
            });
            myLiveData.postValue(myLiveData.getValue());
    }

}
