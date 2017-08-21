package com.profile.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.buildingsrent.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hesham on 8/21/2017.
 */

public class CustomAdAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<HashMap<String , String>> advArrayList;
    private static LayoutInflater inflater = null;

    public CustomAdAdapter(Context context , ArrayList<HashMap<String,String>> data ) {
        mContext = context;
        advArrayList = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return 6;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(convertView == null){
            view = inflater.inflate(R.layout.my_advertise_profile_row , null);
            TextView price = (TextView) view.findViewById(R.id.priceAdsList);
            TextView address = (TextView) view.findViewById(R.id.addressAdsList);
            ImageView image = (ImageView) view.findViewById(R.id.imageAdsList);
            HashMap<String , String > myAdv = new HashMap<>();
            myAdv = advArrayList.get(position);

            price.setText(myAdv.get("price"));
            address.setText(myAdv.get("address"));
            try{

               String cardImage = myAdv.get("cardImage");
                if( ! cardImage.equals("")) {
                    byte[] b = Base64.decode(cardImage, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    image.setImageBitmap(bitmap);
                }

            }catch (Exception e){

            }
        }
        return view;
    }
}
