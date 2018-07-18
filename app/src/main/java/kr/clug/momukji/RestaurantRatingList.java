package kr.clug.momukji;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class RestaurantRatingList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_rating_list);
        Toast.makeText(getApplicationContext(),"asdf",Toast.LENGTH_LONG).show();
        ListView listview;
        RestaurantListViewAdapter adapter;
        ArrayList<RestaurantRatingListItem> restaurantRatingListItems = new ArrayList<RestaurantRatingListItem>();


        listview = (ListView) findViewById(R.id.restaurantRatingListView);
        adapter = new RestaurantListViewAdapter(getApplicationContext(), restaurantRatingListItems);
        listview.setAdapter(adapter);
        listview.setFocusable(false);
        restaurantRatingListItems.add(new RestaurantRatingListItem(3.5,"2018-07-18","정말 맛있지만 가격이 좀 비싸요"));
        restaurantRatingListItems.add(new RestaurantRatingListItem(5,"2018-07-17","최고에요"));
        restaurantRatingListItems.add(new RestaurantRatingListItem(2, "2018-07-16", "맛은 둘째치고 직원이 정말 불친절하네요 그렇죠? 그렇죠? 제말맞죠? 에베베베베베베베 빼애애애애애애액"));
        restaurantRatingListItems.add(new RestaurantRatingListItem(4.5,"2018-07-14","굿굿"));

    }
}
