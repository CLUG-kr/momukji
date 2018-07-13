package kr.clug.momukji;

import android.content.Intent;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RestaurantInfo extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);

        Intent getData = getIntent();
        int uniqueid = getData.getIntExtra("uniqueid",0);

        TextView x = (TextView)findViewById(R.id.restName);
        x.setText(String.valueOf(uniqueid));

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.map_6P);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap map) {

        mMap = map;

        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.503148, 126.957)));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        map.animateCamera(zoom);

        MarkerOptions marker = new MarkerOptions();
        marker.position(new LatLng(37.503148, 126.957)).title("6층 팀플실");
        map.addMarker(marker).showInfoWindow();
    }
}
