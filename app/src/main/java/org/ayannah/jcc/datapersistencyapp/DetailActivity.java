package org.ayannah.jcc.datapersistencyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.ayannah.jcc.datapersistencyapp.model.DataItem;
import org.ayannah.jcc.datapersistencyapp.sample.SampleDataProvider;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String itemId = getIntent().getExtras().getString(ItemAdapterRecyclerView.ITEM_ID_KEY);
        DataItem item = SampleDataProvider.dataItemHashMap.get(itemId);
        Toast.makeText(this, "You have selected "+item.getItemName(), Toast.LENGTH_SHORT).show();

        }
}
