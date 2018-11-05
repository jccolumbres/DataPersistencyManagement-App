package org.ayannah.jcc.datapersistencyapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import org.ayannah.jcc.datapersistencyapp.model.DataItem;

import java.util.ArrayList;
import java.util.List;

public class DataSource {

    private Context mContext;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDatabase;

    public DataSource(Context context) {
        this.mContext = context;
        mDBHelper = new DBHelper(mContext);
        mDatabase = mDBHelper.getWritableDatabase();
    }

    public void open() {
        mDatabase = mDBHelper.getWritableDatabase();
    }

    public void close() {
        mDBHelper.close();
    }

    public DataItem createItem(DataItem item) {
        ContentValues values = item.toValues();
        mDatabase.insert(ItemsTable.TABLE_ITEMS, null, values);
        return item;
    }

    public long getDataItemListCount() {
        return DatabaseUtils.queryNumEntries(mDatabase, ItemsTable.TABLE_ITEMS);
    }

    public void seedDatabase(List<DataItem> dataItemList) {
        long itemCount = getDataItemListCount();
        if (itemCount == 0) {
            for (DataItem item :
                    dataItemList) {
                try {
                    createItem(item);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(mContext, "Data saved to local database", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mContext, "Data already inserted", Toast.LENGTH_LONG).show();
        }
    }

    public String[] getCategoriesDB() {
        ArrayList<String> categories = new ArrayList<>();
        String[] category = {ItemsTable.COLUMN_CATEGORY};
        Cursor cursor = mDatabase.query(true,ItemsTable.TABLE_ITEMS,category,
                null, null, null, null, ItemsTable.COLUMN_CATEGORY + " DESC",null);

        while (cursor.moveToNext()) {
            categories.add(cursor.getString(cursor.getColumnIndex(ItemsTable.COLUMN_CATEGORY)));
        }
        cursor.close();
        return categories.toArray(new String[categories.size()]);
    }

    public List<DataItem> getAllItems(String category) {
        List<DataItem> dataItemList = new ArrayList<>();
        Cursor cursor = null;

        if (category == null) {
            cursor = mDatabase.query(ItemsTable.TABLE_ITEMS, ItemsTable.ALL_COLUMNS,
                    null, null, null, null, ItemsTable.COLUMN_NAME);
        } else {
            String[] categories = {category};
            cursor = mDatabase.query(ItemsTable.TABLE_ITEMS, ItemsTable.ALL_COLUMNS,
                    ItemsTable.COLUMN_CATEGORY + "=?", categories, null, null, ItemsTable.COLUMN_NAME);
        }
        while (cursor.moveToNext()) {
            DataItem item = new DataItem();
            item.setItemId(cursor.getString(cursor.getColumnIndex(ItemsTable.COLUMN_ID)));
            item.setItemName(cursor.getString(cursor.getColumnIndex(ItemsTable.COLUMN_NAME)));
            item.setCategory(cursor.getString(cursor.getColumnIndex(ItemsTable.COLUMN_CATEGORY)));
            item.setDescription(cursor.getString(cursor.getColumnIndex(ItemsTable.COLUMN_DESCRIPTION)));
            item.setSortPosition(cursor.getInt(cursor.getColumnIndex(ItemsTable.COLUMN_POSITION)));
            item.setPrice(cursor.getDouble(cursor.getColumnIndex(ItemsTable.COLUMN_PRICE)));
            item.setImage(cursor.getString(cursor.getColumnIndex(ItemsTable.COLUMN_IMAGE)));
            dataItemList.add(item);
        }

        cursor.close();
        return dataItemList;
    }
}
