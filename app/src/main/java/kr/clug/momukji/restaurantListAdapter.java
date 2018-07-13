package kr.clug.momukji;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import java.util.ArrayList;

public class restaurantListAdapter extends BaseAdapter{
    Context context;
    ArrayList<restaurant_item> restaurantItemArrayList;
    ViewHolder viewHolder;

    class ViewHolder{
        ImageView profileImage;
        TextView titleText;
        RatingBar restaurantRating;
        TextView distanceText;
    }

    public restaurantListAdapter(Context context, ArrayList<restaurant_item> restaurantItemArrayList) {
        this.context = context;
        this.restaurantItemArrayList = restaurantItemArrayList;
    }

    @Override
    public int getCount() {
        return this.restaurantItemArrayList.size();
    }

    @Override
    public Object getItem(int pos) {
        return this.restaurantItemArrayList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.restaurant_item,null);
            viewHolder = new ViewHolder();
            viewHolder.profileImage = (ImageView)convertView.findViewById(R.id.profileImage);
            viewHolder.titleText = (TextView)convertView.findViewById(R.id.titleText);
            viewHolder.restaurantRating = (RatingBar)convertView.findViewById(R.id.restaurantRating);
            viewHolder.distanceText = (TextView)convertView.findViewById(R.id.distanceText);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.profileImage.setImageResource(restaurantItemArrayList.get(pos).getProfile());
        viewHolder.titleText.setText(restaurantItemArrayList.get(pos).getTitle());
        viewHolder.restaurantRating.setRating(restaurantItemArrayList.get(pos).getStarRating());

        if (restaurantItemArrayList.get(pos).getMyLatitude() == -1 || restaurantItemArrayList.get(pos).getLatitude() == -1) {
            viewHolder.distanceText.setText("정보 없음");
        }
        else {
            Location location = new Location("start");
            float[] result = new float[3];
            location.distanceBetween(
                    restaurantItemArrayList.get(pos).getMyLatitude(),
                    restaurantItemArrayList.get(pos).getMyLongitude(),
                    restaurantItemArrayList.get(pos).getLatitude(),
                    restaurantItemArrayList.get(pos).getLongitude(),
                    result
            );
            viewHolder.distanceText.setText(Integer.toString((int) result[0]) + "m");
        }
        return convertView;
    }
}
