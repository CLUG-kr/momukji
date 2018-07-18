package kr.clug.momukji;


import android.content.ContentValues;
import android.content.Intent;
import android.app.FragmentManager;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;


public class RestaurantInfo extends AppCompatActivity implements OnMapReadyCallback {
    private int uniqueid;
    private GoogleMap mMap;
    private NetworkTask networkTask;
    private double serverLatitude, serverLongitude;
    FragmentManager fragmentManager;
    ActionBar ab;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);
        ab  = getSupportActionBar();

        Intent getData = getIntent();
        uniqueid = getData.getIntExtra("uniqueid",0);

        TextView x = (TextView)findViewById(R.id.restName);
        x.setText(String.valueOf(uniqueid));

    }

    @Override
    public void onResume() {
        super.onResume();
        networkTask = new NetworkTask("http://server7.dothome.co.kr/info.php?id=" + Integer.toString(uniqueid), null);
        networkTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_action, menu);
        this.menu = menu;
        TinyDB tinydb = new TinyDB(RestaurantInfo.this);
        ArrayList<Integer> arrayList = tinydb.getListInt("favorite");
        if (arrayList.contains(uniqueid)) {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this,getResources().getIdentifier("btn_star_big_on", "drawable", "android")));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TinyDB tinydb = new TinyDB(RestaurantInfo.this);

        switch (item.getItemId()) {
            case R.id.action_search :
                //Toast.makeText(RestaurantInfo.this, "버튼을 눌렀습니다", Toast.LENGTH_LONG).show();
                ArrayList<Integer> arrayList = tinydb.getListInt("favorite");
                if (arrayList.contains(uniqueid)) {
                    arrayList.remove((Integer)uniqueid);
                    tinydb.putListInt("favorite", arrayList);
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(this,getResources().getIdentifier("btn_star_big_off", "drawable", "android")));
                    Toast.makeText(RestaurantInfo.this, "즐겨찾기에서 제거했습니다", Toast.LENGTH_SHORT).show();
                } else {
                    arrayList.add(uniqueid);
                    tinydb.putListInt("favorite", arrayList);
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(this,getResources().getIdentifier("btn_star_big_on", "drawable", "android")));
                    Toast.makeText(RestaurantInfo.this, "즐겨찾기에 추가했습니다", Toast.LENGTH_SHORT).show();
                }

                return true;
            default :
                return super.onOptionsItemSelected(item) ;
        }
    }



    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(serverLatitude, serverLongitude)));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
        map.animateCamera(zoom);

        MarkerOptions marker = new MarkerOptions();
        marker.position(new LatLng(serverLatitude, serverLongitude)).title(((TextView)findViewById(R.id.restName)).getText().toString());
        map.addMarker(marker).showInfoWindow();
    }

    public void onClick_Review(View v){
        Intent review = new Intent(getApplicationContext(), RestaurantRatingList.class);
        review.putExtra("uniqueid", uniqueid);
        startActivity(review);
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {
        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = request(url);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String strjson = s;
            if (s == null) {
                Toast.makeText(getApplicationContext(), "인터넷에 연결되지 않습니다.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            try {
                JSONArray jsonArray = new JSONArray(strjson);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                ((TextView)findViewById(R.id.restName)).setText(jsonObject.getString("name"));
                ab.setTitle(jsonObject.getString("name"));
                ((TextView)findViewById(R.id.restAddress)).setText(jsonObject.getString("address"));
                ((TextView)findViewById(R.id.restDescription)).setText(jsonObject.getString("description")+"\n"+jsonObject.getString("tag"));
                ((TextView)findViewById(R.id.restNumber)).setText(jsonObject.getString("phone"));
                ((TextView)findViewById(R.id.restOpenhours)).setText(jsonObject.getString("time"));
                ((TextView)findViewById(R.id.restMenu)).setText(jsonObject.getString("menu"));
                ((RatingBar)findViewById(R.id.ratingBar)).setRating(Float.parseFloat(jsonObject.getString("star")));
                serverLatitude = Double.parseDouble(jsonObject.getString("latitude"));
                serverLongitude = Double.parseDouble(jsonObject.getString("longitude"));

                fragmentManager = getFragmentManager();
                MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.map_6P);
                mapFragment.getMapAsync(RestaurantInfo.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String request(String urlStr){
        StringBuilder output = new StringBuilder();
        HttpURLConnection conn = null;

        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (conn != null) {
            conn.setConnectTimeout(10000);
            try {
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                try {
                    InputStream is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String line = null;
                    while(true) {
                        line = reader.readLine();
                        if(line==null) {
                            break;
                        }
                        output.append(line+'\n');
                    }
                    reader.close();
                    conn.disconnect();
                    return output.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



}
