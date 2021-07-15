package com.appsinventiv.mrapplianceadmin.Activities.Tracking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appsinventiv.mrapplianceadmin.Activities.GPSTrackerActivity;
import com.appsinventiv.mrapplianceadmin.Adapters.TrackingStaffNamesAdapter;
import com.appsinventiv.mrapplianceadmin.Models.MyMarker;
import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Servicemen.ServicemanModel;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;
import com.appsinventiv.mrapplianceadmin.Utils.GetAddress;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class TrackStaff extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<MyMarker> mMyMarkersArray = new ArrayList<MyMarker>();
    private HashMap<Marker, MyMarker> mMarkersHashMap = new HashMap<>();
    private RelativeLayout pinView;
    DatabaseReference mDatabase;
    private ArrayList<ServicemanModel> riderList = new ArrayList();
    private LocationManager manager;
    private double lng;
    private double lat;
    HashMap<String, Marker> myMap = new HashMap<>();
    private String trackingUsersNames = "";
    RecyclerView trackingRecycler;
    TrackingStaffNamesAdapter trackingStaffNamesAdapter;
    private ArrayList<ServicemanModel> trackingNamesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_track_staff);
        pinView = findViewById(R.id.pinView);
        trackingRecycler = findViewById(R.id.trackingRecycler);
        trackingRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        trackingStaffNamesAdapter = new TrackingStaffNamesAdapter(this, trackingNamesList, new TrackingStaffNamesAdapter.TrackingStaffNamesAdapterCallbacks() {
            @Override
            public void onClick(ServicemanModel model) {
                double lasad = myMap.get(model.getMobile()).getPosition().latitude;
                double lasdas = myMap.get(model.getMobile()).getPosition().longitude;
                LatLng newPosition = new LatLng(lasad, lasdas);
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 13));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 13));
            }
        });
        trackingRecycler.setAdapter(trackingStaffNamesAdapter);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            CommonUtils.showToast("Please turn on GPS");
            final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);

        } else {
            getPermissions();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle extras = data.getExtras();
                lng = extras.getDouble("Longitude");
                lat = extras.getDouble("Latitude");

                LatLng newPosition = new LatLng(lat, lng);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 13));

                setUpMap();
                getDataFromServer();
            }
        }
    }

    private void getDataFromServer() {
        trackingUsersNames = "";
        mDatabase.child("Servicemen").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getValue() != null) {
                    ServicemanModel model = dataSnapshot.getValue(ServicemanModel.class);
                    if (model.isActive()) {
                        if (model != null && model.getName() != null && model.getLatitude() != 0) {
                            trackingNamesList.add(model);
                            trackingStaffNamesAdapter.setItemList(trackingNamesList);
                            MyMarker myMarker = new MyMarker(model.getName(),
                                    model.getImageUrl(), model.getLatitude(), model.getLongitude(), model.getMobile());
                            plotSingleMarker(myMarker);
                        }
                    }
                } else {
                    riderList.clear();
                    CommonUtils.showToast("No Data");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getValue() != null) {
                    ServicemanModel model = dataSnapshot.getValue(ServicemanModel.class);
                    if (model.isActive() && model.getLatitude() != 0) {
                        MyMarker myMarker = new MyMarker(model.getName(),
                                model.getImageUrl(), model.getLatitude(), model.getLongitude(), model.getMobile());
                        plotSingleMarker(myMarker);
                    } else {
                        Marker marr = myMap.get(model.getMobile());
                        if (marr != null) {
                            marr.remove();
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPermissions() {


        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_FINE_LOCATION

        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                } else {
//                    CommonUtils.showToast("granted");
                    Intent intent = new Intent(TrackStaff.this, GPSTrackerActivity.class);
                    startActivityForResult(intent, 1);
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (permissions[0].equalsIgnoreCase("android.permission.ACCESS_FINE_LOCATION") && grantResults[0] == 0) {
                Intent intent = new Intent(TrackStaff.this, GPSTrackerActivity.class);
                startActivityForResult(intent, 1);

            }
        } catch (ArrayIndexOutOfBoundsException e) {
            getPermissions();
        }

    }

    private void plotSingleMarker(final MyMarker myMarker) {
        // Create user marker with custom icon and other options
//
        Glide.with(TrackStaff.this).asBitmap().load(myMarker.getmIcon()).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                LatLng latLng = new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude());
                Marker marr = myMap.get(myMarker.getPhone());
                if (marr != null) {
                    marr.remove();
                }
                Marker currentMarker = mMap.addMarker(new MarkerOptions()
                        .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
//                                .icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(resource, 120, 120, false)))
                        .icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(TrackStaff.this, resource, myMarker.getmLabel(), myMarker.getmIcon())))
                        .position(latLng));
                myMap.put(myMarker.getPhone(), currentMarker);
                mMarkersHashMap.put(currentMarker, myMarker);
                mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());

            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });


    }

    public Bitmap createCustomMarker(final Context context, Bitmap bitmap12, String name, String imgUrl) {

        final View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);

        final CircleImageView markerImage = marker.findViewById(R.id.user_dp);
        Glide.with(this).load(imgUrl).into(markerImage);

        TextView txt_name = (TextView) marker.findViewById(R.id.name);
        txt_name.setText(name);


        markerImage.setImageBitmap(bitmap12);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(70, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }


    private void setUpMap() {
        // Do a null check to confirm that we have not already instantiated the map.

        // Try to obtain the map from the SupportMapFragment.

        // Check if we were successful in obtaining the map.

        if (mMap != null) {
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    marker.showInfoWindow();
                    return true;
                }
            });
//
//            LatLng newPosition = new LatLng(31.4983326, 74.3400643);
//            mMap.addMarker(new MarkerOptions().position(newPosition).title("Marker in Sydney"));
//
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 13));

        } else
            Toast.makeText(getApplicationContext(), "Unable to create Maps", Toast.LENGTH_SHORT).show();


    }

    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        public MarkerInfoWindowAdapter() {
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View v = getLayoutInflater().inflate(R.layout.infowindow_layout, null);
            final MyMarker myMarker = mMarkersHashMap.get(marker);
            CircleImageView markerIcon = v.findViewById(R.id.marker_icon);
            TextView markerLabel = (TextView) v.findViewById(R.id.marker_label);
            TextView anotherLabel = (TextView) v.findViewById(R.id.another_label);
            Glide.with(TrackStaff.this).load(myMarker.getmIcon()).into(markerIcon);
            markerLabel.setText(myMarker.getmLabel());
            anotherLabel.setText("Phone: " + myMarker.getPhone() +
                    "\nAddress: " + GetAddress.getFullAddress(TrackStaff.this, myMarker.getmLatitude(), myMarker.getmLongitude()));
            return v;
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}