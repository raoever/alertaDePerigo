package com.example.alertadeperigo;

import androidx.fragment.app.FragmentActivity;

import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.alertadeperigo.databinding.ActivityMapaBinding;

public class MapaActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener {

    private GoogleMap mMap;
    private ActivityMapaBinding binding;
    private LocationManager locationManager;
    private static final LatLngBounds PALMAS = new LatLngBounds(
            new LatLng(-10.40, -48.5), new LatLng(10.25, 48.323));
    private static final CameraPosition PALMAS_CAMERA = new CameraPosition.Builder()
            .target(new LatLng(-10.32754, -48.29925)).zoom(18.0f).bearing(0).tilt(0).build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        googleMap.setOnCameraIdleListener(this);
        mMap.setLatLngBoundsForCameraTarget(PALMAS);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(PALMAS_CAMERA));
        mMap.addMarker(new MarkerOptions().position(PALMAS_CAMERA.target).title("Gin??sio Ayrton Senna"));
    }

    @Override
    public void onCameraIdle() {

    }
}