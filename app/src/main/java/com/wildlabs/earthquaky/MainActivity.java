package com.wildlabs.earthquaky;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wildlabs.earthquaky.models.EarthquakeData;
import com.wildlabs.earthquaky.models.Properties;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "sagar";
    private MainViewModel mainViewModel;
    private RecyclerView rvEarthquake;
    private Spinner spSort;
    private int check = 0, dataSize;
    private SeekBar seekBar;
    private TextView tvSeekbar;
    private ImageView ivClearFilter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private List<EarthquakeData.Feature> featuresList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvEarthquake = findViewById(R.id.rvEarthquake);
        seekBar = findViewById(R.id.seekBar);
        tvSeekbar = findViewById(R.id.tvSeekbar);
        ivClearFilter = findViewById(R.id.ivClearFilter);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        progressBar = findViewById(R.id.progressBar);

        featuresList = new ArrayList<>();
        EarthquakeAdapter adapter = new EarthquakeAdapter(this);
        rvEarthquake.setLayoutManager(new LinearLayoutManager(this));
        rvEarthquake.setAdapter(adapter);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        if(dataSize==mainViewModel.getDataSize()){
                            Toast.makeText(MainActivity.this,"No New Items, Data is already Updated!", Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this,"No New Items, Data is already Updated!", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);

                    }
                }, 1000);
            }
        });

        if(mainViewModel.getPropertiesList()==null){
            mainViewModel.returnEqData().observe(this, new Observer<List<EarthquakeData.Feature>>() {
                @Override
                public void onChanged(List<EarthquakeData.Feature> features) {
                    adapter.setData(features);
                    dataSize = mainViewModel.getDataSize();
                    progressBar.setVisibility(View.GONE);
                    featuresList = features;
                    for(EarthquakeData.Feature feature: featuresList){
                        mainViewModel.insert(feature.properties);
                    }
                }
            });
        } else {
            mainViewModel.getPropertiesList().observe(this, new Observer<List<Properties>>() {
                @Override
                public void onChanged(List<Properties> properties) {
//                    adapter.setData(properties);
                }
            });
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                BigInteger bigInteger = new BigInteger(String.valueOf(progress));
                String magStr = String.valueOf(new BigDecimal(bigInteger,1));

                tvSeekbar.setText("Mag: " + magStr);

                adapter.getFilter().filter(magStr);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ivClearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSeekbar.setText("0");
                adapter.getFilter().filter("");
            }
        });

        spSort= (Spinner) findViewById(R.id.spSort);//fetch the spinner from layout file
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.spinner_array));//setting the country_array to spinner
        spSort.setAdapter(spAdapter);
//if you want to set any action you can do in this listener
        spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(++check>1){
                    String selectedOption = spSort.getSelectedItem().toString();
                    if(selectedOption.equals("Title Wise")){
                        mainViewModel.sortByTitle();
                    } else if(selectedOption.equals("Time Wise")){
                        mainViewModel.sortByTime();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

}