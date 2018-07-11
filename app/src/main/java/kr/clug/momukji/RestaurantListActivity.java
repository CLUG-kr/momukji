package kr.clug.momukji;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
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

public class RestaurantListActivity extends AppCompatActivity {
    ListView myList;
    restaurantListAdapter myListAdapter;
    ArrayList<restaurant_item> restaurantItemArrayList;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
        NetworkTask networkTask = new NetworkTask("http://server7.dothome.co.kr/test.php", null);
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
            restaurantItemArrayList = new ArrayList<restaurant_item>();

            try {
                JSONArray jsonArray = new JSONArray(strjson);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    restaurantItemArrayList.add(new restaurant_item(jsonObject.getInt("uniqueid"), R.mipmap.ic_launcher,
                            jsonObject.getString("title"),(float) jsonObject.getDouble("starRating"), jsonObject.getInt("distance")));
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
        } catch (IOException e) {
            e.printStackTrace();
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
