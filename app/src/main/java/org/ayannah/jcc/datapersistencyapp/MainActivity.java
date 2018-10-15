package org.ayannah.jcc.datapersistencyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.ayannah.jcc.datapersistencyapp.model.DataItem;

public class MainActivity extends AppCompatActivity {

    private TextView tvOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataItem dataItem = new DataItem();
        tvOut = findViewById(R.id.tv_out);
        tvOut.setText(dataItem.toString());

    }
}
