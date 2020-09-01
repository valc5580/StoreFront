package net.codebot.shapes;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;

public class LoginService {

    static Task<Boolean> validateLogin(String user, String pass) {

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("user", user);
        requestData.put("pass", pass);

        return HTTPConnector.validateLogin(requestData).continueWith(new Continuation<String, Boolean>() {
            @Override
            public Boolean then(@NonNull Task<String> task) throws Exception {
                // This continuation runs on either success or failure, but if the task
                // has failed then getResult() will throw an Exception which will be
                // propagated down.

                return task.getResult().equalsIgnoreCase("true");

            }
        });
    }
}
