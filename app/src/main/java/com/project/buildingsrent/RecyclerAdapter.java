package com.project.buildingsrent;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Created by Hesham.Adawy on 4/4/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.PredictionHolder> implements Filterable{


    private ArrayList<AT_Place> myResultList;
    private GoogleApiClient myApiClient;
    private LatLngBounds myBounds;
    private AutocompleteFilter myACFilter;

    private Context myContext;
    private int layout;

    public RecyclerAdapter (Context context , int resource , GoogleApiClient googleApiClient ,
                            LatLngBounds bounds , AutocompleteFilter filter){
        myContext = context;
        layout = resource;
        myApiClient = googleApiClient;
        myBounds = bounds;
        myACFilter = filter;

    }


    public void setMyBounds(LatLngBounds bounds){myBounds = bounds;}




    @Override
    public PredictionHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(layout , parent, false);
        PredictionHolder mPredictionHolder = new PredictionHolder(convertView);
        return mPredictionHolder;

    }

    @Override
    public void onBindViewHolder(PredictionHolder holder, int position) {
        holder.myPrediction.setText(myResultList.get(position).description);
    }



    @Override
    public int getItemCount() {
        if (myResultList != null)
            return myResultList.size();
        else
            return 0;
    }

    public AT_Place getItem(int position) {
        return myResultList.get(position);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null){
                    myResultList = getAutoComplete(constraint);
                    if(myResultList != null){
                        results.values = myResultList;
                        results.count = myResultList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if(results != null && results.count > 0){
                    notifyDataSetChanged();
                }else {

                }

            }
        };
        return filter;
    }

    private ArrayList<AT_Place>getAutoComplete(CharSequence constraint){

        if(myApiClient.isConnected()){
            PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi
                            .getAutocompletePredictions(myApiClient,constraint.toString(),myBounds,myACFilter);
            AutocompletePredictionBuffer autocompletePredictions =results
                    .await(60 , TimeUnit.SECONDS);

            final Status status = autocompletePredictions.getStatus();
            if(!status.isSuccess()){
                Toast.makeText(myContext, "error contacting API: please check internet connection ..", Toast.LENGTH_SHORT).show();
                Log.e("", "Error getting autocomplete prediction API call "+status.toString());
                return null;

            }

            Log.i("" , "Query complete. Received "+autocompletePredictions.getCount()+ " predictions.");


            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
            ArrayList resultList = new ArrayList<>(autocompletePredictions.getCount());
            while (iterator.hasNext()){
                AutocompletePrediction prediction = iterator.next();
                resultList.add(new AT_Place(prediction.getPlaceId() , prediction.getFullText(null)));
            }

            autocompletePredictions.release();
            return resultList;

        }

        Log.e("" , "Google API Client is not connected for autocomplete query");
        return null;
    }








    public class PredictionHolder extends RecyclerView.ViewHolder {

        private TextView myPrediction;
        private RelativeLayout myRow;


        public PredictionHolder(View itemView) {
            super(itemView);
            myPrediction = (TextView) itemView.findViewById(R.id.address);
            myRow = (RelativeLayout) itemView.findViewById(R.id.recycler_row);


        }
    }

    public class  AT_Place {

        public CharSequence placeId;
        public CharSequence description;

        public AT_Place(CharSequence placeId, CharSequence description) {
            this.placeId = placeId;
            this.description = description;
        }

        @Override
        public String toString() {
            return description.toString();
        }
    }




}
