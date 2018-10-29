package org.ayannah.jcc.datapersistencyapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import org.ayannah.jcc.datapersistencyapp.model.DataItem;

public class DataSource {

    private Context mContext;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDatabase;

    public DataSource(Context context) {
        this.mContext = context;
        mDBHelper = new DBHelper(mContext);
        mDatabase = mDBHelper.getWritableDatabase();
    }


    public void open(){
        mDatabase = mDBHelper.getWritableDatabase();
    }

    public void close(){
        mDBHelper.close();
    }

    public DataItem createItem(DataItem item){
        ContentValues values = item.toValues();
        mDatabase.insert(ItemsTable.TABLE_ITEMS,null,values);
        return item;
    }
    public long getDataItemListCount(){
        return DatabaseUtils.queryNumEntries(mDatabase,ItemsTable.TABLE_ITEMS);
    }
}
