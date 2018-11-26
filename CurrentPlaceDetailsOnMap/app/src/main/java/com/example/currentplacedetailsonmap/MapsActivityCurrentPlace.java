package com.example.currentplacedetailsonmap;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;


import java.util.*;

/**
 * An activity that displays a map showing the place at the device's current location.
 */
public class MapsActivityCurrentPlace extends AppCompatActivity
        implements OnMapReadyCallback {

    private static final String TAG = MapsActivityCurrentPlace.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private DatabaseReference mDatabase;
    private List<ParkingSpot> spotslist = new ArrayList<ParkingSpot>();

    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private String[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;

    //Saved Parking Spot
    ParkingSpot mySpot;

    // Buttons
    private Button parking;
    private Button leaving;
    private Button gotoFilter;


    //User
    private User localuser;
    public boolean loggedIn;

    //Intent
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            loggedIn = false;
            intent.putExtra("loggedIn", loggedIn);
        } else {
            if (extras.getBoolean("loggedIn")) {
                loggedIn = true;
            } else {
                loggedIn = false;
            }
        }

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        if (getIntent().getStringExtra("upperBound") != null) {
            Log.d("filtertester", getIntent().getStringExtra("lotType"));
            List<String> lotList = new ArrayList<String>();
            List<String> specList = new ArrayList<String>();


            String lotType = getIntent().getStringExtra("lotType");
            String specialty = getIntent().getStringExtra("specialty");
            if (lotType.equals("Pick One")) {
                lotList.add("Grass");
                lotList.add("UnderGround");
                lotList.add("Garage/Deck");
                lotList.add("Street");
            }
            if (specialty.equals("Pick One")){
                specList.add("Electric");
                specList.add("Handicap");
                specList.add("Motorcycle");
                specList.add("Bus");
            }
            specList.add(specialty);
            lotList.add(lotType);
            String upperBound = getIntent().getStringExtra("upperBound");
            //mMap.clear();
            Log.d("clearFinished", getIntent().getStringExtra("lotType"));
            addMarkersfilter(upperBound,lotList, specList);
            //addMarkerNum(getIntent().getStringExtra("upperBound").toString());
            //addMarkers();
        }

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //
        // test code
        // populateDatabase();


        parking = (Button) findViewById(R.id.parkingButton);
        leaving = (Button) findViewById(R.id.leavingButton);

        parking.setVisibility(View.VISIBLE);
        leaving.setVisibility(View.GONE);

        // these 2 functions flip the visibilty of the buttons and
        parking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaving.setVisibility(View.VISIBLE);
                parking.setVisibility(View.GONE);
                removeCurrentLocationFromDatabase();
            }
        });

        leaving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parking.setVisibility(View.VISIBLE);
                leaving.setVisibility(View.GONE);
                addCurrentLocationToDatabase();
            }
        });

        gotoFilter = (Button) findViewById(R.id.filterPage);
        gotoFilter.setVisibility(View.GONE);

        // for testing purposes, this button appears when the user is logged in
        if (loggedIn) {gotoFilter.setVisibility(View.VISIBLE); }

        gotoFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mMap.clear();
                openMenuPage();

            }
        });
    }
    //test code
    /*
    private void populateDatabase() {
        ParkingSpot spot = new ParkingSpot(true, 37.4276, -122.0845);
        mDatabase.child(spot.getId()).setValue(spot);
        ParkingSpot spot2 = new ParkingSpot(true, 37.4286, -122.0846);
        mDatabase.child(spot2.getId()).setValue(spot2);
        ParkingSpot spot3 = new ParkingSpot(true, 37.4296, -122.0847);
        mDatabase.child(spot3.getId()).setValue(spot3);

    }*/

    public void openMenuPage() {
        //mMap.clear();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        //mMap.clear();
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Sets up the options menu.
     * @param menu The options menu.
     * @return Boolean.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.current_place_menu, menu);
        return true;
    }

    /**
     * Handles a click on the menu option to get a place.
     * @param item The menu item to handle.
     * @return Boolean.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_goto_filter) {
            openMenuPage();
        }
        return true;
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mDatabase = FirebaseDatabase.getInstance().getReference("parking spots");
        mMap = map;

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        //addMarkers();
    }
    private  void addMarkerNum(final String upperBound){
        Query query = FirebaseDatabase.getInstance().getReference("parking spots").orderByChild("isAvailable").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                spotslist.clear();
                //Log.d("valueEventListener", "" + dataSnapshot.exists()
                if (dataSnapshot.exists()) {
                    for (DataSnapshot datasnap : dataSnapshot.getChildren()) {
                        //Log.d("valueEventListener", datasnap.toString());

                        ParkingSpot spot = datasnap.getValue(ParkingSpot.class);
                        String spotCost = spot.getCost();
                        String spotLotType = spot.getLotType();
                        String spotSpecialty = spot.getSpecialty();

                        if (Double.parseDouble(upperBound) >= Double.parseDouble(spotCost))
                            spotslist.add(spot);


                        //Log.d("valueEventListener", spot.getId().toString());

                        Log.d("valueEventListener", spotslist.toString());
                    }
                }
                markerHelper(spotslist);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void addMarkersfilter(final String upper, final List<String> lotList, final List<String> specialtyList) {
        Query query = FirebaseDatabase.getInstance().getReference("parking spots").orderByChild("isAvailable").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                spotslist.clear();
                //Log.d("valueEventListener", "" + dataSnapshot.exists()
                if (dataSnapshot.exists()) {
                    for (DataSnapshot datasnap : dataSnapshot.getChildren()) {
                        //Log.d("valueEventListener", datasnap.toString());

                        ParkingSpot spot = datasnap.getValue(ParkingSpot.class);
                        String spotCost = spot.getCost();

                        String spotLotType = spot.getLotType();
                        String spotSpecialty = spot.getSpecialty();
                        //String upperPerHr = "$" + upper+"/hr";
                        //Log.d("costformat", "" + upperPerHr);

                        if (lotList.contains(spotLotType) && specialtyList.contains(spotSpecialty)) {
                            if(Double.parseDouble(upper)>=Double.parseDouble(spotCost)) {
                                spotslist.add(spot);
                            }

                        }

                        //Log.d("valueEventListener", spot.getId().toString());

                        Log.d("valueEventListener", spotslist.toString());
                    }
                }
                markerHelper(spotslist);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void addMarkers() {
        Query query = FirebaseDatabase.getInstance().getReference("parking spots").orderByChild("isAvailable").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                spotslist.clear();
                //Log.d("valueEventListener", "" + dataSnapshot.exists());

                if (dataSnapshot.exists()) {
                    for (DataSnapshot datasnap : dataSnapshot.getChildren()) {
                        //Log.d("valueEventListener", datasnap.toString());
                        ParkingSpot spot = datasnap.getValue(ParkingSpot.class);


                        spotslist.add(spot);

                        //Log.d("valueEventListener", spot.getId().toString());

                        Log.d("valueEventListener", spotslist.toString());
                    }
                }
                markerHelper(spotslist);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        /*mMap.addMarker(new MarkerOptions()
                .title(getString(R.string.default_info_title))
                .position(spotCheck)
                .snippet(getString(R.string.default_info_snippet)));

        mMap.addMarker(new MarkerOptions()
                .title(getString(R.string.default_info_title))
                .position(dummy2)
                .snippet(getString(R.string.default_info_snippet)));

        mMap.addMarker(new MarkerOptions()
                .title(getString(R.string.default_info_title))
                .position(dummy3)
                .snippet(getString(R.string.default_info_snippet)));*/
    }

    private void markerHelper(List<ParkingSpot> spots) {
        Log.d("MarkerHelper", spots.toString());
        //mMap.clear();
        for (ParkingSpot spot : spots) {
            Log.d("MarkerHelper", spot.toString());
            LatLng data = new LatLng(spot.getLatitude(), spot.getLongitude());
            String stringbuilder = "";
            if(spot.getCost() != null) {
                stringbuilder += "Cost: " + spot.getCost();
            } else {
                stringbuilder += "Cost: N/A";
            }
            if(spot.getLotType() != null) {
                stringbuilder += "\nLot Type: " + spot.getLotType();
            } else {
                stringbuilder += "\nLot Type: N/A";
            }
            if(spot.getSpecialty() != null) {
                stringbuilder += "\nSpecialty: " + spot.getSpecialty();
            } else {
                stringbuilder += "\nSpecialty: N/A";
            }
            mMap.addMarker(new MarkerOptions()
                    .position(data)
                    .snippet(stringbuilder));
        }

    }


    /**
     * Saves User Filters to the database
     * @param user
     */
    private void saveUserFilters(User user) {
        DatabaseReference uDatabase = FirebaseDatabase.getInstance().getReference("users");
        uDatabase.child(user.getUsername()).setValue(user);

    }


    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }

                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    //Adds users current location to the database, called when a user releases their parking spot
    private void addCurrentLocationToDatabase() {


        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();

                            ParkingSpot currentSpot = new ParkingSpot(true, mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                            mDatabase.child(currentSpot.getId()).setValue(currentSpot);
                            mySpot = currentSpot;


                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());

                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    //sets parking spot to false, called when user parks in spot
    private void removeCurrentLocationFromDatabase() {
        if (mySpot != null) {
            mySpot.setisAvailable(false);
            mDatabase.child(mySpot.getId()).setValue(mySpot);
        } else  {
            Log.d(TAG, "mySpot is null, if this was thrown, something went wrong");

        }

    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    private void showCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final
            Task<PlaceLikelihoodBufferResponse> placeResult =
                    mPlaceDetectionClient.getCurrentPlace(null);
            placeResult.addOnCompleteListener
                    (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();

                                // Set the count, handling cases where less than 5 entries are returned.
                                int count;
                                if (likelyPlaces.getCount() < M_MAX_ENTRIES) {
                                    count = likelyPlaces.getCount();
                                } else {
                                    count = M_MAX_ENTRIES;
                                }

                                int i = 0;
                                mLikelyPlaceNames = new String[count];
                                mLikelyPlaceAddresses = new String[count];
                                mLikelyPlaceAttributions = new String[count];
                                mLikelyPlaceLatLngs = new LatLng[count];

                                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                                    // Build a list of likely places to show the user.
                                    mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
                                    mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace()
                                            .getAddress();
                                    mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
                                            .getAttributions();
                                    mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                                    i++;
                                    if (i > (count - 1)) {
                                        break;
                                    }
                                }

                                // Release the place likelihood buffer, to avoid memory leaks.
                                likelyPlaces.release();

                                // Show a dialog offering the user the list of likely places, and add a
                                // marker at the selected place.
                                openPlacesDialog();

                            } else {
                                Log.e(TAG, "Exception: %s", task.getException());
                            }
                        }
                    });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(mDefaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The "which" argument contains the position of the selected item.
                LatLng markerLatLng = mLikelyPlaceLatLngs[which];
                String markerSnippet = mLikelyPlaceAddresses[which];
                if (mLikelyPlaceAttributions[which] != null) {
                    markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[which];
                }

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                mMap.addMarker(new MarkerOptions()
                        .title(mLikelyPlaceNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet));

                // Position the map's camera at the location of the marker.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        DEFAULT_ZOOM));
            }
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.pick_place)
                .setItems(mLikelyPlaceNames, listener)
                .show();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}
