package kr.clug.momukji;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class testAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void listButtonClick(View v){
        Intent x = new Intent(getApplicationContext(), RestaurantListActivity.class);
        startActivity(x);
    }
}
