package com.wildlabs.earthquaky;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wildlabs.earthquaky.models.EarthquakeData;
import com.wildlabs.earthquaky.models.Properties;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EarthquakeDBAdapter extends RecyclerView.Adapter<EarthquakeDBAdapter.ViewHolder> implements Filterable {

    private Context ctx;
    private LayoutInflater layoutInflater;
    private List<Properties> featureList, fullFeatureList;

    public EarthquakeDBAdapter(Context ctx){
        this.ctx = ctx;
        layoutInflater = LayoutInflater.from(ctx);
    }

    public void setData(List<Properties> featureList){
        if(featureList!=null){
            this.featureList = featureList;
            fullFeatureList = new ArrayList<>(featureList);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public EarthquakeDBAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_earthquake, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull EarthquakeDBAdapter.ViewHolder holder, int i) {
        holder.tvMag.setText("Mag: " + featureList.get(i).mag);
        holder.tvPlace.setText("Place: " + featureList.get(i).place);
        holder.tvTitle.setText(featureList.get(i).title);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy (hh:mm a)");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(featureList.get(i).time);
        String formattedTime = dateFormat.format(cal.getTime());
        holder.tvTime.setText("Time: " + formattedTime);
    }

    @Override
    public int getItemCount() {
        if(featureList!=null)
        return featureList.size();
        else return 0;
    }

    @Override
    public Filter getFilter() {
        return magnitudeFilter;
    }

    private Filter magnitudeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Properties> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(fullFeatureList);
            } else {
                String query = constraint.toString().toLowerCase().trim();
                if(query.equals("")){
                    filteredList.addAll(fullFeatureList);
                } else {
                    for(Properties item: fullFeatureList){
                        if(item.mag == Double.valueOf(query)){
                            filteredList.add(item);
                        }
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(featureList!=null){
                featureList.clear();
                featureList.addAll((List) results.values);
                notifyDataSetChanged();
            }
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvPlace, tvTime, tvMag, tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlace = itemView.findViewById(R.id.tvPlace);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvMag = itemView.findViewById(R.id.tvMag);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
