package com.profile.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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

    public String getMyCondition() {
        return myCondition;
    }

    public void setMyCondition(String myCondition) {
        this.myCondition = myCondition;
    }

    String myCondition = "";

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
                address.setText(myAdv.get("area"));
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

                    Toast.makeText(mContext, "I'm still working on removing advertise and editing soon", Toast.LENGTH_SHORT).show();

                }
            });

//            cancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
//                    alert.setCancelable(true);
//                    alert.setTitle(R.string.remove);
//                    alert.setIcon(R.mipmap.alarm);
//                    alert.setMessage(R.string.removeMsg);
//                    alert.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
////                            setMyCondition("ok");
////
////                           new RemoveUserAds(mContext , position );
//
//
//                        }
//                    });
//
//                    alert.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
//
//                    alert.create().show();
//
//                }
//            });

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



//    private class RemoveUserAds extends Activity{
//
//        private   RemoveUserAds(final Context context , final int position) {
//
//            // Firebase and facebook user auth \\
//            FirebaseAuth mAuth = FirebaseAuth.getInstance();
//            final FirebaseUser currentUser = mAuth.getCurrentUser();
//
//            /////\\\\
//
//            if (getMyCondition().equals("ok")) {
//                try {
//                    String building = "home";
//                    boolean flag = true;
//
//                    final String myRefString = "users/" + currentUser.getUid();
//                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                    final DatabaseReference ref = database.getReference(myRefString + "/" + building);
//
//                    if (ref != null) {
//                        if (flag) {
//                            flag = false;
//                            ref.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(final DataSnapshot dataSnapshot) {
//
//                                    dataSnapshot.getChildrenCount();
//
//
//                                    Log.e("aaaaaaaaaaacount", String.valueOf(dataSnapshot.getChildrenCount()));
//
//
//                                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
//                                        //instances retrived
//                                        Log.i("Snap", messageSnapshot.toString());
//                                        String key = messageSnapshot.getKey();
//                                        Log.i("key", key);
//
//                                        if (key.equals(String.valueOf(position + 1))) {
//                                            Log.i("my Data", dataSnapshot.getRef().child("home").getRef().toString());
//
//
////                                    dataSnapshot.child("home").
//                                            String path = (String) messageSnapshot.getValue();
//                                            Log.i("retrieving home", path);
//
//
//                                            if (path != null) {
//
//                                                DatabaseReference myRef = database.getReference(path);
//                                                Log.i("myRef", myRef.toString());
//                                                myRef.addValueEventListener(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                                        try {
//                                                            if(getMyCondition().equals("ok")) {
//                                                                dataSnapshot.getRef().removeValue();
//                                                                advArrayList.remove(position);
//                                                                setListSize(mListView);
//                                                                setMyCondition("");
//                                                            }
//                                                        } catch (Exception e) {
//
//                                                        }
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(DatabaseError databaseError) {
//
//                                                    }
//                                                });
//
//                                            }
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
//                        }
//                    }
//
//
//                    building = "chalet";
//
//                    final DatabaseReference ref2 = database.getReference(myRefString + building);
//
//
//                    if (ref2 != null) {
//                        ref2.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(final DataSnapshot dataSnapshot) {
//
//                                dataSnapshot.getChildrenCount();
//
//
//                                Log.e("aaaaaaaaaaacount", String.valueOf(dataSnapshot.getChildrenCount()));
//
//
//                                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
//                                    //instances retrived
//                                    Log.i("Snap", messageSnapshot.toString());
//                                    String key = messageSnapshot.getKey();
//                                    if (key.equals(position)) {
//                                        dataSnapshot.getRef().removeValue();
//                                        String path = (String) messageSnapshot.getValue();
//                                        Log.i("retrieving land", path);
//
//                                        if (path != null) {
//
//                                            DatabaseReference myRef = database.getReference(path);
//                                            myRef.removeValue();
//                                            myRef.addValueEventListener(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                                    advArrayList.remove(position);
//                                                    setListSize(mListView);
//                                                }
//
//                                                @Override
//                                                public void onCancelled(DatabaseError databaseError) {
//
//                                                }
//                                            });
//
//                                        }
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//
//                    }
//
//
//                    building = "hall";
//                    final DatabaseReference ref3 = database.getReference(myRefString + building);
//                    if (ref3 != null) {
//                        ref3.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(final DataSnapshot dataSnapshot) {
//
//                                dataSnapshot.getChildrenCount();
//
//
//                                Log.e("aaaaaaaaaaacount", String.valueOf(dataSnapshot.getChildrenCount()));
//
//
//                                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
//                                    //instances retrived
//                                    Log.i("Snap", messageSnapshot.toString());
//                                    String key = messageSnapshot.getKey();
//                                    if (key.equals(position)) {
//                                        dataSnapshot.getRef().removeValue();
//                                        String path = (String) messageSnapshot.getValue();
//                                        Log.i("retrieving land", path);
//
//                                        if (path != null) {
//
//                                            DatabaseReference myRef = database.getReference(path);
//                                            myRef.removeValue();
//                                            myRef.addValueEventListener(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                                    advArrayList.remove(position);
//                                                    setListSize(mListView);
//                                                }
//
//                                                @Override
//                                                public void onCancelled(DatabaseError databaseError) {
//
//                                                }
//                                            });
//
//                                        }
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//
//                    }
//                    building = "store";
//                    final DatabaseReference ref4 = database.getReference(myRefString + building);
//                    if (ref4 != null) {
//                        ref4.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(final DataSnapshot dataSnapshot) {
//
//                                dataSnapshot.getChildrenCount();
//
//
//                                Log.e("aaaaaaaaaaacount", String.valueOf(dataSnapshot.getChildrenCount()));
//
//
//                                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
//                                    //instances retrived
//                                    Log.i("Snap", messageSnapshot.toString());
//                                    String key = messageSnapshot.getKey();
//                                    if (key.equals(position)) {
//                                        dataSnapshot.getRef().removeValue();
//                                        String path = (String) messageSnapshot.getValue();
//                                        Log.i("retrieving land", path);
//
//                                        if (path != null) {
//
//                                            DatabaseReference myRef = database.getReference(path);
//                                            myRef.removeValue();
//                                            myRef.addValueEventListener(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                                    advArrayList.remove(position);
//                                                    setListSize(mListView);
//                                                }
//
//                                                @Override
//                                                public void onCancelled(DatabaseError databaseError) {
//
//                                                }
//                                            });
//
//                                        }
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//
//                    }
//                    building = "land";
//                    final DatabaseReference ref5 = database.getReference(myRefString + building);
//                    if (ref5 != null) {
//                        ref5.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(final DataSnapshot dataSnapshot) {
//
//                                dataSnapshot.getChildrenCount();
//
//
//                                Log.e("aaaaaaaaaaacount", String.valueOf(dataSnapshot.getChildrenCount()));
//
//
//                                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
//                                    //instances retrived
//                                    Log.i("Snap", messageSnapshot.toString());
//                                    String key = messageSnapshot.getKey();
//                                    if (key.equals(position)) {
//                                        dataSnapshot.getRef().removeValue();
//                                        String path = (String) messageSnapshot.getValue();
//                                        Log.i("retrieving land", path);
//
//                                        if (path != null) {
//
//                                            DatabaseReference myRef = database.getReference(path);
//                                            myRef.removeValue();
//                                            myRef.addValueEventListener(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                                    advArrayList.remove(position);
//                                                    setListSize(mListView);
//                                                }
//
//                                                @Override
//                                                public void onCancelled(DatabaseError databaseError) {
//
//                                                }
//                                            });
//
//                                        }
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//
//                    }
//                } catch (Exception e) {
//                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//
//    }


}