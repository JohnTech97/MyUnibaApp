package sms.myunibapp.unibaServices;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.myunibapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LuoghiDiInteresse extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luoghi_di_interesse);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        LatLng mensa = new LatLng(41.110781, 16.883308);
        mMap.addMarker(new MarkerOptions().position(mensa).title("Mensa Universitaria CUM"));

        LatLng parco = new LatLng(41.107096, 16.874362);
        mMap.addMarker(new MarkerOptions().position(parco).title("Luogo di svago"));

        LatLng campus = new LatLng(41.108698, 16.883900);
        mMap.addMarker(new MarkerOptions().position(campus).title("Campus Universitario"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(campus, 18));
    }
}