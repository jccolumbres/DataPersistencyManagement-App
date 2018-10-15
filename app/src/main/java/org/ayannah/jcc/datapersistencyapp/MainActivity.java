package org.ayannah.jcc.datapersistencyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.ayannah.jcc.datapersistencyapp.model.DataItem;
import org.ayannah.jcc.datapersistencyapp.sample.SampleDataProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private List<DataItem> dataItemList = SampleDataProvider.dataItemList;
    private List<String> itemStrings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        for (DataItem item: dataItemList) {
            itemStrings.add(item.getItemName());
        }

        Collections.sort(itemStrings);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                itemStrings);


        ListView listView = findViewById(R.id.lv_items);
        listView.setAdapter(adapter);


    }
}
