package com.example.samsungproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter  extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> usedItems;

    public ListViewAdapter(Context context, ArrayList<String> usedItems) {
        super(context, R.layout.last_used_item);
        this.usedItems = usedItems;
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.last_used_item, parent, false);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        TextView textView = (TextView) v.findViewById(R.id.used_item_text);
        textView.setText(usedItems.get(position));
        imageView.setImageResource(R.drawable.ic_refresh_black_24dp);
        return v;
    }

    @Override
    public void add(@Nullable String object) {
        super.add(object);
    }
}
