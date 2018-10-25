package org.ayannah.jcc.datapersistencyapp;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;

import org.ayannah.jcc.datapersistencyapp.model.DataItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class JSONHelper {

    public static final String FILE_NAME = "menuitems.json";
    public static final String TAG = "JSONHelper";


    public static boolean exportToJSON(Context context, List<DataItem> dataItemList) {

        DataItems menuData = new DataItems();
        menuData.setDataItems(dataItemList);

        Gson gson = new Gson();
        String jsonString = gson.toJson(menuData);

        FileOutputStream outputStream = null;
        File file = new File(Environment.getExternalStorageDirectory(), FILE_NAME);
        boolean isExported = false;

        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(jsonString.getBytes());
            isExported = true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isExported;

    }

    public static List<DataItem> importFromJSON(Context context) {
        FileReader reader = null;
        File file = new File(Environment.getExternalStorageDirectory(),FILE_NAME);

        try {
            reader = new FileReader(file);
            Gson gson = new Gson();
            DataItems dataItems = gson.fromJson(reader,DataItems.class);
            return dataItems.getDataItems();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public static boolean deleteJSONFile(){
        File file = new File(Environment.getExternalStorageDirectory(),FILE_NAME);
        boolean isDeleted = false;
        if (file.exists()) {
            file.delete();
            isDeleted = true;
        }
        return isDeleted;
    }
    static class DataItems{
        List<DataItem> dataItems;

        public List<DataItem> getDataItems() {
            return dataItems;
        }

        public void setDataItems(List<DataItem> dataItems) {
            this.dataItems = dataItems;
        }
    }

}