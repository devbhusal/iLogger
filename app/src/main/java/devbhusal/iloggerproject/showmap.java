package devbhusal.iloggerproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.os.Handler;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import android.os.AsyncTask;
//import java.util.logging.Handler;


public class showmap extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
       // GoogleMap.OnMarkerClickListener,
        LocationListener {
    public static final String TAG = showmap.class.getSimpleName();
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public TextView selectedmarker;
    public Boolean isitfromcache;
    public Double markerlat, markerlng;
    // public  ProgressBar linlaHeaderProgress;
    public TextView plzwait;
    ArrayList<Double> arrLat;
    ArrayList<Double> arrLng;
    ArrayList<String> arrmname;
    String markertodelete;
    AlertDialog alert;
    Double marketrodelete_lat,markertodelete_lng;
    //ProgressDialog pDialog ;
    ProgressDialog ringProgressDialog;
    public int whichposition;
    public boolean alldone;
    private Handler ll = new Handler();
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showmap);
        arrLat = new ArrayList<Double>();
        arrLng = new ArrayList<Double>();
        arrmname = new ArrayList<String>();
        alldone = false;
        //linlaHeaderProgress=(ProgressBar)findViewById(R.id.pbHeaderProgress);
        //linlaHeaderProgress.setVisibility(View.VISIBLE);
       // plzwait = (TextView) findViewById(R.id.plzwt);
       // plzwait.setVisibility(View.VISIBLE);
        selectedmarker = (TextView) findViewById(R.id.selectedlocation);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please wait. Loading..\n -Google Map \n -Current Location \n -Saved Locations")
                .setCancelable(false)



        ;
         alert = builder.create();
        alert.show();



        setUpMapIfNeeded();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        try {

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(Marker marker) {
                    // if(arg0.getTitle().equals("MyHome")) // if marker source is clicked
                    //  Toast.makeText(showmap.this, "DONE" , Toast.LENGTH_SHORT).show();
                    selectedmarker.setText(marker.getTitle());
                    markerlat = marker.getPosition().latitude;
                    markerlng = marker.getPosition().latitude;
                    marker.showInfoWindow();
                    //marker.

                    return true;
                }

            });
        } catch (Exception e) {

        }


    }


    public int showoldlocation(View v) {
        String s1 = "You are here";
        String s2 = "Selected location";
        if (selectedmarker.getText().toString().equals(s1) || selectedmarker.getText().toString().equals(s2)) {
            Toast.makeText(this, "First select previously saved location", Toast.LENGTH_SHORT).show();
            return 0;
        }
        Intent yy = new Intent(this, viewoldmarker.class);
        yy.putExtra("marker", selectedmarker.getText().toString());
        yy.putExtra("lat", markerlat);
        yy.putExtra("lng", markerlng);
        startActivity(yy);
        return 1;
    }


    public boolean checkifalreadymarked() {
        Double L1, L2, D;
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        L1 = location.getLatitude();
        L2 = location.getLongitude();

        for (int i = 0; i < arrLat.size(); i++) {
            // if (L1.equals(arrLat.get(i)) && L2.equals(arrLng.get(i)) ){
            D = Distancebetn(L1, L2, arrLat.get(i), arrLng.get(i));
            if (D < 0.02) {
                markertodelete = arrmname.get(i);
                marketrodelete_lat=arrLat.get(i);
                markertodelete_lng=arrLng.get(i);
                whichposition = i;
                return true;

            }
        }
        return false;
    }

    /*
        public Double Distancebetn(Double lat1,Double lng1,Double lat2,Double lng2){
         Double d;
            d=Math.pow((lat1-lat2),2)+ Math.pow((lng1-lng2),2);
            d=Math.sqrt(d);



            return d;

        }

    */
