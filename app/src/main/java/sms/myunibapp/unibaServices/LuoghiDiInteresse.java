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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng mensa = new LatLng(41.110781, 16.883308);
        mMap.addMarker(new MarkerOptions().position(mensa).title("Mensa Universitaria CUM"));

        LatLng parco = new LatLng(41.107096, 16.874362);
        mMap.addMarker(new MarkerOptions().position(parco).title("Luogo di svago"));

        LatLng campus = new LatLng(41.108698, 16.883900);
        mMap.addMarker(new MarkerOptions().position(campus).title("Campus Universitario"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(campus, 18));
    }
}