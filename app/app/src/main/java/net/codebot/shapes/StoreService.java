package net.codebot.shapes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StoreService {

    public static HashMap<String,String> createMapFromGrid(int [][] grid){
        HashMap<String,String> gridMap = new HashMap<>();

        for (int i = 0; i < grid.length; i++ ){
            for (int j = 0; j< grid[i].length; j++){
                String key = Integer.toString(i) + Integer.toString(j);
                String value = Integer.toString(grid[i][j]);
                gridMap.put(key,value);
            }
        }

        return gridMap;
    }

    public static int [][] createGridFromMap (HashMap<String,String> gridMap){
        int [][] grid = new int[10][10];

        for (String key : gridMap.keySet()) {
            String value = gridMap.get(key);

            int rowNum = Character.getNumericValue(key.charAt(0));
            int colNum = Character.getNumericValue(key.charAt(1));
            grid[rowNum][colNum] = Integer.parseInt(value);
        }

        return grid;
    }

    static JSONObject convertMapToJSON(HashMap<String,String> floorMap){
        JSONObject jsonData = new JSONObject();
        for (String key : floorMap.keySet()) {
            Object value = floorMap.get(key);
            try {
                jsonData.put(key, value);
            }
            catch (JSONException e){
            }
        }
        return jsonData;
    }

    static HashMap<String,String> convertJSONtoMAP(JSONObject data){
        HashMap<String,String> floorMap = new HashMap<>();
        Iterator<String> keys = data.keys();

        while(keys.hasNext()) {
            String key = keys.next();
            try{
                String value = data.getString(key);
                floorMap.put(key,value);
            }
            catch (JSONException e){

            }


        }
        return floorMap;
    }

    static String convertStoreToGSON(Store store){
        ArrayList <Floor> floors  = store.getFloors();

        JSONArray floorArray = new JSONArray();

        for (Floor floor: floors){
            HashMap<String,String> floorMap = createMapFromGrid(floor.getGrid());
            JSONObject floorJson = convertMapToJSON(floorMap);
            floorArray.put(floorJson);
        }
        return floorArray.toString();

    }

    static Task<String> updateStore(Store store){

        Map<String, Object> requestData = new HashMap<>();

        String storeString = StoreService.convertStoreToGSON(store);
        requestData.put( "storeStr", storeString );


        return HTTPConnector.updateStore(requestData).continueWith(new Continuation<String, String>() {
            @Override
            public String then(@NonNull com.google.android.gms.tasks.Task<String> task) throws Exception {
                // This continuation runs on either success or failure, but if the task
                // has failed then getResult() will throw an Exception which will be
                // propagated down.
                return task.getResult();

            }
        });
    }

    static Task<Store> getStore(){
        return HTTPConnector.getStore().continueWith(new Continuation<String,Store>() {
            @Override
            public Store then(@NonNull Task<String> task) throws Exception {
                // This continuation runs on either success or failure, but if the task
                // has failed then getResult() will throw an Exception which will be
                // propagated down.

                String returnedJSON = task.getResult();
                // this is a json array of hashmaps
                Log.i("JSON STORE", returnedJSON);

                //JSONObject data = new JSONObject(returnedJSON);
                JSONArray data = new JSONArray(returnedJSON);
                Log.i("JSON array", data.toString());
                JSONObject storeStr = data.getJSONObject(0);
                Log.i("storeStr",storeStr.toString());
                JSONArray floors = new JSONArray(storeStr.get("storeStr").toString());
                ArrayList<Floor> floorArrayList = new ArrayList<>();


                for (int i=0; i< floors.length(); i++){
                    JSONObject obj = floors.getJSONObject(i);
                    HashMap<String,String> floorMap = convertJSONtoMAP(obj);
                    Floor floor = new Floor(createGridFromMap(floorMap));
                    floorArrayList.add(floor);
                }

                Store store = new Store(floorArrayList);
                Log.i("RET","Returning Store");
                return store;

            }
        });
    }

    static Task<ArrayList<String>> getPath(ArrayList<String> productLocations){

        Map<String, Object> requestData = new HashMap<>();


        JSONArray locationJsonArray = new JSONArray();
        for (String location: productLocations){
            locationJsonArray.put(location);
        }
        Log.i("jsonLocations", locationJsonArray.toString());
        requestData.put("items", locationJsonArray);
        

        return HTTPConnector.getPath(requestData).continueWith(new Continuation<String, ArrayList<String>>() {
            @Override
            public ArrayList<String> then(@NonNull com.google.android.gms.tasks.Task<String> task) throws Exception {
                // This continuation runs on either success or failure, but if the task
                // has failed then getResult() will throw an Exception which will be
                // propagated down.
                ArrayList<String> path = new ArrayList<>();

                String returnedJSON = task.getResult();
                JSONArray arr = new JSONArray(returnedJSON);

                for (int i=0; i<arr.length();i++){
                    path.add(arr.getString(i));
                }

                Log.i("PATH ", arr.toString());

                return path;
            }
        });
    }




}
