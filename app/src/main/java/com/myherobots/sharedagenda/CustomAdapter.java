package com.myherobots.sharedagenda;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ale on 23/01/2018.
 */

public class CustomAdapter extends ArrayAdapter<ChatMessage> {

    private Context mContext;
    private List<ChatMessage> mItems;

    public CustomAdapter(@NonNull Context context, List<ChatMessage> items) {
        super(context, R.layout.list_element);
        mContext = context;
        mItems = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.list_element, null, false);

        TextView txtName = view.findViewById(R.id.txt_name);
        txtName.setText(mItems.get(position).getTitle());

        TextView startTime = view.findViewById(R.id.start_time);
        startTime.setText(mItems.get(position).getStartTime());

        TextView endTime = view.findViewById(R.id.end_time);
        endTime.setText(mItems.get(position).getEndTime());

        /*TextView duration = view.findViewById(R.id.duration);
        duration.setText(mItems.get(position).getDuration());

*/        return view;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }


}
