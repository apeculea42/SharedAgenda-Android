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

import java.util.List;

/**
 * Created by ale on 04/02/2018.
 */

public class UsersAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private List<String> mItems;

    public UsersAdapter(Context context, List<String> items){
        super(context, R.layout.list_users);
        mContext = context;
        mItems = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.list_users, null, false);

        TextView txtName = view.findViewById(R.id.txt_name);
        txtName.setText(mItems.get(position));

        ImageView image = view.findViewById(R.id.profile_image_user);




      return view;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

}
