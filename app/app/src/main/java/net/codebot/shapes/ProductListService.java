package net.codebot.shapes;

import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class ProductListService {
    static Task<ArrayList<Product>> getProducts() {
        return HTTPConnector.getProducts().continueWith(new Continuation<String, ArrayList<Product>>() {
            @Override
            public ArrayList<Product> then(@NonNull Task<String> task) throws Exception {
                // This continuation runs on either success or failure, but if the task
                // has failed then getResult() will throw an Exception which will be
                // propagated down.

                //{"products":[{"quantity":123,"name":"hamburger","location":"E7","id":"1585516373471","pic":"https://storage.googleapis.com/store-front-backend.appspot.com/Products/1585516373471.jpg"},{"quantity":123,"name":"hamburger","location":"E7","id":"1585516776539","pic":"https://storage.googleapis.com/store-front-backend.appspot.com/Products/1585516776539.jpg"}]}
                String returnedJSON = task.getResult();

                //converts to json to just get json array [{"quantity":123,"name":"hamburger","location":"E7","id":"1585516373471","pic":"https://storage.googleapis.com/store-front-backend.appspot.com/Products/1585516373471.jpg"}, {"quantity":"100","name":"newProd","location":"G34","id":"1585601148728","pic":"https://storage.googleapis.com/store-front-backend.appspot.com/Products/1585601148728.jpg"}]
                JsonArray jsonProdArray = new Gson().fromJson(returnedJSON, JsonObject.class).getAsJsonArray("products");
                Log.i("JSON ARRAY", jsonProdArray.toString());
                //convert JsonArray to arraylist of product objects
                ArrayList<Product> productArrayList = (ArrayList<Product>) (new Gson()).fromJson(jsonProdArray, new TypeToken<ArrayList<Product>>() {}.getType());
                Log.i("After productArrayList", Integer.toString(productArrayList.size()));
                return productArrayList;
            }
        });
    }

    static Task<String> addProduct(Product prod) {

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("name", prod.name);
        requestData.put("pic", prod.pic);
        requestData.put("quantity", prod.quantity);
        requestData.put("location", prod.location);


        return HTTPConnector.addProduct(requestData).continueWith(new Continuation<String, String>() {
            @Override
            public String then(@NonNull Task<String> task) throws Exception {
                // This continuation runs on either success or failure, but if the task
                // has failed then getResult() will throw an Exception which will be
                // propagated down.
                return task.getResult();

            }
        });
    }

    static Task<String> removeProduct(String productID) {

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("productID", productID);


        return HTTPConnector.removeProduct(requestData).continueWith(new Continuation<String, String>() {
            @Override
            public String then(@NonNull Task<String> task) throws Exception {
                // This continuation runs on either success or failure, but if the task
                // has failed then getResult() will throw an Exception which will be
                // propagated down.
                return task.getResult();

            }
        });
    }
}
