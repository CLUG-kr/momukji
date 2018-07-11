package kr.clug.momukji;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class RestaurantInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);
        Intent getData = getIntent();
        int uniqueid = getData.getIntExtra("uniqueid",0);
        TextView x = (TextView)findViewById(R.id.titleText);
        x.setText(String.valueOf(uniqueid));
    }
}
