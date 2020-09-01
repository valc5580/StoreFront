package net.codebot.shapes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.gson.Gson;

import java.util.Map;

public class HTTPConnector {

    static Task<String> getProducts() {

        return FirebaseFunctions.getInstance() // Optional region: .getInstance("europe-west1")
                .getHttpsCallable("getProducts")
                .call()
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        return new Gson().toJson(task.getResult().getData());
                    }
                });
    }

    static Task<String> addProduct(Map<String, Object> requestData){

        return FirebaseFunctions.getInstance()
                .getHttpsCallable("addProduct")
                .call(requestData)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        Log.i("HTTP CONNECTOR", "Returned "+task.getResult().getData().toString());
                        return task.getResult().getData().toString();
                    }
                });


    }

    static Task<String> removeProduct(Map<String, Object> requestData){

        return FirebaseFunctions.getInstance()
                .getHttpsCallable("removeProduct")
                .call(requestData)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        Log.i("HTTP CONNECTOR", "Returned "+task.getResult().getData().toString());
                        return task.getResult().getData().toString();
                    }
                });


    }

    static Task<String> updateStore(Map<String,Object> requestData){

        return FirebaseFunctions.getInstance()
                .getHttpsCallable("upsertStore")
                .call(requestData)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        Log.i("HTTP CONNECTOR", "Returned "+task.getResult().getData().toString());
                        return task.getResult().getData().toString();
                    }
                });

    }
    static Task<String> validateLogin(Map<String, Object> requestData){

        return FirebaseFunctions.getInstance()
                .getHttpsCallable("validateLogin")
                .call(requestData)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        Log.i("HTTP CONNECTOR", "Returned "+task.getResult().getData().toString());
                        return task.getResult().getData().toString();
                    }
                });
    }

    static Task<String> getStore() {

        return FirebaseFunctions.getInstance() // Optional region: .getInstance("europe-west1")
                .getHttpsCallable("getStore")
                .call()
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        return new Gson().toJson(task.getResult().getData());
                    }
                });
    }

    static Task<String> getPath(Map<String, Object> requestData) {


        return FirebaseFunctions.getInstance() // Optional region: .getInstance("europe-west1")
                .getHttpsCallable("getPath")
                .call(requestData)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        return new Gson().toJson(task.getResult().getData());
                    }
                });
    }


    }
