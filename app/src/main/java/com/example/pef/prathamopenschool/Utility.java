package com.example.pef.prathamopenschool;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by Varun Anand on 10-Aug-2015.
 */
public class Utility {

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    private final DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


    public String GetCurrentDateTime() {
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public String GetCurrentDate(){
        Calendar cal = Calendar.getInstance();
        return dateFormat1.format(cal.getTime());
    }

    public UUID GetUniqueID() {
        return UUID.randomUUID();
    }

    public int ConvertBooleanToInt(Boolean val) {
        return (val) ? 1 : 0;
    }

    public static String getProperty(String key,Context context) {
        try{
            Properties properties = new Properties();
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("config.properties");
            properties.load(inputStream);
            return properties.getProperty(key);
        }
        catch (Exception ex){
            return null;
        }
    }

}
