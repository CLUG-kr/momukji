package kr.clug.momukji;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;

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

public class RestaurantRatingList extends AppCompatActivity {

    private int uniqueid;
    private NetworkTask networkTask;
    ArrayList<RestaurantRatingListItem> restaurantRatingListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_rating_list);
        Intent getData = getIntent();
        uniqueid = getData.getIntExtra("uniqueid",0);
    }


    @Override
    public void onResume() {
        super.onResume();
        networkTask = new NetworkTask("http://server7.dothome.co.kr/review.php?id=" + Integer.toString(uniqueid), null);
        networkTask.execute();
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

            restaurantRatingListItems = new ArrayList<RestaurantRatingListItem>();
            try {
                JSONArray jsonArray = new JSONArray(strjson);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    restaurantRatingListItems.add(new RestaurantRatingListItem(Float.parseFloat(jsonObject.getString("star")),
                            jsonObject.getString("date"), jsonObject.getString("context")));
                }
                ListView listview;
                RestaurantListViewAdapter adapter;

                listview = (ListView) findViewById(R.id.restaurantRatingListView);
                adapter = new RestaurantListViewAdapter(getApplicationContext(), restaurantRatingListItems);
                listview.setAdapter(adapter);
                listview.setFocusable(false);
            } catch (JSONException e) {
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
