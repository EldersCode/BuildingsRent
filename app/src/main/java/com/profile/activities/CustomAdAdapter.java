package com.profile.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.buildingsrent.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hesham on 8/21/2017.
 */

public class CustomAdAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<HashMap<String, String>> advArrayList;
    private ListView mListView;
    private static LayoutInflater inflater = null;
    int firstSize = 0 ;

    public CustomAdAdapter(Context context, ArrayList<HashMap<String, String>> data , ListView listView) {
        mContext = context;
        advArrayList = data;
        mListView = listView;
        firstSize = data.size();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return advArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.my_advertise_profile_row ,null);
            final TextView price = (TextView) view.findViewById(R.id.priceAdsList);
            final TextView address = (TextView) view.findViewById(R.id.addressAdsList);
            ImageView image = (ImageView) view.findViewById(R.id.imageAdsList);
            ImageView cancel = (ImageView) view.findViewById(R.id.removeAd);
            CardView cardView =  (CardView) view.findViewById(R.id.cardView1);


                HashMap<String, String> myAdv;
                myAdv = advArrayList.get(position);
                price.setText(myAdv.get("price"));
                address.setText(myAdv.get("address"));
                final String cardImage = myAdv.get("cardImage");
//                try {
//
//                   String  cardImage = myAdv.get("cardImage");
                    if (!cardImage.equals("")) {
//                        byte[] b = Base64.decode(cardImage, Base64.DEFAULT);
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
//                        image.setImageBitmap(bitmap);
                        int bitmap = Integer.parseInt(cardImage);
                        image.setImageResource(bitmap);
                    }
//                } catch (Exception e) {
//                }


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(mContext, "Position is : " + position, Toast.LENGTH_SHORT).show();

                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                    alert.setCancelable(true);
                    alert.setTitle(R.string.remove);
                    alert.setIcon(R.mipmap.alarm);
                    alert.setMessage(R.string.removeMsg);
                    alert.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(mContext , ProfileActivity.class);
                            intent.putExtra("change" , "change");
                            mContext.startActivity(intent);
                            advArrayList.remove(position);
                            if (advArrayList.size() != firstSize){
                                setListSize(mListView) ;

                            }


                        }
                    });

                    alert.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    alert.create().show();

                }
            });

            }
        return view;
    }


    public void setListSize(ListView listView){
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight =0 ;
        View view = null;
        for(int i=0 ; i < listAdapter.getCount();i++){
            view = listAdapter.getView(i , view , listView);

            if(i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth , ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth , View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();

        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


}