package org.ayannah.jcc.datapersistencyapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.ayannah.jcc.datapersistencyapp.database.DBHelper;
import org.ayannah.jcc.datapersistencyapp.database.DataSource;
import org.ayannah.jcc.datapersistencyapp.model.DataItem;
import org.ayannah.jcc.datapersistencyapp.sample.SampleDataProvider;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_WRITE = 0;
    private static String TAG = MainActivity.class.getSimpleName();

    private static final int SIGNIN_REQUEST = 1001;
    public static final String GLOBAL_KEYS = "global_keys";
    private List<DataItem> dataItemList = SampleDataProvider.dataItemList;
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener;
    private boolean permissionGranted;

    List<DataItem> listFromDb;
    DataSource mDataSource;

    //For navigation and category selection
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    String[] mCategories;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();
        mDataSource = new DataSource(this);
        mDataSource.open();
        mDataSource.seedDatabase(dataItemList);

        //For navigation and category selection -- start
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mCategories = mDataSource.getCategoriesDB();//getResources().getStringArray(R.array.categories);
        mDrawerList = findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, mCategories));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = mCategories[position];
                displayItemsDB(category);
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });
        //For navigation and category selection -- end
        listFromDb = mDataSource.getAllItems(null);
        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                Log.i("Preferences", "SharedPref Key: " + s);
            }
        };
        Collections.sort(dataItemList, new Comparator<DataItem>() {
            @Override
            public int compare(DataItem o1, DataItem o2) {
                return o1.getItemName().compareTo(o2.getItemName());
            }
        });
        ItemAdapterRecyclerView adapter = new ItemAdapterRecyclerView(this, listFromDb);
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean grid = settings.getBoolean(getString(R.string.pref_display_grid), false);
        settings.registerOnSharedPreferenceChangeListener(prefListener);

        recyclerView = findViewById(R.id.rv_items);
        if (grid) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }

        displayItemsDB(null);
    }

    public void displayItemsDB(String category) {
        listFromDb = mDataSource.getAllItems(category);
        ItemAdapterRecyclerView adapterRecyclerView = new ItemAdapterRecyclerView(this, listFromDb);
        recyclerView.setAdapter(adapterRecyclerView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDataSource.open();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_signin:
                Intent intent = new Intent(this, SigninActivity.class);
                startActivityForResult(intent, SIGNIN_REQUEST);
                return true;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, PrefsActivity.class);
                startActivity(settingsIntent);
                finish();
                return true;
            case R.id.action_export:
                boolean result = JSONHelper.exportToJSON(this, dataItemList);
                if (result) {
                    Toast.makeText(this, "Data exported", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_import:
                List<DataItem> dataItems = JSONHelper.importFromJSON(this);
                if (dataItems != null) {
                    for (DataItem dataItem : dataItems) {
                        Log.i(TAG, "Item exported: " + dataItem.getItemName());
                    }
                } else {
                    Toast.makeText(this, "File not existing", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_delete:
                boolean output = JSONHelper.deleteExportedFile();
                if (output) {
                    Toast.makeText(this, "File deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Not existing", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_import_raw:
                List<DataItem> dataItemsRaw = JSONHelper.importFromResource(this);
                if (dataItemsRaw != null) {
                    for (DataItem dataItem : dataItemsRaw) {
                        Log.i(TAG, "Item exportedRaw: " + dataItem.getItemName());
                    }
                } else {
                    Toast.makeText(this, "File not existing", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_all_items:
                displayItemsDB(null);
                return true;
            case R.id.action_choose_category:
                mDrawerLayout.openDrawer(mDrawerList);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == SIGNIN_REQUEST) {
            String email = data.getStringExtra(SigninActivity.EMAIL_KEY);
            Toast.makeText(this, "You signed in as " + email, Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor editor =
                    getSharedPreferences(GLOBAL_KEYS, MODE_PRIVATE).edit();
            editor.putString(SigninActivity.EMAIL_KEY, email);
            editor.apply();
        }

    }
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    // Initiate request for permissions.
    private boolean checkPermissions() {

        if (!isExternalStorageReadable() || !isExternalStorageWritable()) {
            Toast.makeText(this, "This app only works on devices with usable external storage",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_WRITE);
            return false;
        } else {
            return true;
        }
    }

    // Handle permissions result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_WRITE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                    Toast.makeText(this, "External storage permission granted",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "You must grant permission!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
