package project.mssd2.mssd;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Path;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import mssd.com.project.DirectionFinder;
import mssd.com.project.DirectionFinderListener;
import mssd.com.project.Route;
import mssd.com.project.UserHelper;

public class MapsActivity extends FragmentActivity implements DirectionFinderListener {
    //google map
    GoogleMap mMap;
    Button requestwork, search;

    Spinner district;
    private Double Latitude = 0.00;
    private Double Longitude = 0.00;
    ArrayList<HashMap<String, String>> location, districtarray;
    UserHelper user;
    LatLng latLng;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    TextView txtdistance;
    MarkerOptions marker1,marker2;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        user = new UserHelper();
        district = (Spinner) findViewById(R.id.districtspinner);
        search = (Button) findViewById(R.id.btnsearch);
        txtdistance = (TextView)findViewById(R.id.colway);
        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchlocation();
            }
        });
        requestwork = (Button)findViewById(R.id.btnsaverequest);
        requestwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(),output.class);
                startActivity(it);
            }
        });
        googlemapprocess();
        selectdistrict();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void googlemapprocess() {
        mMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setBuildingsEnabled(true);

        //   mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
   /*     String url = "http://192.168.1.102/mobile/get_location_District.ashx";
        try {
            JSONArray data = new JSONArray(user.getHttpGet(url));
            location = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;
            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<String, String>();

                map.put("lat", c.getString("lat"));
                map.put("long", c.getString("long"));
                map.put("location_name", c.getString("location_name"));
                location.add(map);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        Latitude = Double.parseDouble(location.get(0).get("lat").toString());
        Longitude = Double.parseDouble(location.get(0).get("long").toString());
        LatLng coordinate = new LatLng(Latitude, Longitude);
        mMap.setMapType(com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 17));
        for (int i = 0; i < location.size(); i++) {
            Latitude = Double.parseDouble(location.get(i).get("lat").toString());
            Longitude = Double.parseDouble(location.get(i).get("long").toString());
            String name = location.get(i).get("location_name").toString();
            MarkerOptions marker = new MarkerOptions().position(new LatLng(Latitude, Longitude)).title(name);
            mMap.addMarker(marker);
        }
*/

    }
private void searchlocation(){
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
            .build();
    try {
        Intent intent1 = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(typeFilter).build(this);
        startActivityForResult(intent1, PLACE_AUTOCOMPLETE_REQUEST_CODE);
    } catch (GooglePlayServicesRepairableException e) {
        // TODO: Handle the error.
        System.out.println(e.getMessage());
    } catch (GooglePlayServicesNotAvailableException e) {
        // TODO: Handle the error.
    }
}
    private void selectdistrict(){
        String url = "http://10.10.92.91/mobile/District.ashx";
        try {
            JSONArray data = new JSONArray(user.getHttpGet(url));

            districtarray = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;
            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);
                map = new HashMap<String, String>();
                map.put("district_name", c.getString("district_name"));
                districtarray.add(map);
            }

            SimpleAdapter sAdap;
            sAdap = new SimpleAdapter(MapsActivity.this, districtarray, R.layout.districtlist,
                    new String[] {"district_name"}, new int[] { R.id.ColName});
            district.setAdapter(sAdap);
district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String search =   districtarray.get(position).get("district_name").toString();

        resultcalway();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
});
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                search.setText(place.getName());
              new GeocoderTask().execute(search.getText().toString());

                Log.i("TAG", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i("TAG", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.

            }
        }
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.","Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();
        int districtnumber = district.getSelectedItemPosition();
        String search =   districtarray.get(districtnumber).get("district_name").toString();
        String url = "http://10.10.92.91/mobile/get_location_District.ashx";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("search", search));
        //  Toast.makeText(MapsActivity.this, sName, Toast.LENGTH_SHORT).show();
        String resultServer  =  user.getHttpPost(url,params);

        try {
            JSONArray data = new JSONArray(resultServer);
            location = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;
            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<String, String>();

                map.put("lat", c.getString("lat"));
                map.put("long", c.getString("long"));
                map.put("location_name", c.getString("location_name"));
                location.add(map);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        Latitude = Double.parseDouble(location.get(0).get("lat").toString());
        Longitude = Double.parseDouble(location.get(0).get("long").toString());
        LatLng coordinate = new LatLng(Latitude, Longitude);
        mMap.setMapType(com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 17));

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));

            txtdistance.setText(route.distance.text);
            originMarkers.add(mMap.addMarker(new MarkerOptions()

                    .title(route.startAddress)
                    .position(route.startLocation)));


            for (int i = 0; i < location.size(); i++) {
                Latitude = Double.parseDouble(location.get(i).get("lat").toString());
                Longitude = Double.parseDouble(location.get(i).get("long").toString());
                String name = location.get(i).get("location_name").toString();
                route.endLocationdistrict = new LatLng(Latitude, Longitude);
                MarkerOptions marker = new MarkerOptions().position(route.endLocationdistrict).title(name);
                destinationMarkers.add(mMap.addMarker(marker));
                //  destinationMarkers.add(mMap.addMarker(marker));
            }
        /*    destinationMarkers.add(mMap.addMarker(new MarkerOptions()

                    .title(route.endAddress)
                    .position(route.endLocation))); */
            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
private void resultcalway(){
    String origin = search.getText().toString();
    int districtnumber = district.getSelectedItemPosition();
    String destination = districtarray.get(districtnumber).get("district_name");
    if (origin.isEmpty()) {
        Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
        return;
    }
    if (destination.isEmpty()) {
        Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
        return;
    }

    try {
        new DirectionFinder(this, origin, destination).execute();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }

}
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {


        protected List<Address> doInBackground(String... locationName) {
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;

            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }
        protected void onPostExecute(List<Address> addresses) {

            if(addresses==null || addresses.size()==0){
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }

            // Clears all the existing markers on the map
            //mMap.clear();



            // Adding Markers on Google Map for each matching address
            for(int i=0;i<addresses.size();i++){

                Address address = (Address) addresses.get(i);

                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address.getLatitude(), address.getLongitude());

                String addressText = String.format("%s, %s", address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "", address.getCountryName());




                originMarkers.add(mMap.addMarker(new MarkerOptions().position(latLng)
                        .title(addressText).icon(BitmapDescriptorFactory.fromResource(R.drawable.selectlocation))));
                // Locate the first location
                if(i==0)
                {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                }
            }
        }

    }

}