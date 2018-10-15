package org.ayannah.jcc.datapersistencyapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.ayannah.jcc.datapersistencyapp.model.DataItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ItemAdapter extends ArrayAdapter<DataItem> {

    private List<DataItem> items;
    private LayoutInflater layoutInflater;

    public ItemAdapter(Context context,List<DataItem> objects) {
        super(context, R.layout.list_item, objects);


        items = objects;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        //check if view is null (ListView)
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item,parent,false);
        }

        //Initialize listview components
        TextView tvItemName = convertView.findViewById(R.id.tvItemName);
        ImageView imageView = convertView.findViewById(R.id.imageView);

        //Initialize object call with current item
        DataItem item = items.get(position);

        //Display textview and imageview of the item
        tvItemName.setText(item.getItemName());
        InputStream inputStream = null;
        try {
            String imageFileName = item.getImage();
            inputStream = getContext().getAssets().open(imageFileName);
            Drawable d = Drawable.createFromStream(inputStream,null);
            imageView.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (inputStream != null){
                    inputStream.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        //return view for usage
        return convertView;
    }
}
