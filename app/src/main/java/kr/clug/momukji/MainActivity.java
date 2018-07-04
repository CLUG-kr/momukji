package kr.clug.momukji;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButton1Clicked(View v){
        EditText edittext=(EditText)findViewById(R.id.editText123);
        Toast.makeText(getApplicationContext(), edittext.getText() + "을(를) 검색합니다.", Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.search.naver.com/search.naver?query=" + edittext.getText()));
        startActivity(myIntent);
    }
}