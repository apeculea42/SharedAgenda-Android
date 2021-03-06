package com.myherobots.sharedagenda;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ale on 23/01/2018.
 */

public class CustomAdapter extends ArrayAdapter<Users> {

    private Context mContext;
    private List<Users> mItems;

    public CustomAdapter(Context context, List<Users> items) {
        super(context, R.layout.list_users);
        mContext = context;
        mItems = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.list_users, null, false);

        ImageView photo = view.findViewById(R.id.profile_image_user);
        Picasso.with(mContext).load(mItems.get(position).getUrl()).into(photo);

        TextView name = view.findViewById(R.id.txt_name);
        name.setText(mItems.get(position).getName());


        return view;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }


}