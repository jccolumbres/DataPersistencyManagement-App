package org.ayannah.jcc.datapersistencyapp;

import android.content.Context;

import org.ayannah.jcc.datapersistencyapp.model.DataItem;

import java.util.List;

public class JSONHelper {

    public static final String FILE_NAME = "menuitems.json";
    public static final String TAG =  "JSONHelper";


    public static boolean exportToJSON(Context context, List<DataItem> dataItemList){
        return false;
    }

    public static List<DataItem> importFromJSON(Context context){
        return null;
    }
}
