package org.ayannah.jcc.datapersistencyapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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
}
