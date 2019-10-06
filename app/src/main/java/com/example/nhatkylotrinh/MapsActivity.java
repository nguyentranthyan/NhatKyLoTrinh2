package com.example.nhatkylotrinh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback{
        //,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

   private GoogleMap mGoogleMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(googleServiceAvailable()){
            Toast.makeText(this,"connect to play services",Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_maps);
            initmap();
           // buildGoogleApiClient();
            //getLocation();
        }else{
            //no googlemap layout
        }
    }

    private void initmap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapfragment);
        mapFragment.getMapAsync(this);
    }
    /**
     * Phương thức kiểm chứng google play services trên thiết bị
     */
    public boolean googleServiceAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isVailable = api.isGooglePlayServicesAvailable(this);
        if (isVailable == ConnectionResult.SUCCESS){
            return true;
        }else if(api.isUserResolvableError(isVailable)){
            Dialog dialog=api.getErrorDialog(this,isVailable,0);
            dialog.show();
        }else{
            Toast.makeText(this,"cant connect to play services",Toast.LENGTH_LONG).show();
        }
        return false;
    }
    //zoom ban do
    private void gotlocationZoom(double lat,double lng,float zoom){
        LatLng latLng=new LatLng ( lat,lng );
        CameraUpdate update=CameraUpdateFactory.newLatLngZoom ( latLng,zoom );
        mGoogleMap.moveCamera ( update );
    }
    //show ban do khi nhan button go theo noi nhap vao edittext
    public void geoLocation(View view) throws IOException {
        EditText et=(EditText)findViewById(R.id.editText);
        String location=et.getText().toString();

        Geocoder gc=new Geocoder(this);
        List<Address> list=gc.getFromLocationName (location,1);
        Address address=list.get (0);
        String locatily=address.getLocality ();
        Toast.makeText ( this,locatily,Toast.LENGTH_LONG ).show ();
        double lat=address.getLatitude ();
        double lng=address.getLongitude ();
        gotlocationZoom ( lat,lng,18 );
    }

    //tao menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate (R.menu.menu,menu);
        return super.onCreateOptionsMenu ( menu );
    }
    //showmenu trinh chieu ban do
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu ( menu, v, menuInfo );
        getMenuInflater ().inflate ( R.menu.menu, menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId ()){
            case R.id.mapTypeNone:
                 mGoogleMap.setMapType (GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.map_type_normal:
                mGoogleMap.setMapType (GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.map_type_terrain:
                mGoogleMap.setMapType (GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.map_type_satellite:
                mGoogleMap.setMapType (GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.map_type_hybird:
                mGoogleMap.setMapType (GoogleMap.MAP_TYPE_HYBRID);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected ( item );
    }
    //hien thi map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        // Add a marker in Sydney and move the camera

        LatLng sydney = new LatLng(10.826566, 106.688707);
        mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("I am here"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(sydney, 17);
        mGoogleMap.animateCamera(cameraUpdate);
    }
//    /**
//     * Tạo đối tượng google api client
//     */
//    protected synchronized void buildGoogleApiClient() {
//        if (mGoogleApiClient == null) {
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi( LocationServices.API).build();
//        }
//    }

//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        // Đã kết nối với google api, lấy vị trí
//        getLocation();
//    }
//    private void getLocation() {
//        if (ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // Kiểm tra quyền hạn
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
//        } else {
//            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//            if (location != null) {
//                latitude = location.getLatitude();
//                longitude = location.getLongitude();
//
//                LatLng sydney = new LatLng(latitude, longitude);
//                mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("I am here"));
//                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(sydney, 17);
//                mGoogleMap.animateCamera(cameraUpdate);
//            }
//        }
//    }
//
//LocationRequest mLocationRequest;
//    @Override
//    public void onConnectionSuspended(int i) {
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Toast.makeText(this, "Lỗi kết nối: " + connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
//    }
//
//    protected void onStart() {
//        mGoogleApiClient.connect();
//        super.onStart();
//    }
//
//    protected void onStop() {
//        mGoogleApiClient.disconnect();
//        super.onStop();
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }
}
