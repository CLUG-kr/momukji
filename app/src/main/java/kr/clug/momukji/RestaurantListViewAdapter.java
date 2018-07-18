package kr.clug.momukji;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RestaurantListViewAdapter extends BaseAdapter{
    Context context;
    private ArrayList<RestaurantRatingListItem> listViewItemList;
    ViewHolder viewHolder;

    class ViewHolder{
        RatingBar rate;
        TextView dateText;
        TextView context;
    }

    public RestaurantListViewAdapter(Context context, ArrayList<RestaurantRatingListItem> restaurantRatingListItemArrayList) {
        this.context = context;
        this.listViewItemList = restaurantRatingListItemArrayList;
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_restaurant_rating_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.rate = (RatingBar)convertView.findViewById(R.id.restaurantRating);
            viewHolder.dateText = (TextView)convertView.findViewById(R.id.restRatingDate);
            viewHolder.context = (TextView)convertView.findViewById(R.id.restRatingText);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.rate.setRating((float)listViewItemList.get(position).getRestRating());
        viewHolder.dateText.setText(listViewItemList.get(position).getRestRatingDate());
        viewHolder.context.setText(listViewItemList.get(position).getRestRatingText());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }


}