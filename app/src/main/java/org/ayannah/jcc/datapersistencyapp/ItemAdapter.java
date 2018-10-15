package org.ayannah.jcc.datapersistencyapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.ayannah.jcc.datapersistencyapp.model.DataItem;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<DataItem> {
    private List<DataItem> dataItems;
    private LayoutInflater layoutInflater;

    public ItemAdapter(Context context, List<DataItem> objects) {
        super(context, R.layout.list_item, objects);

        dataItems = objects;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item,parent,false);
        }
        TextView tvName = convertView.findViewById(R.id.tvItemName);
        ImageView imageView = convertView.findViewById(R.id.imageView);

        DataItem item = dataItems.get(position);

        tvName.setText(item.getItemName());
        imageView.setImageResource(R.drawable.apple_pie);

        return convertView;
    }
}