//using haversine
    public Double Distancebetn(Double lat1, Double lng1, Double lat2, Double lng2) {
        Double R = 6371.0; // Radius of the earth in km
        Double dLat = deg2rad(lat2 - lat1);  // deg2rad below
        Double dLon = deg2rad(lng2 - lng1);
        Double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                                Math.sin(dLon / 2) * Math.sin(dLon / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        Double d = R * c; // Distance in km
        return d;
    }

    public Double deg2rad(Double deg) {
        return deg * (Math.PI / 180);
    }


    public int startnewlocation(View v) {
        //boolean a;
        if (checkifalreadymarked()) {
            Toast.makeText(this, "Location already saved", Toast.LENGTH_SHORT).show();

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Location within 20m radius from current point was already saved. Do you want to Delete it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            deletemarker();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    })
                   .setNeutralButton("View old data", new DialogInterface.OnClickListener() {
                       public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                           Intent yy = new Intent(showmap.this, viewoldmarker.class);
                           yy.putExtra("marker", markertodelete);
                           yy.putExtra("lat", marketrodelete_lat);
                           yy.putExtra("lng", markertodelete_lng);
                           startActivity(yy);
                       }
                   })

            ;
            final AlertDialog alert = builder.create();
            alert.show();

            return 0;

        }


        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Intent xx = new Intent(this, addnewmarker.class);
        xx.putExtra("lat", location.getLatitude());
        xx.putExtra("lng", location.getLongitude());
        xx.putExtra("place", getplace(location.getLatitude(), location.getLongitude()));
        File dir = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/temp");
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }


        startActivity(xx);
        return 1;
    }

    public void deletemarker() {


        //delte folder
        selectedmarker.setText("Selected location");
        String markertosave, temp;
        markertosave = "";
        File dir = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/" + markertodelete);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }
        dir.delete();
        //arrLat.remove(whichposition);
        //arrLng.remove(whichposition);
        // arrmname.remove(whichposition);

        //delte from marker.txt
        //  File filetmp = new File(Environment.getExternalStorageDirectory()+"/ilogger_data/temp00125xxcb.txt" );
        File file = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/All_markers.txt");

        Scanner in = null;
        try {
            in = new Scanner(new FileReader(file));
            while (in.hasNextLine()) {
                temp = in.nextLine();
                if (!markertodelete.equals(temp)) {
                    markertosave = markertosave + temp + "\n";

                }
                ;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Exception e) { /* ignore */ }
        }
        file.delete();
//rewrite marker
        try {


            FileWriter buf = new FileWriter(file);


            buf.write(markertosave);

            buf.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

        // mMap.clear();
        handleNewLocation();

        Toast.makeText(this, markertodelete + " has been deleted. You can now add this location", Toast.LENGTH_SHORT).show();


        //delete from marker setup
        //delte from array
    }

    @Override
    protected void onResume() {
        super.onResume();

        // mMap.clear();
        // linlaHeaderProgress.setVisibility(View.VISIBLE);
       // plzwait.setVisibility(View.VISIBLE);
        alert.show();
        setUpMapIfNeeded();
        //new Thread(new Runnable() {
        //    public void run(){

        mGoogleApiClient.connect();
        // }
        //  }).start();


    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.

        alldone = false;
        //    new Thread(new Runnable() {


        //      public void run() {

        //          ll.post (new Runnable() {
        //             public void run() {
        //View.setImageBitmap(bitmap);
        //                 while(true){
        //                  if(alldone) linlaHeaderProgress.setVisibility(View.INVISIBLE);
        //                  if(!alldone) linlaHeaderProgress.setVisibility(View.VISIBLE);
        //  }
        //             }
        //         });

        //    }


        //   }).start();

        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();


        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    // private void setUpMap() {
    //     mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    //  }
    @Override
    public void onConnected(Bundle bundle) {
        //startDialog();
        if (didwegetcurrentlocation()) handleNewLocation();


    }


    public boolean didwegetcurrentlocation() {

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        while (location == null) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        }
        return true;
    }


    /* @Override
      public boolean onMarkerClick(Marker marker) {
          Log.i("GoogleMapActivity", "onMarkerClick");
          Toast.makeText(getApplicationContext(),
                  "Marker Clicked: " + marker.getTitle(), Toast.LENGTH_LONG)
                  .show();
          return false;
      }
  */
    private void handleNewLocation() {
        mMap.clear();
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();


        mark_old_location();
        markONlocation(currentLatitude, currentLongitude, "You are here", BitmapDescriptorFactory.HUE_RED);
        //markONlocation(-37.720712,  145.048397, "Latrobe University", BitmapDescriptorFactory.HUE_CYAN);

        String gplace = getplace(currentLatitude, currentLongitude);
        TextView tt = (TextView) findViewById(R.id.textView2);
        tt.setText(gplace);
        if (isitfromcache) {
            tt.setTextColor(Color.BLUE);
        } else {
            tt.setTextColor(Color.BLACK);
        }

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
        //plzwait.setVisibility(View.INVISIBLE);
        alldone = true;
        alert.hide();

    }


    public void mark_old_location() {
        //read all marker name
        File file = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/All_markers.txt");
        arrLat.clear();
        arrmname.clear();
        arrLng.clear();
        Scanner in = null;
        try {
            in = new Scanner(new FileReader(file));
            while (in.hasNextLine()) {

                marklocationfromfile(in.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Exception e) { /* ignore */ }
        }


    }

    public void marklocationfromfile(String filename) {
        File file = new File(Environment.getExternalStorageDirectory() + "/ilogger_data/" + filename + "/location.txt");
        String lat, lng;
        Double L1, L2;
        Scanner in = null;
        try {
            in = new Scanner(new FileReader(file));

            lat = in.nextLine();
            lng = in.nextLine();
            L1 = Double.parseDouble(lat);
            L2 = Double.parseDouble(lng);
            markONlocation(L1, L2, filename, BitmapDescriptorFactory.HUE_AZURE);
            arrLat.add(L1);
            arrLng.add(L2);
            arrmname.add(filename);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Exception e) { /* ignore */ }
        }


    }


    public void markONlocation(double lat, double lng, String title, float img) {

        LatLng latLng = new LatLng(lat, lng);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker(img));
        mMap.addMarker(options);
        //Marker=options;

    }

    public String getplace(double lat, double lng) {

        String str = "Info not found", place;
        place = str;
        String temp_lat, temp_lng, temp_add1, temp_add2, temp_add3, temp_add4;
        Double temp_lat_d, temp_lng_d, D;
        //double lat = location.getLatitude();
        // double lng = location.getLongitude();
        //check in cachefile first
        File cache = getExternalCacheDir();
        File file = new File(cache.getAbsolutePath(), "gplace111.txt");
        Scanner in = null;
        try {
            in = new Scanner(new FileReader(file));
            while (in.hasNextLine()) {
                temp_lat = in.nextLine();
                temp_lng = in.nextLine();
                temp_add1 = in.nextLine();
                temp_add2 = in.nextLine();
                temp_add3 = in.nextLine();
                temp_add4 = in.nextLine();
                temp_lat_d = Double.parseDouble(temp_lat);
                temp_lng_d = Double.parseDouble(temp_lng);
                D = Distancebetn(lat, lng, temp_lat_d, temp_lng_d);
                if (D <= 0.05) {
                    place = temp_add1 + "\n" + temp_add2 + "\n" + temp_add3 + "\n" + temp_add4;
                    isitfromcache = true;
                    return place;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Exception e) { /* ignore */ }
        }


        Geocoder gc = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = gc.getFromLocation(lat, lng, 1);
            StringBuilder sb = new StringBuilder();
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                //for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                sb.append(address.getAddressLine(0)).append("\n");
                sb.append(address.getLocality()).append("\n");
                sb.append(address.getPostalCode()).append("\n");
                sb.append(address.getCountryName());
                place = sb.toString();


            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //info save to cache

        if (place.equals(str)) return place;
        try {


            FileWriter buf = new FileWriter(file, true);
            buf.append(Double.toString(lat) + "\n");
            buf.append(Double.toString(lng) + "\n");

            buf.append(place + "\n");

            buf.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }


        isitfromcache = false;
        return place;
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}