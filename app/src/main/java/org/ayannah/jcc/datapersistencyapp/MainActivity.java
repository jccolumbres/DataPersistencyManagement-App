package org.ayannah.jcc.datapersistencyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import org.ayannah.jcc.datapersistencyapp.model.DataItem;
import org.ayannah.jcc.datapersistencyapp.sample.SampleDataProvider;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //initiate List to store content from data provider class
    private List<DataItem> dataItemList = SampleDataProvider.dataItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //sort list alphabetically
        Collections.sort(dataItemList, new Comparator<DataItem>() {
            @Override
            public int compare(DataItem o1, DataItem o2) {
                return o1.getItemName().compareTo(o2.getItemName());
            }
        });
        //create a new adapter with your own customize adapter
//        ItemAdapterListView adapter = new ItemAdapterListView(this,dataItemList);
        ItemAdapterRecyclerView adapter = new ItemAdapterRecyclerView(this, dataItemList);

        //Initialize listview and set adapter
//        ListView listView = findViewById(R.id.lv_items);
        RecyclerView recyclerView = findViewById(R.id.rv_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
}
