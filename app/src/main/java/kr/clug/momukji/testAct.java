package kr.clug.momukji;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class testAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("대학맛집사전");
    }

    public void listButtonClick(View v){
        Intent x = new Intent(getApplicationContext(), RestaurantListActivity.class);
        x.putExtra("type","first");
        startActivity(x);
    }

    public void onRecommend(View v){
        Intent x = new Intent(getApplicationContext(), RestaurantListActivity.class);
        x.putExtra("type","recommend");
        startActivity(x);
    }

    public void onClick_Favorite(View v) {
        Intent x = new Intent(getApplicationContext(), RestaurantListActivity.class);
        x.putExtra("type","favorite");
        startActivity(x);
    }
}
