package com.example.zooseekercse110team7.location;

import android.content.Context;
import android.util.Log;

import com.example.zooseekercse110team7.GlobalDebug;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class MockLocationParser {
    public static List<Coord> parseMockLocations(Context aContext , String aPath){
        try{
            InputStream input = aContext.getAssets().open(aPath);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Coord>>(){}.getType();

            List<Coord> parsedResult = gson.fromJson(reader,type);
            if(GlobalDebug.DEBUG){
                for(Coord aCoord: parsedResult){
                    Log.d("MockLocationParser", aCoord.toString());
                }
            }

            return parsedResult;

        }catch (IOException e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
