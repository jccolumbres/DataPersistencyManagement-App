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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class JSONHelper {
    private static final String FILE_NAME = "menuitems.json";
    private static final String TAG = JSONHelper.class.getSimpleName();


    public static boolean exportToJSON(Context context,List<DataItem> dataItemList){
        //Refer to external storage
        FileOutputStream outputStream = null;
        File file = new File(Environment.getExternalStorageDirectory(),FILE_NAME);

        //Instantiate data class the store argument result/value
        DataItems dataItems = new DataItems();
        dataItems.setDataItems(dataItemList);

        //Prepare/Convert file content to JSON format
        Gson gson = new Gson();
        String fileContent = gson.toJson(dataItems);


        try {
            //Create file then write the content inside
            outputStream = new FileOutputStream(file);
            outputStream.write(fileContent.getBytes());
            Log.i(TAG, "File creation successful");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //close the FileOutputStream
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static List<DataItem> importFromJSON(Context context){
        //Prepare FileReader class then refer to external storage
        FileReader reader = null;
        File file = new File(Environment.getExternalStorageDirectory(), FILE_NAME);

        try {
            //Instantiate FileReader with file
            reader = new FileReader(file);
            //Populate DataItems class and return result
            Gson gson = new Gson();
            DataItems dataItems = gson.fromJson(reader, DataItems.class);
            return dataItems.getDataItems();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public static List<DataItem> importFromResource(Context context){
        //Prepare InputStreamReader class and InputStream
        InputStreamReader reader = null;
        InputStream inputStream = null;

        try {
            //Instantiate InputStream with static raw file the assign it to InputStreamReader
            inputStream = context.getResources().openRawResource(R.raw.menuitems);
            reader = new InputStreamReader(inputStream);
            //Populate DataItems class and return result
            Gson gson = new Gson();
            DataItems dataItems = gson.fromJson(reader, DataItems.class);
            return dataItems.getDataItems();
        }finally {
            //close both classes
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    public static Boolean deleteExportedFile(){
        File file = new File(Environment.getExternalStorageDirectory(),FILE_NAME);
        if (file.exists()){
            file.delete();
            return true;
        }
        return false;
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