package kr.clug.momukji;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class RestaurantRatingList extends AppCompatActivity {

    private int uniqueid;
    private NetworkTask networkTask, networkTask2;
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
        networkTask = new NetworkTask("http://server7.dothome.co.kr/review.php?id=" + Integer.toString(uniqueid), "GET", null);
        networkTask.execute();
    }


    public void onClick_Write(View v){
        AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
        gsDialog.setTitle("안내");
        gsDialog.setMessage("리뷰와 별점을 등록하면\n수정/삭제할 수 없습니다.\n정말 등록하시겠습니까?");
        gsDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                networkTask2 = new NetworkTask("http://server7.dothome.co.kr/review.php?mode=write&id=" + Integer.toString(uniqueid), "POST",
                        "context=" + ((EditText)findViewById(R.id.reviewWrite)).getText().toString() +
                                "&star=" + Float.toString(((RatingBar)findViewById(R.id.restRatingApply)).getRating()));
                networkTask2.execute();
            }
        }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        }).create().show();
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {
        private String url;
        private String method;
        private String data;

        public NetworkTask(String url, String method, String data) {
            this.url = url;
            this.method = method;
            this.data = data;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = request(url, method, data);
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

            if (data != null) {
                Toast.makeText(RestaurantRatingList.this,"리뷰를 등록했습니다.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RestaurantRatingList.this, RestaurantRatingList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
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

    private String request(String urlStr, String method, String data){
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
                conn.setRequestMethod(method);
                conn.setDoInput(true);
                try {
                    if (data != null) {
                        try {
                            conn.setDoOutput(true);
                            OutputStream os = conn.getOutputStream();
                            os.write(data.getBytes("UTF-8"));
                            os.flush();
                            os.close();
                        } catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
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
