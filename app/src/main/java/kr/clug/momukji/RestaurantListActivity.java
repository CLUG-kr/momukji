package kr.clug.momukji;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class RestaurantListActivity extends AppCompatActivity {
    ListView myList;
    restaurantListAdapter myListAdapter;
    ArrayList<restaurant_item> restaurantItemArrayList;
    Handler handler;
    String restaurantType = "all";
    double myLatitude = -1, myLongitude = -1;
    LocationManager manager;
    GPSListener gpsListener;
    Timer timerGPSLocation = null;
    LoadingTask task = null;
    ProgressDialog asyncDialog = null;
    NetworkTask networkTask = null;
    static public boolean errorGPS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asyncDialog = new ProgressDialog(RestaurantListActivity.this);
        setContentView(R.layout.activity_restaurant_list);
    }

    @Override
    public void onResume(){
        super.onResume();
        networkTask = new NetworkTask("http://server7.dothome.co.kr/list.php?type=" + restaurantType, null);
        networkTask.execute();

        if (startLocationService() == 1) {
            if (errorGPS){
                Toast.makeText(getApplicationContext(), "일시적으로 GPS와 연결되지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            task = new LoadingTask();
            timerGPSLocation = new Timer();
            timerGPSLocation.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (myLatitude == -1) {
                        try {
                            manager.removeUpdates(gpsListener);
                            try { task.cancel(true); } catch (Exception ex) { ex.printStackTrace(); }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        try {
                            asyncDialog.dismiss();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        RestaurantListActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "일시적으로 GPS와 연결되지 않습니다.", Toast.LENGTH_SHORT).show();
                                errorGPS = true;
                            }
                        });
                    }
                }

            }, (long) (3000));
            task.execute();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        try { timerGPSLocation.cancel(); } catch(Exception ex) { ex.printStackTrace(); }
        try { task.cancel(true); } catch(Exception ex) { ex.printStackTrace(); }
        try { networkTask.cancel(true); } catch(Exception ex) { ex.printStackTrace(); }
        try { asyncDialog.dismiss();} catch(Exception ex) { ex.printStackTrace(); }
        try { manager.removeUpdates(gpsListener); } catch(Exception ex) { ex.printStackTrace(); }
    }

    int loResult = 1;
    private int startLocationService() {
        loResult = 1;
        gpsListener = new GPSListener();
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
        }
        else {
            myLatitude = -1;
            myLongitude = -1;
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
            loResult = 0;
            gsDialog.setMessage("원활한 사용을 위해 GPS가 필요합니다. 위치 서비스 기능을 설정하시겠습니까?");
            gsDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                }
            }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            }).create().show();
        }
        return loResult;
    }

    private class LoadingTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            asyncDialog.setCancelable(false);
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로딩 중입니다..");
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                while (!this.isCancelled()) {
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                asyncDialog.dismiss();
                e.printStackTrace();
            }
            asyncDialog.dismiss();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    private class GPSListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            myLatitude = location.getLatitude();
            myLongitude = location.getLongitude();
            restaurant_item.setMyLatitude(myLatitude);
            restaurant_item.setMyLongitude(myLongitude);
            try {
                myListAdapter.notifyDataSetChanged();
            }catch (Exception ex) {
                ;ex.printStackTrace();
            }
            try { timerGPSLocation.cancel(); } catch(Exception ex) { ex.printStackTrace(); }
            try { task.cancel(true); } catch(Exception ex) { ex.printStackTrace(); }
            try { asyncDialog.dismiss();} catch(Exception ex) { ex.printStackTrace(); }
            try { manager.removeUpdates(this); } catch(Exception ex) { ex.printStackTrace(); }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
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

            restaurantItemArrayList = new ArrayList<restaurant_item>();
            try {
                JSONArray jsonArray = new JSONArray(strjson);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    restaurantItemArrayList.add(new restaurant_item(Integer.parseInt(jsonObject.getString("id")), R.mipmap.ic_launcher,
                            jsonObject.getString("name"), Float.parseFloat(jsonObject.getString("star")),
                            Double.parseDouble(jsonObject.getString("latitude")),Double.parseDouble(jsonObject.getString("longitude"))));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            myList = (ListView)findViewById(R.id.mylist);
            myListAdapter = new restaurantListAdapter(getApplicationContext(),restaurantItemArrayList);
            myList.setAdapter(myListAdapter);
            myList.setFocusable(false);
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    Toast.makeText(getApplicationContext(), restaurantItemArrayList.get(pos).getTitle() + "를 선택했습니다.",Toast.LENGTH_LONG).show();
                    Intent newActivity = new Intent(getApplicationContext(), RestaurantInfo.class);
                    newActivity.putExtra("uniqueid",restaurantItemArrayList.get(pos).getUniqueId());
                    startActivity(newActivity);
                    }
            }
            );
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
                    // int resCode = conn.getResponseCode();
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
