package org.ayannah.jcc.datapersistencyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.ayannah.jcc.datapersistencyapp.model.DataItem;
import org.ayannah.jcc.datapersistencyapp.sample.SampleDataProvider;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
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
        ItemAdapter adapter = new ItemAdapter(this,dataItemList);

        //Initialize listview and set adapter
        ListView listView = findViewById(R.id.lv_items);
        listView.setAdapter(adapter);

    }
}
